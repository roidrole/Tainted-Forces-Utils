package roidrole.tfutils.mixins.railcraft;

import mods.railcraft.common.core.Railcraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Railcraft.class)
public class RailCraftMixin {
	/**
	 * @author roidrole
	 * @reason remove that fingerprintviolation crash
	 */
	@Overwrite(remap = false)
	public void fingerprintError(net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent event) {
		//NO-OP
	}
}
