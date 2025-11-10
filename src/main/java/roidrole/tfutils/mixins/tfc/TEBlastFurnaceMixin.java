package roidrole.tfutils.mixins.tfc;

import net.dries007.tfc.objects.te.TEBlastFurnace;
import net.dries007.tfc.objects.te.TEInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(TEBlastFurnace.class)
public abstract class TEBlastFurnaceMixin extends TEInventory {
	protected TEBlastFurnaceMixin(int inventorySize) {
		super(inventorySize);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	@Nullable
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			//I hate this.
			return (T) new IItemHandlerModifiable() {
				@Override
				public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
					inventory.setStackInSlot(slot, stack);
				}

				@Override
				public int getSlots() {
					return 1;
				}

				@Nonnull
				@Override
				public ItemStack getStackInSlot(int slot) {
					return inventory.getStackInSlot(slot);
				}

				@Nonnull
				@Override
				public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
					if(!isItemValid(slot, stack)){
						return stack;
					}
					return inventory.insertItem(slot, stack, simulate);
				}

				@Nonnull
				@Override
				public ItemStack extractItem(int slot, int amount, boolean simulate) {
					return inventory.extractItem(slot, amount, simulate);
				}

				@Override
				public int getSlotLimit(int slot) {
					return inventory.getSlotLimit(slot);
				}

				@Override
				public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
					return inventory.isItemValid(slot, stack);
				}
			};
		}
		return super.getCapability(capability, facing);
	}

	@Inject(
		method = "isItemValid",
		at = @At("HEAD"),
		remap = false
	)
	public void isItemValidListen(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir){
		System.out.println("isItemValid called for stack:"+ stack.getItem()+":"+stack.getItemDamage());
	}
}
