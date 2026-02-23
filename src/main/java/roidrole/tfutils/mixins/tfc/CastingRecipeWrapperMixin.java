package roidrole.tfutils.mixins.tfc;

import net.dries007.tfc.compat.jei.wrappers.CastingRecipeWrapper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CastingRecipeWrapper.class)
public abstract class CastingRecipeWrapperMixin {
	@Redirect(
		method = "<init>",
		at = @At(
			value = "NEW",
			target = "(Lnet/minecraftforge/fluids/Fluid;I)Lnet/minecraftforge/fluids/FluidStack;"
		),
		remap = false
	)
	public FluidStack biggerTank(Fluid fluid, int amount){
		return new FluidStack(fluid, 144);
	}
}
