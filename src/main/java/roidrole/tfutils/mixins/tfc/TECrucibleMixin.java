package roidrole.tfutils.mixins.tfc;

import net.dries007.tfc.objects.te.TECrucible;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import roidrole.tfutils.Capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(TECrucible.class)
public class TECrucibleMixin extends TileEntity {
	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if(capability != CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){return super.getCapability(capability, facing);}
		return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new Capabilities.FluidCapabilityCrucible(((TECrucible)(Object)this)));
	}
}
