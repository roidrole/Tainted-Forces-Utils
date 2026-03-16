package roidrole.tfutils.mixins.buildinggadgets;

import com.direwolf20.buildinggadgets.common.blocks.ConstructionBlockTileEntity;
import com.direwolf20.buildinggadgets.common.blocks.EffectBlock;
import com.direwolf20.buildinggadgets.common.config.SyncedConfig;
import com.direwolf20.buildinggadgets.common.entities.BlockBuildEntity;
import com.direwolf20.buildinggadgets.common.items.ModItems;
import com.direwolf20.buildinggadgets.common.items.gadgets.GadgetCopyPaste;
import com.direwolf20.buildinggadgets.common.items.gadgets.GadgetGeneric;
import com.direwolf20.buildinggadgets.common.network.PacketBlockMap;
import com.direwolf20.buildinggadgets.common.network.PacketHandler;
import com.direwolf20.buildinggadgets.common.tools.*;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import roidrole.tfutils.TFUtils;
import roidrole.tfutils.config.BuildingGadgetIgnoredNBTKeys;
import roidrole.tfutils.utils.MutablerBlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TODO: Mirror, rotate support
@Mixin(GadgetCopyPaste.class)
public abstract class GadgetCopyPasteMixin extends GadgetGeneric {
	public GadgetCopyPasteMixin(String name) {
		super(name);
	}

	@Shadow(remap = false)
	private static void setLastBuild(ItemStack stack, BlockPos anchorPos, Integer dim) { }

	@Shadow(remap = false)
	@Nonnull
	@SuppressWarnings("all")
	private static List<BlockMap> getBlockMapList(@Nullable NBTTagCompound tagCompound, BlockPos pos)  {
		return null;
	}

	@Shadow(remap = false)
	@Nonnull
	@SuppressWarnings("all")
	public static BlockMapIntState getBlockMapIntState(@Nullable NBTTagCompound tagCompound) {
		return null;
	}

