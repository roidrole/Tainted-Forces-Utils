package roidrole.tfutils.mixins.cyberware;

import baubles.api.BaublesApi;
import flaxbeard.cyberware.common.block.tile.TileEntityBeaconLarge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.lib.events.EssentiaHandler;

import java.util.List;

@Mixin(TileEntityBeaconLarge.class)
public abstract class TileEntityBeaconLargeMixin extends TileEntity {
	@Shadow(remap = false)
	private boolean wasWorking;

	@Inject(
		method = "func_73660_a",
		at = @At("HEAD"),
		remap = false
	)
	@SuppressWarnings("deprecation")
	private void refuelEssentia(CallbackInfo ci){
		if(!this.wasWorking || this.world.getWorldTime() % 16 != 0){
			return;
		}
		List<EntityPlayer> playersInArea = this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(
			this.pos.getX() - 32,
			this.pos.getY() - 32,
			this.pos.getZ() - 32,
			this.pos.getX() + 32,
			this.pos.getY() + 32,
			this.pos.getZ() + 32
		));
		for(EntityPlayer player : playersInArea){
			int slot = BaublesApi.isBaubleEquipped(
				player,
				ForgeRegistries.ITEMS.getValue(
					new ResourceLocation("thaumicaugmentation", "thaumostatic_harness")
				)
			);
			if(slot == -1){
				continue;
			}
			ItemStack stack = BaublesApi.getBaubles(player).getStackInSlot(slot);
			NBTTagCompound tag = stack.getTagCompound();
			if(tag == null){
				continue;
			}
			int charge = tag.getInteger("tc.charge");
			if(charge < 5){
				if (!EssentiaHandler.drainEssentia(
					this,
					Aspect.ENERGY,
					EnumFacing.DOWN,
					16,
					false,
					5
				)){
					return;
				}
				stack.getTagCompound().setInteger("tc.charge", charge + 1);
			}
		}
	}
}
