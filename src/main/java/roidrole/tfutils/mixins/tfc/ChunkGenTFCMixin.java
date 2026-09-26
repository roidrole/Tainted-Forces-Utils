package roidrole.tfutils.mixins.tfc;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.dries007.tfc.world.classic.ChunkGenTFC;
import net.dries007.tfc.world.classic.CustomChunkPrimer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(ChunkGenTFC.class)
public abstract class ChunkGenTFCMixin {
	@Shadow(remap = false)
	@Final
	private World world;
	@Shadow(remap = false)
	@Final
	private Random rand;
	@Unique
	public MapGenStructure tfUtils_mineshaftGen;

	@Unique
	private MapGenBase tfutils_newCaveGen;

	@Inject(
		method = "<init>",
		at = @At("TAIL"),
		remap = false
	)
	private void addMineshaftGenerator(World w, String settingsString, CallbackInfo ci){
		tfUtils_mineshaftGen = (MapGenStructure) TerrainGen.getModdedMapGen(new MapGenMineshaft(), InitMapGenEvent.EventType.MINESHAFT);
	}

	@Inject(
		method = "generateChunk",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/gen/MapGenBase;generate(Lnet/minecraft/world/World;IILnet/minecraft/world/chunk/ChunkPrimer;)V",
			ordinal = 0
		)
	)
	private void generateMineshaft(int chunkX, int chunkZ, CallbackInfoReturnable<Chunk> cir, @Local(name = "chunkPrimerOut") CustomChunkPrimer chunkPrimerOut){
		try {
			this.tfUtils_mineshaftGen.generate(this.world, chunkX, chunkZ, chunkPrimerOut);
		} catch (RuntimeException ignored) { }
	}

	@Inject(
		method = "populate",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraftforge/fml/common/IWorldGenerator;generate(Ljava/util/Random;IILnet/minecraft/world/World;Lnet/minecraft/world/gen/IChunkGenerator;Lnet/minecraft/world/chunk/IChunkProvider;)V",
			ordinal = 0
		),
		remap = false
	)
	private void populateMineshaft(int chunkX, int chunkZ, CallbackInfo ci) {
		try {
			this.tfUtils_mineshaftGen.generateStructure(this.world, this.rand, new ChunkPos(chunkX, chunkZ));
		} catch (RuntimeException ignored) { }
	}

	@WrapOperation(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraftforge/event/terraingen/TerrainGen;getModdedMapGen(Lnet/minecraft/world/gen/MapGenBase;Lnet/minecraftforge/event/terraingen/InitMapGenEvent$EventType;)Lnet/minecraft/world/gen/MapGenBase;"
		),
		remap = false
	)
	private MapGenBase storeMapGen(MapGenBase original, InitMapGenEvent.EventType type, Operation<MapGenBase> originalOperation){
		this.tfutils_newCaveGen = originalOperation.call(original, type);
		return original;
	}

	@ModifyReceiver(
		method = "generateChunk",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/gen/MapGenBase;generate(Lnet/minecraft/world/World;IILnet/minecraft/world/chunk/ChunkPrimer;)V",
			ordinal = 0
		)
	)
	private MapGenBase generateWithNew(MapGenBase instance, World worldIn, int x, int z, ChunkPrimer primer){
		return tfutils_newCaveGen;
	}
}
