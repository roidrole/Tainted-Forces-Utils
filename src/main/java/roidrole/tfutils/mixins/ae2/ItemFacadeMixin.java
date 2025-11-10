package roidrole.tfutils.mixins.ae2;

import appeng.items.parts.ItemFacade;
import cofh.core.util.RayTracer;
import cofh.core.util.helpers.BlockHelper;
import cofh.thermaldynamics.duct.Attachment;
import cofh.thermaldynamics.duct.attachments.cover.Cover;
import cofh.thermaldynamics.duct.attachments.cover.CoverHelper;
import cofh.thermaldynamics.duct.tiles.TileGrid;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFacade.class)
public abstract class ItemFacadeMixin extends Item {

	@Inject(
		method = "onItemUseFirst",
		at = @At(
			value = "INVOKE",
			target = "Lappeng/items/parts/ItemFacade;createPartFromItemStack(Lnet/minecraft/item/ItemStack;Lappeng/api/util/AEPartLocation;)Lappeng/facade/FacadePart;"
		),
		cancellable = true,
		remap = false
	)
	public void handleTD(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand, CallbackInfoReturnable<EnumActionResult> cir){
		ItemStack stack = player.getHeldItem(hand);
		Attachment attachment = ItemFacadeMixin.TFUtils_getAttachment(stack, player, world, pos, side);

		if (attachment != null && attachment.addToTile()) {
			if (!player.capabilities.isCreativeMode) {
				stack.shrink(1);
			}
			cir.setReturnValue(EnumActionResult.SUCCESS);
		}
	}

	@Unique
	private static Attachment TFUtils_getAttachment(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side) {
		Attachment attachment = null;

		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileGrid) {
			int s;
			RayTraceResult movingObjectPosition = RayTracer.retraceBlock(world, player, pos);
			if (movingObjectPosition != null) {
				int subHit = movingObjectPosition.subHit;
				if (subHit < 6) {
					s = subHit;
				} else if (subHit < 12) {
					s = (subHit - 6);
				} else if (subHit == 13) {
					s = side.ordinal();
				} else {
					s = ((subHit - 14) % 6);
				}
				if (s != -1) {
					attachment = ((ItemFacadeMixin) stack.getItem()).TFUtils_getAttachment(EnumFacing.VALUES[s ^ 1], stack, (TileGrid) tile);
				}
			}
		} else {
			tile = BlockHelper.getAdjacentTileEntity(world, pos, side);
			if (tile instanceof TileGrid) {
				attachment = ((ItemFacadeMixin) stack.getItem()).TFUtils_getAttachment(side, stack, (TileGrid) tile);
			}
		}
		return attachment;
	}

	@Unique
	public Attachment TFUtils_getAttachment(EnumFacing side, ItemStack stack, TileGrid tile) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null || !nbt.hasKey("damage", 99) || !nbt.hasKey("item", 8)) {
			return null;
		}
		int meta = nbt.getByte("damage");
		Block block = Block.getBlockFromName(nbt.getString("item"));

		if (block == Blocks.AIR || meta < 0 || meta >= 16 || !CoverHelper.isValid(block, meta)) {
			return null;
		}
		return new Cover(tile, ((byte) (side.ordinal() ^ 1)), block.getStateFromMeta(meta));
	}



}
