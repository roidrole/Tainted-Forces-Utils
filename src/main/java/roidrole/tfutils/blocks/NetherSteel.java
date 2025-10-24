package roidrole.tfutils.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class NetherSteel extends Block {
	public static Block BLOCK = new NetherSteel("");
	public static ItemBlock ITEM = new NetherSteelItem("");

	public Item pickItem;
	public NetherSteel(String variant) {
		super(Material.IRON);
		this.setHardness(5.0f);
		this.setResistance(30.0f);
		if(variant.isEmpty()){
			setRegistryName("bewitchment", "nethersteel");
		} else {
			setRegistryName("bewitchment", "nethersteel_"+variant);
		}
		this.setTranslationKey("bewitchment.nethersteel");
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(pickItem, 1, 0);
	}

	public static class NetherSteelItem extends ItemBlock{
		private final String variant;

		public NetherSteelItem(String variant) {
			super(NetherSteel.BLOCK);
			this.variant = variant;
			if(variant.isEmpty()){
				setRegistryName("bewitchment", "nethersteel");
			} else {
				setRegistryName("bewitchment", "nethersteel_"+variant);
			}
			setTranslationKey("bewitchment.nethersteel");
		}

		@Override
		public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
			if(variant.isEmpty()){return;}
			tooltip.add(I18n.format("tooltip.nethersteel."+variant));
		}
	}
}
