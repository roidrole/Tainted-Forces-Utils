package roidrole.tfutils.mixins.bettermineshafts;

import com.yungnickyoung.minecraft.bettermineshafts.init.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.throwables.MixinException;

@Mixin(ModConfig.class)
public interface AccModConfig {
	@Invoker(remap = false)
	static void invokeLoadVariantsJSON(){
		throw new MixinException("Implemented via Mixin");
	}
}
