package roidrole.tfutils.mixins.rustichromia;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import rustichromia.handler.WindHandler;

import java.util.HashMap;

@Mixin(value = WindHandler.class, remap = false)
public interface WindHandlerAccessor {
	@Accessor
	static HashMap<Integer, WindHandler> getHandlers(){
		throw new RuntimeException("Mixin was not applied");
	}

	@Invoker
	static int invokeGetDimension(World world){
		throw new RuntimeException("Mixin was not applied");
	}
}
