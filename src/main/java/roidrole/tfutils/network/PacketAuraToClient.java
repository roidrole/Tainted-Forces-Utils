package roidrole.tfutils.network;

import hellfall.visualores.database.thaumcraft.AuraFluxPosition;
import hellfall.visualores.database.thaumcraft.TCClientCache;
import hellfall.visualores.database.thaumcraft.TCDimensionCache;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import roidrole.tfutils.mixins.visualores.TCClientCacheAccessor;
import roidrole.tfutils.mixins.visualores.TCDimensionCacheAccessor;
import thaumcraft.common.world.aura.AuraChunk;

import java.util.Map;

public class PacketAuraToClient implements IMessage, IMessageHandler<PacketAuraToClient, IMessage> {
	ChunkPos pos;
	short base;
	float vis;
	float flux;

	public PacketAuraToClient() {}

	public PacketAuraToClient(AuraChunk chunk) {
		this.pos = chunk.getLoc();
		this.base = chunk.getBase();
		this.vis = chunk.getVis();
		this.flux = chunk.getFlux();
	}

	public void toBytes(ByteBuf dos) {
		dos.writeInt(pos.x);
		dos.writeInt(pos.z);
		dos.writeShort(this.base);
		dos.writeFloat(this.vis);
		dos.writeFloat(this.flux);
	}

	public void fromBytes(ByteBuf dat) {
		int x = dat.readInt();
		int z = dat.readInt();
		this.pos = new ChunkPos(x, z);
		this.base = dat.readShort();
		this.vis = dat.readFloat();
		this.flux = dat.readFloat();
	}

	public IMessage onMessage(final PacketAuraToClient message, MessageContext ctx) {
		//Scheduling because hashMap access off-thread is unsafe
		Minecraft.getMinecraft().addScheduledTask(() -> {
			Int2ObjectMap<TCDimensionCache> tcCache = ((TCClientCacheAccessor)TCClientCache.instance).getCache();
			TCDimensionCache dimCache = tcCache.computeIfAbsent(0, key -> new TCDimensionCache());
			Map<ChunkPos, AuraFluxPosition> chunkCache = ((TCDimensionCacheAccessor)dimCache).getChunks();

			chunkCache.put(message.pos, new AuraFluxPosition(message.base, message.vis, message.flux, message.pos.x, message.pos.z));
		});
		return null;
	}
}
