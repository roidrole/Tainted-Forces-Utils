package roidrole.tfutils.handlers;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.ImmutablePair;
import roidrole.tfutils.proxy.ClientProxy;
import thebetweenlands.api.aspect.ItemAspectContainer;
import thebetweenlands.api.capability.IFoodSicknessCapability;
import thebetweenlands.api.item.IDecayFood;
import thebetweenlands.api.item.IEquippable;
import thebetweenlands.api.item.IFoodSicknessItem;
import thebetweenlands.common.capability.circlegem.CircleGemType;
import thebetweenlands.common.capability.foodsickness.FoodSickness;
import thebetweenlands.common.config.BetweenlandsConfig;
import thebetweenlands.common.handler.FoodSicknessHandler;
import thebetweenlands.common.herblore.aspect.AspectManager;
import thebetweenlands.common.item.armor.amphibious.AmphibiousArmorUpgrades;
import thebetweenlands.common.recipe.censer.AbstractCenserRecipe;
import thebetweenlands.common.recipe.misc.AnimatorRecipe;
import thebetweenlands.common.recipe.mortar.PestleAndMortarRecipe;
import thebetweenlands.common.recipe.purifier.PurifierRecipe;
import thebetweenlands.common.registries.CapabilityRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static thebetweenlands.common.capability.circlegem.CircleGemHelper.ITEM_GEM_NBT_TAG;

public class ItemTooltipHandler {
	//The Betweenlands, why private?
	public static final String NBT_AMPHIBIOUS_UPGRADE_DAMAGE_KEY = "thebetweenlands.amphibious_armor_upgrade_damage";
	public static final String NBT_AMPHIBIOUS_UPGRADE_MAX_DAMAGE_KEY = "thebetweenlands.amphibious_armor_upgrade_max_damage";

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onItemTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		Item item = stack.getItem();
		List<String> toolTip = event.getToolTip();
		EntityPlayer player = event.getEntityPlayer();
		NBTTagCompound nbt = stack.getTagCompound();

		//-----Betweenlands-----
		if(nbt != null){
			int armorUpgradeDamage = nbt.getInteger(NBT_AMPHIBIOUS_UPGRADE_DAMAGE_KEY);
			if(armorUpgradeDamage > 0) {
				int maxArmorUpgradeDamage = nbt.getInteger(NBT_AMPHIBIOUS_UPGRADE_MAX_DAMAGE_KEY);
				toolTip.add(I18n.format("tooltip.bl.damaged_armor_upgrade", Math.max(0, maxArmorUpgradeDamage - armorUpgradeDamage), maxArmorUpgradeDamage));
			}

			if(nbt.getInteger(ITEM_GEM_NBT_TAG) != 0){
				CircleGemType type = CircleGemType.TYPES[nbt.getInteger(ITEM_GEM_NBT_TAG)-1];
				toolTip.add(I18n.format("tooltip.bl.circlegem." + type.name));
			}
		}


		if(item instanceof IDecayFood) {
			((IDecayFood)item).getDecayFoodTooltip(stack, player != null ? player.world : null, toolTip, event.getFlags());
		} else if(BetweenlandsConfig.GENERAL.decayFoodList.getStats(stack) != null) {
			toolTip.add(I18n.format("tooltip.bl.decay_food", stack.getDisplayName()));
		}

		if(player != null) {
			if(FoodSicknessHandler.isFoodSicknessEnabled(player.getEntityWorld()) && item instanceof ItemFood && item instanceof IFoodSicknessItem && ((IFoodSicknessItem)item).canGetSickOf(player, stack)) {
				IFoodSicknessCapability cap = player.getCapability(CapabilityRegistry.CAPABILITY_FOOD_SICKNESS, null);
				if(cap != null) {
					FoodSickness sickness = cap.getSickness(stack.getItem());
					int hatred = cap.getFoodHatred(stack.getItem());
					((IFoodSicknessItem)stack.getItem()).getSicknessTooltip(stack, sickness, hatred, event.getFlags().isAdvanced(), toolTip);
				}
			}

			if(item instanceof IEquippable && ((IEquippable)item).canEquip(stack, player, player)) {
				toolTip.add(I18n.format("tooltip.bl.item.equippable"));
			}
		}

