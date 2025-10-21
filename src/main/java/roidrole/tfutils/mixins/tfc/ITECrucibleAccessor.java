package roidrole.tfutils.mixins.tfc;

import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.te.TECrucible;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TECrucible.class)
public interface ITECrucibleAccessor {
	@Accessor(remap = false)
	void setAlloyResult(Metal metal);
}
