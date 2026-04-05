package roidrole.tfutils.mixins.botania;

import net.dries007.tfc.api.types.Rock;
import net.dries007.tfc.objects.blocks.stone.BlockRockVariant;
import net.dries007.tfc.world.classic.chunkdata.ChunkDataTFC;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.common.block.subtile.functional.SubTileMarimorphosis;

@Mixin(SubTileMarimorphosis.class)
public abstract class SubTileMarimorphosisMixin extends SubTileEntity {
	/**
	 * @author roidrole
	 * @reason TFC instead of vanilla
	 */
	@Overwrite(remap = false)
	public IBlockState getStoneToPut(BlockPos coords){
		return BlockRockVariant.get(
			ChunkDataTFC.getRock1(this.getWorld(), coords),
			Rock.Type.RAW
		).getDefaultState();
	}
}
