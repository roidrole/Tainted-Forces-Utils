package roidrole.tfutils.handlers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import roidrole.tfutils.network.PacketAuraToClient;
import roidrole.tfutils.network.PacketHandler;
import thaumcraft.common.world.aura.AuraChunk;
import thaumcraft.common.world.aura.AuraHandler;
import thaumcraft.common.world.aura.AuraWorld;

public class WorldTickHandler {
	@SubscribeEvent
	public static void onWorldTick(TickEvent.PlayerTickEvent event){
		if(event.side != Side.SERVER){
			return;
		}
		if(event.phase != TickEvent.Phase.END){
			return;
		}
		EntityPlayerMP player = (EntityPlayerMP) event.player;
		WorldServer world = (WorldServer) event.player.world;
		if(world.provider.getDimension() != 0){
			return;
		}
		if(world.getWorldTime() % 64 != 1){
			return;
		}
		AuraWorld auraWorld = AuraHandler.getAuraWorld(0);

		//Payload
		int startX = ((int)player.posX >> 4) - 6;
		int startZ = ((int)player.posZ >> 4) - 6;
		short[] base = new short[169];
		float[] vis = new float[169];
		float[] flux = new float[169];

		int index = 0;
		for (int x = startX; x < startX + 13; x++){
			for (int z = startZ; z < startZ + 13; z++){
				AuraChunk auraChunk = auraWorld.getAuraChunkAt(x, z);
				if(auraChunk == null){
					base[index] = -1;
				} else {
					base[index] = auraChunk.getBase();
					vis[index] = auraChunk.getVis();
					flux[index] = auraChunk.getFlux();
				}
				index++;
			}
		}
		//Sending thr packet
		PacketHandler.INSTANCE.sendTo(
			new PacketAuraToClient(startX, startZ, base, vis, flux),
			player
		);
	}
}
