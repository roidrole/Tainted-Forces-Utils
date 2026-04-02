package roidrole.tfutils.hwyla;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mysticalmechanics.api.IMechCapability;
import mysticalmechanics.api.MysticalMechanicsAPI;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class ProviderMechanicalPower implements IWailaDataProvider {
	public static final ProviderMechanicalPower INSTANCE = new ProviderMechanicalPower();

	public static final String mechPowerKey = "mech_power";

	@Override
	@Nonnull
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
		IMechCapability cap = te.getCapability(MysticalMechanicsAPI.MECH_CAPABILITY, null);
		if(cap == null){
			return tag;
		}
		tag.setDouble(mechPowerKey, cap.getPower(null));
		return tag;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		NBTTagCompound tag = accessor.getNBTData();
		if(!tag.hasKey(mechPowerKey)){
			return tooltip;
		}
		tooltip.add("Mechanical power: "+tag.getDouble(mechPowerKey));
		return tooltip;

	}
}
