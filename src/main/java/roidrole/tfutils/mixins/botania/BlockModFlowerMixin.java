package roidrole.tfutils.mixins.botania;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IShearable;
import org.spongepowered.asm.mixin.Mixin;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Mixin(BlockModFlower.class)
public abstract class BlockModFlowerMixin extends BlockFlower implements IShearable {

	@Override
	public int quantityDropped(Random random) {
		return 2;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)	{
		return ModItems.petal;
	}


	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, @Nonnull BlockPos pos) {
		return true;
	}

	@Nonnull
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, @Nonnull BlockPos pos, int fortune) {
		IBlockState state = world.getBlockState(pos);
		return Collections.singletonList(new ItemStack(this, 1, state.getValue(BotaniaStateProps.COLOR).getMetadata()));
	}
}
