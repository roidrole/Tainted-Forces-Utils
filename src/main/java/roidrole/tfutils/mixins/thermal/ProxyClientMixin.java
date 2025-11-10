package roidrole.tfutils.mixins.thermal;

import codechicken.lib.render.item.IItemRenderer;
import cofh.thermaldynamics.proxy.ProxyClient;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ProxyClient.class)
public abstract class ProxyClientMixin {
	@Redirect(
		method = "preInit",
		at = @At(
			value = "INVOKE",
			target = "Lcodechicken/lib/model/ModelRegistryHelper;registerItemRenderer(Lnet/minecraft/item/Item;Lcodechicken/lib/render/item/IItemRenderer;)V"
		),
		remap = false
	)
	void noCovers(Item item, IItemRenderer renderer){
		//NO-OP
	}
}
