package roidrole.tfutils.mixins.thaumicwaila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import me.xfl03.thaumicwaila.provider.AspectContainerProvider;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.common.lib.utils.EntityUtils;

import java.util.List;

@Mixin(AspectContainerProvider.class)
public abstract class AspectContainerProviderMixin {
	@Inject(
		method = "getWailaBody",
		at = @At(
			value = "HEAD"
		),
		remap = false,
		cancellable = true
	)
	private static void onlyIfGoggles(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config, CallbackInfoReturnable<List<String>> cir){
		if(!EntityUtils.hasGoggles(accessor.getPlayer())){
			cir.setReturnValue(tooltip);
		}
	}
}
