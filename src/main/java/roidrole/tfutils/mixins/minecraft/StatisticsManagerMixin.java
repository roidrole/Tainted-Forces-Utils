package roidrole.tfutils.mixins.minecraft;

import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatisticsManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatisticsManager.class)
public abstract class StatisticsManagerMixin {
	@Inject(
		method = "readStat",
		at = @At("HEAD"),
		cancellable = true
	)
	private void stopNPE(StatBase stat, CallbackInfoReturnable<Integer> cir){
		if(stat == null){
			cir.setReturnValue(0);
		}
	}
}
