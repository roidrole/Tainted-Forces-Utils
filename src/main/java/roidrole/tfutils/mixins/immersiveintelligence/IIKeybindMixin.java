package roidrole.tfutils.mixins.immersiveintelligence;

import blusunrize.immersiveengineering.client.ClientUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "pl.pabilo8.immersiveintelligence.client.util.IIKeybind$1", remap = false)
public abstract class IIKeybindMixin {
	@Inject(
		method = "isActive()Z",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void checkNullPlayer(CallbackInfoReturnable<Boolean> cir){
		if(ClientUtils.mc().player == null){
			cir.setReturnValue(false);
		}
	}
}
