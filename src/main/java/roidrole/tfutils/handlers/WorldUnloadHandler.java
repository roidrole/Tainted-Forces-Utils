package roidrole.tfutils.handlers;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import roidrole.tfutils.mixins.railcraft.TrainManagerAccessor;
import roidrole.tfutils.mixins.rustichromia.WindHandlerAccessor;

public class WorldUnloadHandler {
	@SubscribeEvent
	public static void onWorldUnload(WorldEvent.Unload event){
		//Fix a memory leak where wind handlers keep a reference to the world after it is unloaded.
		WindHandlerAccessor.getHandlers().remove(WindHandlerAccessor.invokeGetDimension(event.getWorld()));

		//Fix RailCraft's Train Manager leaking world referenced
		TrainManagerAccessor.getInstances().remove(event.getWorld());
	}
}
