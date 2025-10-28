package roidrole.tfutils.mixins.tfc;

import net.dries007.tfc.objects.te.TECrucible;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import roidrole.tfutils.Capabilities;

@Mixin(TECrucible.class)
public class TECrucibleMixin {
	@Inject(
		method = "hasCapability",
		at = @At("HEAD"),
		cancellable = true,
		remap = false
	)
	public void hasFluidCapability(Capability<?> capability, EnumFacing facing, CallbackInfoReturnable<Boolean> cir) {
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
			cir.setReturnValue(true);
		}
	}

	@Inject(
		method = "getCapability",
		at = @At("HEAD"),
		cancellable = true,
		remap = false
	)
	public <T> void getFluidCapability(Capability<T> capability, EnumFacing facing, CallbackInfoReturnable<T> cir) {
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
			cir.setReturnValue(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new Capabilities.FluidCapabilityCrucible(((TECrucible)(Object)this))));
		}
	}

	//Confirmed to work in prod
	@ModifyArg(
		method = "func_73660_a",
		at = @At(
			value = "INVOKE",
			target = "Lnet/dries007/tfc/util/Alloy;removeAlloy(IZ)I"
		),
		index = 0,
		remap = false
	)
	public int increaseFillRate(int removeAmount){
		return 3;
	}
}
