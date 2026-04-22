package roidrole.tfutils.mixins.fancymenu;

import de.keksuccino.fancymenu.menu.animation.ResourcePackAnimationRenderer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ResourcePackAnimationRenderer.class)
public abstract class ResourcePackAnimationRendererMixin {
	@Redirect(
		method = "loadAnimationFrames",
		at = @At(
			value = "NEW",
			target = "Lnet/minecraft/util/ResourceLocation;"
		)
	)
	private ResourceLocation tfutils_useBetterResLoc(String namespaceIn, String pathIn){
		return new ResourceLocation("fancymenu", "animation/" + namespaceIn + "/" + pathIn);
	}
}
