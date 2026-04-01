package roidrole.tfutils.hwyla;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.items.IGogglesDisplayExtended;
import thaumcraft.common.blocks.devices.BlockVisBattery;

@WailaPlugin
public class TFUtilsWailaPlugin implements IWailaPlugin {
	@Override
	public void register(IWailaRegistrar registrar) {
		registrar.registerBodyProvider(EssentiaTransportProvider.INSTANCE, IEssentiaTransport.class);
		registrar.registerNBTProvider(EssentiaTransportProvider.INSTANCE, IEssentiaTransport.class);

		registrar.registerBodyProvider(BlockVisBatteryProvider.INSTANCE, BlockVisBattery.class);

		registrar.registerBodyProvider(GogglesDisplayProvider.INSTANCE, IGogglesDisplayExtended.class);

		registrar.registerTooltipRenderer("thaumicwaila.aspect", new AspectRender());
	}

}
