package roidrole.tfutils.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import roidrole.tfutils.blocks.*;

public class CommonProxy {

    public void construct(){
        //Only overridden by ClientProxy
    }

    public void preInit(){
        ForgeRegistries.BLOCKS.register(MetalPanel.BLOCK);
        ForgeRegistries.ITEMS.register(MetalPanel.ITEM);
        ForgeRegistries.BLOCKS.register(NetherMetal.BLOCK);
        ForgeRegistries.ITEMS.register(NetherMetal.ITEM);

        registerBlock(NetherSteel.BLOCK, NetherSteel.ITEM);
        for (String variant : new String[]{"symbol", "bevel", "polished", "sentient", "pentacle", "pentagram", "skull", "eye", "watching_eye", "hellish", "watching_hellish"}){
            NetherSteel toRegister = new NetherSteel(variant);
            toRegister.pickItem = new NetherSteel.NetherSteelItem(toRegister, variant);
            registerBlock(toRegister, toRegister.pickItem);
        }
        registerBlock(NetherSteelFence.BLOCK, NetherSteelFence.ITEM);
        registerBlock(NetherSteelStairs.BLOCK, NetherSteelStairs.ITEM);
        ForgeRegistries.BLOCKS.register(NetherSteelSlab.HALF);
        ForgeRegistries.BLOCKS.register(NetherSteelSlab.DOUBLE);
        ForgeRegistries.ITEMS.register(NetherSteelSlab.ITEM);
        registerBlock(NetherSteelWall.BLOCK, NetherSteelWall.ITEM);
		for (int i = 0; i < 16; i++) {
			String colour = EnumDyeColor.byMetadata(i).getName();
			ConcreteSlab half = new ConcreteSlab(NetherSteel.BLOCK, colour);
			ConcreteSlab DOUBLE = new ConcreteSlab(NetherSteel.BLOCK, colour, true);
			DOUBLE.HALF = half;
			ItemBlock ITEM = new ItemSlab(half, half, DOUBLE){{
				setRegistryName(block.getRegistryName());
				setTranslationKey(block.getTranslationKey());
			}};
			registerBlock(half, ITEM);
			ForgeRegistries.BLOCKS.register(DOUBLE);
		}
    }

    public void init(){
        //NO-OP
    }

    public void postInit(){
        //NO-OP
    }

    public void registerBlock(Block block, Item item){
        ForgeRegistries.BLOCKS.register(block);
        ForgeRegistries.ITEMS.register(item);
    }
}