		//Machines this item is used in
		if(!BetweenlandsConfig.GENERAL.itemUsageTooltip){
			return;
		}

		List<String> usedInMachines = new ArrayList<>();

		if(player != null) {
			AspectManager aspectManager = AspectManager.get(player.world);

			if(!aspectManager.getStaticAspects(stack).isEmpty()) {
				usedInMachines.add(I18n.format("tooltip.bl.recipes.static_aspects"));
			}

			if(!ItemAspectContainer.fromItem(stack, aspectManager).isEmpty()) {
				usedInMachines.add(I18n.format("tooltip.bl.recipes.aspects"));
			}
		}

		if(PestleAndMortarRecipe.getRecipe(stack, stack, true) != null) {
			usedInMachines.add(I18n.format("tooltip.bl.recipes.mortar"));
		}

		if(AnimatorRecipe.getRecipe(stack) != null) {
			usedInMachines.add(I18n.format("tooltip.bl.recipes.animator"));
		}

		IFluidHandler fluidHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		if(AbstractCenserRecipe.getRecipe(stack) != null || (fluidHandler != null && AbstractCenserRecipe.getRecipe(fluidHandler.drain(Integer.MAX_VALUE, false)) != null)) {
			usedInMachines.add(I18n.format("tooltip.bl.recipes.censer_primary"));
		}
		if(AbstractCenserRecipe.getRecipeWithSecondaryInput(stack) != null) {
			usedInMachines.add(I18n.format("tooltip.bl.recipes.censer_secondary"));
		}

		if(!PurifierRecipe.getRecipeOutput(stack).isEmpty()) {
			usedInMachines.add(I18n.format("tooltip.bl.recipes.purifier"));
		}

		//Amphibious upgrade
		List<String> amphibiousUpgrades = new ArrayList<>();
		if(AmphibiousArmorUpgrades.getUpgrade(EntityEquipmentSlot.HEAD, stack) != null) {
			amphibiousUpgrades.add(I18n.format("tooltip.bl.amphibious_upgrade.helmet"));
		}
		if(AmphibiousArmorUpgrades.getUpgrade(EntityEquipmentSlot.CHEST, stack) != null) {
			amphibiousUpgrades.add(I18n.format("tooltip.bl.amphibious_upgrade.chestplate"));
		}
		if(AmphibiousArmorUpgrades.getUpgrade(EntityEquipmentSlot.LEGS, stack) != null) {
			amphibiousUpgrades.add(I18n.format("tooltip.bl.amphibious_upgrade.leggings"));
		}
		if(AmphibiousArmorUpgrades.getUpgrade(EntityEquipmentSlot.FEET, stack) != null) {
			amphibiousUpgrades.add(I18n.format("tooltip.bl.amphibious_upgrade.boots"));
		}
		if(!amphibiousUpgrades.isEmpty()) {
			usedInMachines.add(I18n.format("tooltip.bl.amphibious_upgrade.format", String.join("/", amphibiousUpgrades)));
		}

		//Steeping pot, Silk Bundle, hard-coded
		Set<String> tooltip = ClientProxy.betweenlandsItemStackTooltips.get(new ImmutablePair<>(stack.getItem(), stack.getItemDamage()));
		if(tooltip != null){
			usedInMachines.addAll(tooltip);
		}
		tooltip = ClientProxy.betweenlandsItemTooltips.get(stack.getItem());
		if(tooltip != null){
			usedInMachines.addAll(tooltip);
		}

		if(!usedInMachines.isEmpty()) {
			toolTip.add(I18n.format("tooltip.bl.recipes.used_in", String.join(", ", usedInMachines)));
		}
	}
}
