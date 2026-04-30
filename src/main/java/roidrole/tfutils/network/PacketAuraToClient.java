package roidrole.tfutils.network;

import hellfall.visualores.database.thaumcraft.AuraFluxPosition;
import hellfall.visualores.database.thaumcraft.TCClientCache;
import hellfall.visualores.database.thaumcraft.TCDimensionCache;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import roidrole.tfutils.mixins.visualores.TCClientCacheAccessor;
import roidrole.tfutils.mixins.visualores.TCDimensionCacheAccessor;
import thaumcraft.common.world.aura.AuraChunk;

import java.util.Map;

public class PacketAuraToClient implements IMessage, IMessageHandler<PacketAuraToClient, IMessage> {
	int chunkX;
	int chunkZ;

	short base;
	float vis;
	float flux;

	public PacketAuraToClient() {}

	public PacketAuraToClient(int x, int y, AuraChunk chunk) {
		this.chunkX = x;
		this.chunkZ = y;
		this.base = chunk.getBase();
		this.vis = chunk.getVis();
		this.flux = chunk.getFlux();
	}

	public void toBytes(ByteBuf dos) {
		dos.writeInt(chunkX);
		dos.writeInt(chunkZ);
		dos.writeShort(this.base);
		dos.writeFloat(this.vis);
		dos.writeFloat(this.flux);
	}

	public void fromBytes(ByteBuf dat) {
	   this.chunkX = dat.readInt();
	   this.chunkZ = dat.readInt();
	   this.base = dat.readShort();
	   this.vis = dat.readFloat();
	   this.flux = dat.readFloat();
	}

	public IMessage onMessage(final PacketAuraToClient message, MessageContext ctx) {
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		Int2ObjectMap<TCDimensionCache> tcCache = ((TCClientCacheAccessor)TCClientCache.instance).getCache();
		if(!tcCache.containsKey(player.dimension)){
			tcCache.put(player.dimension, new TCDimensionCache());
		}
		TCDimensionCache dimCache = tcCache.get(player.dimension);
		Map<ChunkPos, AuraFluxPosition> chunkCache = ((TCDimensionCacheAccessor)dimCache).getChunks();

		chunkCache.put(new ChunkPos(this.chunkX, this.chunkZ), new AuraFluxPosition(this.base, this.vis, this.flux, this.chunkX, this.chunkZ));
		return null;
	}
}
