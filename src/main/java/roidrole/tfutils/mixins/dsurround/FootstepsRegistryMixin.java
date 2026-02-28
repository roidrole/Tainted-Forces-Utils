package roidrole.tfutils.mixins.dsurround;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.orecruncher.dsurround.ModOptions;
import org.orecruncher.dsurround.registry.RegistryManager;
import org.orecruncher.dsurround.registry.acoustics.IAcoustic;
import org.orecruncher.dsurround.registry.acoustics.RainSplashAcoustic;
import org.orecruncher.dsurround.registry.footstep.FootstepsRegistry;
import org.orecruncher.dsurround.registry.footstep.Variator;
import org.orecruncher.lib.MCHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Mixin(FootstepsRegistry.class)
public abstract class FootstepsRegistryMixin {
	@Shadow(remap = false)
	public IAcoustic[] SWIM;

	@Shadow(remap = false)
	public IAcoustic[] JUMP;

	@Shadow(remap = false)
	public IAcoustic[] SPLASH;

	@Shadow(remap = false)
	private Variator defaultVariator;

	@Shadow(remap = false)
	private Variator childVariator;

	@Shadow(remap = false)
	private Variator playerVariator;

	@Shadow(remap = false)
	private Variator playerQuadrupedVariator;

	@Shadow(remap = false)
	@Nonnull
	protected abstract Variator getVariator(@Nonnull String varName);

	@Shadow(remap = false)
	public abstract boolean hasFootprint(@Nonnull IBlockState state);

	@Shadow(remap = false)
	@Final
	private static List<String> FOOTPRINT_SOUND_PROFILE;

	@Shadow(remap = false)
	private Set<IBlockState> FOOTPRINT_STATES;

	/**
	 * @author roidrole
	 * @reason optimize this shit
	 */
	@Overwrite(remap = false)
	protected void postInit() {
		this.SWIM = RegistryManager.ACOUSTICS.compileAcoustics("_SWIM");
		this.JUMP = RegistryManager.ACOUSTICS.compileAcoustics("_JUMP");
		this.SPLASH = new IAcoustic[] {
			new RainSplashAcoustic(RegistryManager.ACOUSTICS.compileAcoustics("waterfine"))
		};

		this.defaultVariator = getVariator("default");
		this.childVariator = getVariator("child");
		this.playerVariator = getVariator(ModOptions.sound.firstPersonFootstepCadence ? "playerSlow" : "player");
		this.playerQuadrupedVariator = getVariator(
			ModOptions.sound.firstPersonFootstepCadence ? "quadrupedSlow" : "quadruped"
		);

		StreamSupport.stream(ForgeRegistries.BLOCKS.spliterator(), false)
			.map(block -> block.getBlockState().getValidStates())
			.flatMap(Collection::stream)
			.filter(bs -> bs.getMaterial().blocksMovement()	&& !hasFootprint(bs))
			.filter(bs -> {
				final SoundType sound = MCHelper.getSoundType(bs);
				if(sound == null){
					return false;
				}
				final SoundEvent event = sound.getStepSound();
				final ResourceLocation resource = event.getSoundName();
				final String soundName = resource.toString();
				return FOOTPRINT_SOUND_PROFILE.contains(soundName);
			})
			.forEach(bs -> this.FOOTPRINT_STATES.add(bs))
		;
	}

	@Redirect(
		method = "complete",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/Set;size()I"
		),
		remap = false
	)
	private static int alwaysPrint(Set<IBlockState> instance){
		return 1;
	}

	@Redirect(
		method = "complete",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/Set;stream()Ljava/util/stream/Stream;"
		),
		remap = false
	)
	private static Stream<IBlockState> newStream(Set<IBlockState> instance){
		return StreamSupport.stream(ForgeRegistries.BLOCKS.spliterator(), false)
			.map(block -> block.getBlockState().getValidStates())
			.flatMap(Collection::stream);
	}
}
