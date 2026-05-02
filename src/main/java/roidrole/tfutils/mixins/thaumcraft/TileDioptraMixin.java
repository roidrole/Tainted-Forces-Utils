package roidrole.tfutils.mixins.thaumcraft;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagLongArray;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import roidrole.tfutils.mixins.minecraft.NBTTagLongArrayAccessor;
import roidrole.tfutils.network.PacketAuraToClient;
import roidrole.tfutils.network.PacketHandler;
import roidrole.tfutils.utils.TileDioptraAddition;
import thaumcraft.common.tiles.TileThaumcraft;
import thaumcraft.common.tiles.devices.TileDioptra;

import java.util.*;

@Mixin(
	value = TileDioptra.class,
	remap = false
)
public abstract class TileDioptraMixin extends TileThaumcraft implements TileDioptraAddition {
	@Unique
	Collection<UUID> tfutils_playersToSync;

	@Override
	public Collection<UUID> tfutils_getPlayersToSync() {
		return tfutils_playersToSync;
	}

	@Inject(
		method = "<init>",
		at = @At("TAIL")
	)
	private void initPlayerList(CallbackInfo ci){
		this.tfutils_playersToSync = new TreeSet<>();
	}

	@Inject(
		//uodate()
		method = "func_73660_a",
		at = @At("TAIL")
	)
	@SuppressWarnings("all")
	private void sendToPlayers(CallbackInfo ci){
		if(this.world.getWorldTime() % 64 != 1){
			return;
		}
		if(!(this.world instanceof WorldServer)){
			return;
		}
		WorldServer world = (WorldServer) this.world;
		List<EntityPlayerMP> playersToSend = new ArrayList<>(tfutils_playersToSync.size());
		for(UUID uuid: tfutils_playersToSync){
			EntityPlayerMP player = world.getMinecraftServer().getPlayerList().getPlayerByUUID(uuid);
			if(player == null){
				continue;
			}
			playersToSend.add(player);
		}
		if(playersToSend.isEmpty()){
			return;
		}
		PacketAuraToClient packet = new PacketAuraToClient((this.pos.getX() >> 4) - 6, (this.pos.getZ() >> 4) - 6);
		for(EntityPlayerMP player : playersToSend){
			PacketHandler.INSTANCE.sendTo(
				packet,
				player
			);
		}
	}

	@Inject(
		method = "writeSyncNBT",
		at = @At("HEAD")
	)
	public void writePlayerList(NBTTagCompound nbt, CallbackInfoReturnable<NBTTagCompound> cir) {
		long[] uuids = new long[tfutils_playersToSync.size() * 2];
		int index = 0;
		for(UUID uuid : this.tfutils_playersToSync){
			uuids[index++] = uuid.getMostSignificantBits();
			uuids[index++] = uuid.getLeastSignificantBits();
		}
		nbt.setTag("tfutils_playerlist", new NBTTagLongArray(uuids));
	}

	@Inject(
		method = "readSyncNBT",
		at = @At("HEAD")
	)
	public void readPlayerList(NBTTagCompound nbt, CallbackInfo ci) {
		long[] uuids = ((NBTTagLongArrayAccessor) nbt.getTag("tfutils_playerlist")).getData();
		int i = 0;
		while (i < uuids.length) {
			this.tfutils_playersToSync.add(new UUID(uuids[i++], uuids[i++]));
		}
	}

}
