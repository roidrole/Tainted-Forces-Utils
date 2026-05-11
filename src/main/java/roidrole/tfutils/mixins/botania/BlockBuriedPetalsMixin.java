package roidrole.tfutils.mixins.botania;

import org.spongepowered.asm.mixin.Mixin;
import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.block.decor.BlockBuriedPetals;

import java.util.Random;

@Mixin(BlockBuriedPetals.class)
public abstract class BlockBuriedPetalsMixin extends BlockModFlower {

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}
}
