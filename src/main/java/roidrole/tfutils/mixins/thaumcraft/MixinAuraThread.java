package roidrole.tfutils.mixins.thaumcraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import thaumcraft.common.world.aura.AuraThread;

@Mixin(AuraThread.class)
public abstract class MixinAuraThread {
	@ModifyVariable(
		method = "processAuraChunk",
		name = "currentFlux",
		at = @At(
			value = "LOAD",
			ordinal = 1
		),
		remap = false
	)
	float linearSpread(float value) {
		return (float) (value - 20.0);
	}

	@ModifyVariable(
		method = "processAuraChunk",
		name = "currentFlux",
		at = @At(
			value = "LOAD",
			ordinal = 2
		),
		remap = false
	)
	float linearSpread2(float value) {
		return (float) (value - 20.0);
	}
}
