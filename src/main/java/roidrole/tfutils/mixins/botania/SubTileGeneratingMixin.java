package roidrole.tfutils.mixins.botania;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import roidrole.tfutils.TFUtilsConfig;
import vazkii.botania.api.subtile.SubTileGenerating;

@Mixin(SubTileGenerating.class)
public abstract class SubTileGeneratingMixin {
	/**
	 * @author roidrole
	 * @reason buff hudrogeanas production
	 */
	@Overwrite(remap = false)
	public int getValueForPassiveGeneration() {
		return TFUtilsConfig.hydrogenanasPowerMultiplier;
	}
}
