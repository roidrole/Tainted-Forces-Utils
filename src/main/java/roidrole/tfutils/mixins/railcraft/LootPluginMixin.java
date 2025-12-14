package roidrole.tfutils.mixins.railcraft;

import mods.railcraft.common.plugins.forge.LootPlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LootPlugin.class)
public abstract class LootPluginMixin {
	/**
	 * @author roidrole
	 * @reason Removed RC loot addition so it doesn't spam the log with unknown item errors
	 */
	@Overwrite(remap = false)
	public void init(){
		//No-OP
	}
}
