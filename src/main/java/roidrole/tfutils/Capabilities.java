package roidrole.tfutils;

import mekanism.common.MekanismFluids;
import net.dries007.tfc.ConfigTFC;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.dries007.tfc.objects.te.TECrucible;
import net.dries007.tfc.util.Alloy;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.*;
import net.minecraftforge.oredict.OreDictionary;
import roidrole.tfutils.mixins.tfc.ITECrucibleAccessor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Capabilities{
	public static class FluidCapabilityRod implements IFluidHandlerItem {
		static Fluid output = MekanismFluids.Tritium.getFluid();
		static ItemStack container = OreDictionary.getOres("nuggetZircaloy").get(0);
		int count;

		public FluidCapabilityRod(int count){
			this.count = count;
		}
		@Nonnull
		@Override
		public ItemStack getContainer() {
			ItemStack out = container.copy();
			out.setCount(9 - Math.floorDiv(count, 1111));
			return out;
		}

		@Override
		public IFluidTankProperties[] getTankProperties() {
			return new FluidTankProperties[]{
				new FluidTankProperties(new FluidStack(output, count), 10000, false, true)
			};
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			return 0;
		}

		@Nullable
		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain) {
			if(resource.getFluid() != output){return null;}
			return new FluidStack(output, count);
		}

		@Nullable
		@Override
		public FluidStack drain(int maxDrain, boolean doDrain) {
			return new FluidStack(output, Math.min(maxDrain, count));
		}
	}

	public static class FluidCapabilityRodProvider implements ICapabilityProvider {
		private final ItemStack stack;

		public FluidCapabilityRodProvider(ItemStack stack){
			this.stack = stack;
		}
		@Override
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
			return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
		}

		@Nullable
		@Override
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
			if(capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY){
				NBTTagCompound tag = (stack).getTagCompound();
				if(tag != null && tag.hasKey("advDmg")){
					return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(new Capabilities.FluidCapabilityRod(tag.getInteger("advDmg")));
				}
			}
			return null;
		}
	}

	public static class FluidCapabilityCrucible implements IFluidHandler {
		TECrucible internal;
		public FluidCapabilityCrucible(TECrucible internal){
			this.internal = internal;
		}

		@Override
		public IFluidTankProperties[] getTankProperties() {
			Alloy alloy = internal.getAlloy();
			int amount = alloy.getAmount();
			return new IFluidTankProperties[]{new FluidTankProperties(
				new FluidStack(FluidsTFC.getFluidFromMetal(alloy.getResult()), amount),
				ConfigTFC.Devices.CRUCIBLE.tank,
				false,
				true
			)};
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			return 0;
		}

		@Nullable
		@Override
		public FluidStack drain(FluidStack stack, boolean doDrain) {
			Alloy alloy = internal.getAlloy();
			if(stack.getFluid() != FluidsTFC.getFluidFromMetal(alloy.getResult())){
				return null;
			}
			return drain(stack.amount, doDrain);
		}

		@Nullable
		@Override
		public FluidStack drain(int maxDrain, boolean doDrain) {
			Alloy alloy = internal.getAlloy();
			if(alloy.getAmount() == 0){
				return null;
			}
			Metal metal = alloy.getResult();
			int amount = Math.min(alloy.getAmount(), maxDrain);
			if(doDrain){
				alloy.removeAlloy(amount, false);
				internal.markForSync();
				((ITECrucibleAccessor) internal).setAlloyResult(alloy.getResult());
			}
			return new FluidStack(FluidsTFC.getFluidFromMetal(metal), amount);
		}
	}
}
