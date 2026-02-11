package roidrole.tfutils.mixins.thebetweenlands;

import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.common.item.tools.ItemGreatsword;

@Mixin(ItemGreatsword.class)
//All classes implementing IExtendedReach extend ItemGreatsword
public abstract class ItemGreatswordMixin {
	@Shadow(remap = false)
	public abstract double getReach();

	//Removes the need for the reach handler
	@Inject(
		method = "getAttributeModifiers",
		at = @At(
			value = "RETURN"
		),
		remap = false
	)
	private void addReachAttribute(EntityEquipmentSlot slot, ItemStack stack, CallbackInfoReturnable<Multimap<String, AttributeModifier>> cir){
		cir.getReturnValue().put(EntityPlayer.REACH_DISTANCE.getName(), new AttributeModifier("Betweenlands Reach", this.getReach(), 0));
	}
}
