package roidrole.tfutils.hwyla;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.tileentity.TileEntity;

@WailaPlugin
public class TFUtilsWailaPlugin implements IWailaPlugin {
	@Override
	public void register(IWailaRegistrar registrar) {
		registrar.registerBodyProvider(ProviderMechanicalPower.INSTANCE, TileEntity.class);
		registrar.registerNBTProvider(ProviderMechanicalPower.INSTANCE, TileEntity.class);
	}

}