	/**
	 * @author roidrole
	 * @reason TE support
	 */
	@Overwrite(remap = false)
	private static boolean findBlocks(World world, BlockPos start, BlockPos end, ItemStack stack, EntityPlayer player, GadgetCopyPaste tool) {
		setLastBuild(stack, null, 0);

		//Change - removed foundTE
		int startX = start.getX();
		int startY = start.getY();
		int startZ = start.getZ();

		int endX = end.getX();
		int endY = end.getY();
		int endZ = end.getZ();

		if (Math.abs(startX - endX) >= 125 || Math.abs(startY - endY) >= 125 || Math.abs(startZ - endZ) >= 125) {
			player.sendStatusMessage(new TextComponentString(TextFormatting.RED + new TextComponentTranslation("message.gadget.toobigarea").getUnformattedComponentText()), true);
			return false;
		}

		int iStartX = Math.min(startX, endX);
		int iStartY = Math.min(startY, endY);
		int iStartZ = Math.min(startZ, endZ);
		int iEndX = Math.max(startX, endX);
		int iEndY = Math.max(startY, endY);
		int iEndZ = Math.max(startZ, endZ);
		WorldSave worldSave = WorldSave.getWorldSave(world);
		NBTTagCompound tagCompound = new NBTTagCompound();
		List<Integer> posIntArrayList = new ArrayList<>();
		List<Integer> stateIntArrayList = new ArrayList<>();
		BlockMapIntState blockMapIntState = new BlockMapIntState();
		Multiset<UniqueItem> itemCountMap = HashMultiset.create();
		//Addition
		NBTTagCompound tileDataMap = new NBTTagCompound();
		NBTTagCompound extraCosts = new NBTTagCompound();

		int blockCount = 0;

		//Modification: use MutablerBlockPos
		MutablerBlockPos tempPos = new MutablerBlockPos();
		for (int x = iStartX; x <= iEndX; x++) {
			tempPos.setX(x);
			for (int y = iStartY; y <= iEndY; y++) {
				tempPos.setY(y);
				for (int z = iStartZ; z <= iEndZ; z++) {
					tempPos.setZ(z);
					IBlockState tempState = world.getBlockState(tempPos);
					if (!(tempState.getBlock() instanceof EffectBlock)
					 && tempState != Blocks.AIR.getDefaultState()
					 && !tempState.getMaterial().isLiquid()
					 && !SyncedConfig.blockBlacklist.contains(tempState.getBlock())
					) {
						@Nullable TileEntity te = world.getTileEntity(tempPos);
						IBlockState assignState = InventoryManipulation.getSpecificStates(tempState, world, player, tempPos, stack);
						IBlockState actualState = assignState.getActualState(world, tempPos);
						//Change - remove the if(te != null) check
						if (te instanceof ConstructionBlockTileEntity) {
							actualState = ((ConstructionBlockTileEntity) te).getActualBlockState();
						}
						if (actualState != null) {
							UniqueItem uniqueItem = BlockMapIntState.blockStateToUniqueItem(actualState, player, tempPos);
							TFUtils.LOGGER.info("item for pos {} is {}:{}", tempPos, uniqueItem.item, uniqueItem.meta);
							if (uniqueItem.item != Items.AIR) {
								//Addition: use a value
								int relPos = GadgetUtils.relPosToInt(start, tempPos);
								posIntArrayList.add(relPos);
								blockMapIntState.addToMap(actualState);
								stateIntArrayList.add((int) blockMapIntState.findSlot(actualState));

								blockMapIntState.addToStackMap(uniqueItem, actualState);
								blockCount++;
								if (blockCount > 32768) {
									player.sendStatusMessage(new TextComponentString(TextFormatting.RED + new TextComponentTranslation("message.gadget.toomanyblocks").getUnformattedComponentText()), true);
									return false;
								}
								NonNullList<ItemStack> drops = NonNullList.create();
								actualState.getBlock().getDrops(drops, world, new BlockPos(0, 0, 0), actualState, 0);

								int neededItems = 0;
								for (ItemStack drop : drops) {
									if (drop.getItem().equals(uniqueItem.item)) {
										neededItems++;
									}
								}
								if (neededItems == 0) {
									neededItems = 1;
								}
								itemCountMap.add(uniqueItem, neededItems);
								//Addition - add to tileData map
								if(te != null){
									NBTTagCompound data = new NBTTagCompound();
									NBTTagList costs = new NBTTagList();
									te.writeToNBT(data);
									BuildingGadgetIgnoredNBTKeys.INSTANCE.filter(data, costs);
									data.setInteger("x", tempPos.getX());
									data.setInteger("y", tempPos.getY());
									data.setInteger("z", tempPos.getZ());

									tileDataMap.setTag(Integer.toHexString(relPos), data);
									extraCosts.setTag(Integer.toHexString(relPos), costs);
								}
							}
						}
					}
				}
			}
		}
		tool.setItemCountMap(stack, itemCountMap);
		tagCompound.setTag("mapIntState", blockMapIntState.putIntStateMapIntoNBT());
		tagCompound.setTag("mapIntStack", blockMapIntState.putIntStackMapIntoNBT());
		int[] posIntArray = posIntArrayList.stream().mapToInt(i -> i).toArray();
		int[] stateIntArray = stateIntArrayList.stream().mapToInt(i -> i).toArray();
		tagCompound.setIntArray("posIntArray", posIntArray);
		tagCompound.setIntArray("stateIntArray", stateIntArray);
		//Addition
		tagCompound.setTag("tfutils.tileData", tileDataMap);
		tagCompound.setTag("tfutils.extraCosts", extraCosts);

		tagCompound.setTag("startPos", NBTUtil.createPosTag(start));
		tagCompound.setTag("endPos", NBTUtil.createPosTag(end));
		tagCompound.setInteger("dim", player.dimension);
		tagCompound.setString("UUID", tool.getUUID(stack));
		tagCompound.setString("owner", player.getName());
		tool.incrementCopyCounter(stack);
		tagCompound.setInteger("copycounter", tool.getCopyCounter(stack));


		worldSave.addToMap(tool.getUUID(stack), tagCompound);
		worldSave.markForSaving();
		PacketHandler.INSTANCE.sendTo(new PacketBlockMap(tagCompound), (EntityPlayerMP) player);

		//Change - removed TEm message and tracking
		player.sendStatusMessage(new TextComponentString(TextFormatting.AQUA + new TextComponentTranslation("message.gadget.copied").getUnformattedComponentText()), true);

		return true;
	}

	//Redirecting so I can add that tagCompound argument
	@Redirect(
		method = "buildBlockMap",
		at = @At(
			value = "INVOKE",
			//World.spawnEntity
			target = "Lcom/direwolf20/buildinggadgets/common/items/gadgets/GadgetCopyPaste;placeBlock(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/block/state/IBlockState;Ljava/util/Map;)V"
		),
		remap = false
	)
	private void useTileData(
		GadgetCopyPaste instance,
		World world,
		BlockPos pos,
		EntityPlayer player,
		IBlockState state,
		Map<IBlockState, UniqueItem> IntStackMap,
		@Local(name = "tagCompound") NBTTagCompound tagCompound,
		@Local(name = "pos") BlockPos center
	){
		tfutils_placeBlock(
			world,
			pos,
			player,
			state,
			IntStackMap,
			tagCompound
				.getCompoundTag("tfutils.tileData")
				.getCompoundTag(
					Integer.toHexString(GadgetUtils.relPosToInt(center, pos))
				),
			tagCompound
				.getCompoundTag("tfutils.extraCosts")
				.getTagList(
					Integer.toHexString(GadgetUtils.relPosToInt(center, pos)),
					10
				)
				.tagList
		);
	}


