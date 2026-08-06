package roidrole.tfutils.mixins.sync;

import me.ichun.mods.sync.common.tileentity.TileEntityDualVertical;
import me.ichun.mods.sync.common.tileentity.TileEntityShellConstructor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import roidrole.tfutils.config.TFUtilsConfig;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(TileEntityShellConstructor.class)
public abstract class MixinTileEntityShellConstructor extends TileEntityDualVertical<TileEntityShellConstructor> implements IEssentiaTransport {

	@Unique
	private static final int tfutils_suction_amount = 128;


	/**
	 * @author roidrole
	 * @reason no more power input
	 */
	@Overwrite(remap = false)
	public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing){
		return false;
	}

	@Inject(
		method = "func_73660_a",
		at = @At("HEAD"),
		remap = false
	)
	private void updateEssentia(CallbackInfo ci){
		TileEntityShellConstructor logicalConstructor = tfutils_getLogicalConstructor();
		if(logicalConstructor == null){
			return;
		}
		if(!logicalConstructor.canReceive()){
			return;
		}
		TileEntity te;
		IEssentiaTransport ic;

		for (EnumFacing dir : EnumFacing.HORIZONTALS) {
			te = ThaumcraftApiHelper.getConnectableTile(this.world, this.pos, dir);
			if(te == null){
				continue;
			}
			ic = (IEssentiaTransport) te;
			if (ic.getEssentiaAmount(dir.getOpposite()) > 0 && ic.getSuctionAmount(dir.getOpposite()) < tfutils_suction_amount && tfutils_suction_amount >= ic.getMinimumSuction()) {
				int ess = ic.takeEssentia(tfutils_getAspect(), 1, dir.getOpposite());
				if (ess > 0) {
					logicalConstructor.receiveEnergy(1, false);
					return;
				}
			}
		}

	}


	// ---- IEssentiaTransport and IAspectContainer methods ----
	@Override
	public boolean isConnectable(EnumFacing facing) {
		//I'm lazy, and it looks better, so pure benefit!
		return canInputFrom(facing);
	}

	@Override
	public boolean canInputFrom(EnumFacing facing) {
		//I'm lazy, and it looks better, so pure benefit!
		return facing.getHorizontalIndex() != -1;
	}

	@Override
	public boolean canOutputTo(EnumFacing facing) {
		return false;
	}

	@Override
	public void setSuction(Aspect aspect, int i) {
		//NO-OP
	}

	@Override
	public Aspect getSuctionType(EnumFacing facing) {
		if(getSuctionAmount(facing) != 0){
			return this.tfutils_getAspect();
		}
		return null;
	}

	@Override
	public int getSuctionAmount(EnumFacing facing) {
		if(!canInputFrom(facing)){
			return 0;
		}
		TileEntityShellConstructor logicalConstructor = tfutils_getLogicalConstructor();
		if(logicalConstructor != null && logicalConstructor.canReceive()){
			return tfutils_suction_amount;
		}
		return 0;
	}

	@Override
	public int takeEssentia(Aspect aspect, int amount, EnumFacing facing) {
		return 0;
	}

	@Override
	public int addEssentia(Aspect aspect, int amount, EnumFacing facing) {
		if(!canInputFrom(facing)){
			return 0;
		}
		if(getSuctionType(facing) != aspect){
			return 0;
		}

		TileEntityShellConstructor logicalConstructor = tfutils_getLogicalConstructor();
		if(logicalConstructor == null){
			return 0;
		}
		return amount - logicalConstructor.receiveEnergy(1, false);
	}

	@Override
	public Aspect getEssentiaType(EnumFacing facing) {
		return null;
	}

	@Override
	public int getEssentiaAmount(EnumFacing facing) {
		return 0;
	}

	@Override
	public int getMinimumSuction() {
		return tfutils_suction_amount;
	}

	@Unique
	public Aspect tfutils_getAspect(){
		TileEntityShellConstructor logicalConstructor = tfutils_getLogicalConstructor();
		if(logicalConstructor == null){
			return null;
		}
		return TFUtilsConfig.cloneAspects[(int) (logicalConstructor.constructionProgress % TFUtilsConfig.cloneAspects.length)];
	}

	@Unique
	public @Nullable TileEntityShellConstructor tfutils_getLogicalConstructor(){
		if(!this.top){
			return (TileEntityShellConstructor)(Object) this;
		} else {
			return this.pair;
		}
	}
}
