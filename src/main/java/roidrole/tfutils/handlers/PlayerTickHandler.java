package roidrole.tfutils.handlers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import roidrole.tfutils.network.PacketAuraToClient;
import roidrole.tfutils.network.PacketHandler;

public class PlayerTickHandler {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event){
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

		PacketHandler.INSTANCE.sendTo(
			new PacketAuraToClient(((int)player.posX >> 4) - 6, ((int)player.posZ >> 4) - 6),
			player
		);
	}
}
