package roidrole.tfutils.mixins.ic2;

import ic2.core.util.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.BufferedWriter;

@Mixin(Config.class)
public abstract class ConfigMixin {
	@Redirect(
		method = "save(Ljava/io/OutputStream;)V",
		at = @At(
			value = "INVOKE",
			target = "Ljava/io/BufferedWriter;write(Ljava/lang/String;)V",
			ordinal = 2
		),
		remap = false
	)
	private static void sameConfigFile2(BufferedWriter instance, String s){
		//NO-OP
	}
	@Redirect(
		method = "save(Ljava/io/OutputStream;)V",
		at = @At(
			value = "INVOKE",
			target = "Ljava/io/BufferedWriter;write(Ljava/lang/String;)V",
			ordinal = 3
		),
		remap = false
	)
	private static void sameConfigFile3(BufferedWriter instance, String s){
		//NO-OP
	}
}
