package roidrole.tfutils.mixins.botania;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.block.decor.BlockShinyFlower;

import java.util.Random;

@Mixin(BlockShinyFlower.class)
public abstract class BlockShinyFlowerMixin extends BlockModFlower {
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}
}
