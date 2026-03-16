package roidrole.tfutils.mixins.thaumicspeedup;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import roidrole.tfutils.config.TFUtilsConfig;
import thaumcraft.common.config.ConfigAspects;
import thaumcraft.proxies.CommonProxy;

//Would have mixined into Thaumic Speedup's mixin, but mixinsquared is a pain
@Mixin(CommonProxy.class)
public abstract class CommonProxyMixin {
	@Redirect(
		method = "postInit",
		at = @At(
			value = "INVOKE",
			target = "Lthaumcraft/common/config/ConfigAspects;postInit()V"
		),
		remap = false
	)
	private static void onlyIfRegenerating(){
		if(TFUtilsConfig.regenAspectCache){
			ConfigAspects.postInit();
		}
	}
}
