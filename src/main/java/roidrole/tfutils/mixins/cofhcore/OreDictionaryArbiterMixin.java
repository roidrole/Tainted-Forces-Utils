package roidrole.tfutils.mixins.cofhcore;

import cofh.core.util.oredict.OreDictionaryArbiter;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Map;

@Mixin(value = OreDictionaryArbiter.class, remap = false)
public abstract class OreDictionaryArbiterMixin {
	@Shadow
	private static Map<Integer, ArrayList<ItemStack>> oreStacks;

	@Inject(
		method = {"<clinit>", "initialize"},
		at = @At(
			value = "TAIL"
		)
	)
	private static void createBetterMap(CallbackInfo ci){
		oreStacks = new Int2ObjectOpenHashMap<>(128);
	}
}
