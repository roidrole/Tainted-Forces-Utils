package roidrole.tfutils.mixins.tfc;

import com.llamalad7.mixinextras.sugar.Local;
import net.dries007.tfc.world.classic.ChunkGenTFC;
import net.dries007.tfc.world.classic.CustomChunkPrimer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
	public MapGenMineshaft tfUtils_mineshaftGen;

	@Inject(
		method = "<init>",
		at = @At("TAIL"),
		remap = false
	)
	private void addMineshaftGenerator(World w, String settingsString, CallbackInfo ci){
		tfUtils_mineshaftGen = (MapGenMineshaft) TerrainGen.getModdedMapGen(new MapGenMineshaft(), InitMapGenEvent.EventType.MINESHAFT);
	}

	@Inject(
		method = "func_185932_a",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/gen/MapGenBase;func_186125_a(Lnet/minecraft/world/World;IILnet/minecraft/world/chunk/ChunkPrimer;)V",
			ordinal = 0
		),
		remap = false
	)
	private void generateMineshaft(int chunkX, int chunkZ, CallbackInfoReturnable<Chunk> cir, @Local(name = "chunkPrimerOut") CustomChunkPrimer chunkPrimerOut){
		this.tfUtils_mineshaftGen.generate(this.world, chunkX, chunkZ, chunkPrimerOut);
	}

	@Inject(
		method = "func_185931_b",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraftforge/fml/common/IWorldGenerator;generate(Ljava/util/Random;IILnet/minecraft/world/World;Lnet/minecraft/world/gen/IChunkGenerator;Lnet/minecraft/world/chunk/IChunkProvider;)V",
			ordinal = 0
		),
		remap = false
	)
	private void populateMineshaft(int chunkX, int chunkZ, CallbackInfo ci){
		this.tfUtils_mineshaftGen.generateStructure(this.world, this.rand, new ChunkPos(chunkX, chunkZ));
	}
}
