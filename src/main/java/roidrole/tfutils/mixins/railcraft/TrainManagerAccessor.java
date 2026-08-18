package roidrole.tfutils.mixins.railcraft;

import mods.railcraft.common.carts.Train;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = Train.Manager.class, remap = false)
public interface TrainManagerAccessor {
	@Accessor
	static Map<World, Train.Manager> getInstances() {
		throw new RuntimeException("Mixin was not applied");
	}
}
