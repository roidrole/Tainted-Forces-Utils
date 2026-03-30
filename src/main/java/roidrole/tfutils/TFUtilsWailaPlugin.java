package roidrole.tfutils;

import mcp.mobius.waila.api.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.common.blocks.essentia.BlockTube;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.tiles.essentia.TileTube;

import javax.annotation.Nonnull;
import java.util.List;

@WailaPlugin
public class TFUtilsWailaPlugin implements IWailaPlugin {
	@Override
	public void register(IWailaRegistrar registrar) {
		registrar.registerBodyProvider(BlockTubeProvider.INSTANCE, BlockTube.class);
		registrar.registerNBTProvider(BlockTubeProvider.INSTANCE, BlockTube.class);
	}

	public static class BlockTubeProvider implements IWailaDataProvider {
		public static BlockTubeProvider INSTANCE = new BlockTubeProvider();
		@Nonnull
		@Override
		public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
			if (!(te instanceof TileTube)) {
				return tag;
			}
			if(!EntityUtils.hasGoggles(player)){
				return tag;
			}
			if(((TileTube) te).getEssentiaType(null) != null){
				NBTTagCompound essentia = new NBTTagCompound();
				essentia.setString("tag", ((TileTube) te).getEssentiaType(null).getTag());
				essentia.setInteger("amount", ((TileTube) te).getEssentiaAmount(null));
				tag.setTag("essentia", essentia);
			}
			NBTTagCompound suction = new NBTTagCompound();
			if(((TileTube) te).getSuctionType(null) != null){
				suction.setString("tag", ((TileTube) te).getSuctionType(null).getTag());
			}
			suction.setInteger("amount", ((TileTube) te).getSuctionAmount(null));
			tag.setTag("suction", suction);
			return tag;
		}

		@Nonnull
		@Override
		public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
			NBTTagCompound data = accessor.getNBTData();
			if(data.isEmpty() || !data.hasKey("suction")){
				return tooltip;
			}

			NBTTagCompound essentia = data.getCompoundTag("essentia");
			String essentiaTag = essentia.getString("tag");
			if(!essentiaTag.isEmpty()){
				tooltip.add(
					String.format(
						"Contains %s %s%s",
						essentia.getInteger("amount"),
						SpecialChars.WailaSplitter,
						SpecialChars.getRenderString("thaumicwaila.aspect", essentiaTag)
					)
				);
			}

			NBTTagCompound suction = data.getCompoundTag("suction");
			String suctionTag = suction.getString("tag");
			if(!suctionTag.isEmpty()){
				tooltip.add(
					String.format(
						"Suction %s %s%s",
						suction.getInteger("amount"),
						SpecialChars.WailaSplitter,
						SpecialChars.getRenderString("thaumicwaila.aspect", suctionTag)
					)
				);
			} else {
				tooltip.add(
					"Suction " + suction.getInteger("amount") + " Untyped"
				);
			}

			return tooltip;
		}
	}
}