	@Unique
	private void tfutils_placeBlock(
		World world,
		BlockPos pos,
		EntityPlayer player,
		IBlockState state,
		Map<IBlockState, UniqueItem> IntStackMap,
		//Additions
		NBTTagCompound data,
		List<NBTBase> cost
	) {
		if( world.isOutsideBuildHeight(pos) )
			return;

		IBlockState testState = world.getBlockState(pos);
		if ((SyncedConfig.canOverwriteBlocks && !testState.getBlock().isReplaceable(world, pos)) ||
			(!SyncedConfig.canOverwriteBlocks && testState.getBlock().isAir(testState, world, pos)))
			return;

		if (pos.getY() < 0 || state.equals(Blocks.AIR.getDefaultState()) || !player.isAllowEdit())
			return;

		ItemStack heldItem = getGadget(player);
		if (heldItem.isEmpty())
			return;

		if (ModItems.gadgetCopyPaste.getStartPos(heldItem) == null || ModItems.gadgetCopyPaste.getEndPos(heldItem) == null)
			return;

		UniqueItem uniqueItem = IntStackMap.get(state);
		if (uniqueItem == null) return; //This shouldn't happen I hope!
		ItemStack itemStack = new ItemStack(uniqueItem.item, 1, uniqueItem.meta);
		NonNullList<ItemStack> drops = NonNullList.create();
		state.getBlock().getDrops(drops, world, pos, state, 0);
		int neededItems = 0;
		for (ItemStack drop : drops) {
			if (drop.getItem().equals(itemStack.getItem())) {
				neededItems++;
			}
		}
		if (neededItems == 0) {
			neededItems = 1;
		}
		if (!world.isBlockModifiable(player, pos)) {
			return;
		}

		BlockSnapshot blockSnapshot = BlockSnapshot.getBlockSnapshot(world, pos);
		//Replace the original protected method call
		if(ForgeEventFactory.onPlayerBlockPlace(
			player,
			blockSnapshot,
			EnumFacing.UP,
			EnumHand.MAIN_HAND
		).isCanceled()){
			return;
		}

		ItemStack constructionPaste = new ItemStack(ModItems.constructionPaste);
		boolean useConstructionPaste = false;
		if (InventoryManipulation.countItem(itemStack, player, world) < neededItems) {
			if (InventoryManipulation.countPaste(player) < neededItems) {
				return;
			}
			itemStack = constructionPaste.copy();
			useConstructionPaste = true;
		}

		if (!this.canUse(heldItem, player))
			return;

		boolean useItemSuccess;
		if (useConstructionPaste) {
			useItemSuccess = InventoryManipulation.usePaste(player, 1);
		} else {
			useItemSuccess = InventoryManipulation.useItem(itemStack, player, neededItems, world);
		}

		if (useItemSuccess) {
			this.applyDamage(heldItem, player);
			//Addition
			if(!data.isEmpty()){
				world.setBlockState(pos, state);

				TileEntity te = world.getTileEntity(pos);
				if(te != null){
					if(!useConstructionPaste){
						for (int i = 0; i < cost.size(); i++) {
							itemStack = new ItemStack((NBTTagCompound)cost.get(i));
							neededItems = itemStack.getCount();
							itemStack.setCount(1);
							if(!InventoryManipulation.useItem(itemStack, player, neededItems, world)){
								for (int j = 0; j <= i; j++) {
									player.addItemStackToInventory(new ItemStack((NBTTagCompound)cost.get(i)));
								}
								return;
							}
						}
					}
					data.setInteger("x", pos.getX());
					data.setInteger("y", pos.getY());
					data.setInteger("z", pos.getZ());
					te.readFromNBT(data);
					Chunk chunk = world.getChunk(pos);
					chunk.addTileEntity(pos, te);
					chunk.markDirty();
				} else {
					TFUtils.LOGGER.error("Expected a tileEntity, but got null instead. Not copying TileData");
				}
			} else {
				//Original behavior was just that
				world.spawnEntity(new BlockBuildEntity(world, pos, player, state, 1, state, useConstructionPaste));
				TFUtils.LOGGER.info("Setting block {} at pos {} with data {}", state.getBlock(), pos, data);
			}
		}

	}
}
