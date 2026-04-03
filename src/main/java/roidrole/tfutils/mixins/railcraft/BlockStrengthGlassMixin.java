package roidrole.tfutils.mixins.railcraft;

import mods.railcraft.common.blocks.aesthetics.glass.BlockStrengthGlass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlockStrengthGlass.class)
public abstract class BlockStrengthGlassMixin {
	/**
	 * @author roidrole
	 * @reason Original code gathers ItemStack capabilities, which is bad in this pack. Remaking it with CT
	 * Saves 1.3 seconds of load time
	 */
	@Overwrite(remap = false)
	public void defineRecipes(){
		//No-op
	}
}
