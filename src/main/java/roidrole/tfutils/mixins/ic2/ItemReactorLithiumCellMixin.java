package roidrole.tfutils.mixins.ic2;

import ic2.core.item.reactor.ItemReactorLithiumCell;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import roidrole.tfutils.Capabilities;

@Mixin(ItemReactorLithiumCell.class)
public class ItemReactorLithiumCellMixin extends Item {
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt){
		return new Capabilities.FluidCapabilityRodProvider(stack);
	}
}

