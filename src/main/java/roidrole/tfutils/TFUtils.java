package roidrole.tfutils;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import roidrole.tfutils.network.PacketHandler;
import roidrole.tfutils.proxy.CommonProxy;


@Mod(
    modid = Tags.MOD_ID,
    name = Tags.MOD_NAME,
    version = Tags.VERSION
)
public class TFUtils {

    //Proxy
    @SidedProxy(clientSide = Tags.ROOT_PACKAGE+".proxy.ClientProxy", serverSide = Tags.ROOT_PACKAGE+".proxy.ServerProxy")
    //If one proxy isn't used, point it to CommonProxy
    public static CommonProxy PROXY;
    public static Logger LOGGER;

    @Mod.EventHandler
    public void construct(FMLConstructionEvent event){
        PROXY.construct();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();

        PROXY.preInit();
        PROXY.registerEventHandlers();
        PacketHandler.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {PROXY.init();}

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {PROXY.postInit();}
}