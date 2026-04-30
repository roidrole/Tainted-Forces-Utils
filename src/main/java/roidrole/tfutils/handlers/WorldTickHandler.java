package roidrole.tfutils.handlers;

import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import roidrole.tfutils.network.PacketAuraToClient;
import roidrole.tfutils.network.PacketHandler;
import thaumcraft.common.world.aura.AuraChunk;
import thaumcraft.common.world.aura.AuraHandler;
import thaumcraft.common.world.aura.AuraWorld;

public class WorldTickHandler {
	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event){
		if(!(event.world instanceof WorldServer)){
			return;
		}
		WorldServer world = (WorldServer) event.world;
		if(world.provider.getDimension() != 0){
			return;
		}
		if(world.getWorldTime() % 8192 != 1){
			return;
		}
		AuraWorld auraWorld = AuraHandler.getAuraWorld(0);
		for(Chunk chunk : world.getChunkProvider().getLoadedChunks()){
			AuraChunk auraChunk = auraWorld.getAuraChunkAt(chunk.x, chunk.z);
			if (auraChunk == null) {
				continue;
			}
			PacketHandler.INSTANCE.sendToAllAround(
				new PacketAuraToClient(chunk.x, chunk.z, auraChunk),
				new NetworkRegistry.TargetPoint(0, chunk.x << 4, world.getSeaLevel(), chunk.z << 4, 192)
			);
		}
	}
}
