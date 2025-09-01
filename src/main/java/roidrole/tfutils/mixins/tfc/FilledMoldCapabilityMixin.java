package roidrole.tfutils.mixins.tfc;

import net.minecraftforge.fluids.FluidTank;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets="net.dries007.tfc.objects.items.ceramics.ItemMold$FilledMoldCapability")
public class FilledMoldCapabilityMixin {
	@Redirect(
		method = "<init>",
		at = @At(
			value = "NEW",
			target = "net/minecraftforge/fluids/FluidTank"
		),
		remap = false
	)
	public FluidTank biggerTank(int capacity){
		return new FluidTank(144);
	}
}
