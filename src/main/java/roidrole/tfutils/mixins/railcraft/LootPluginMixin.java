package roidrole.tfutils.mixins.railcraft;

import mods.railcraft.common.plugins.forge.LootPlugin;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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

	@Redirect(
		method = "<clinit>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/storage/loot/LootTableList;func_186375_a(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/util/ResourceLocation;"
		),
		remap = false
	)
	private static ResourceLocation noRegistering(ResourceLocation resourceLocation){
		return null;
	}
}
