package roidrole.tfutils.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import roidrole.tfutils.blocks.*;
import thebetweenlands.common.BLDataFixers;
import thebetweenlands.common.block.farming.BlockGenericDugSoil;
import thebetweenlands.common.block.plant.BlockWeedwoodBush;
import thebetweenlands.common.capability.base.EntityCapabilityHandler;
import thebetweenlands.common.capability.blessing.BlessingEntityCapability;
import thebetweenlands.common.capability.collision.RingOfDispersionEntityCapability;
import thebetweenlands.common.capability.falldamagereduce.FallDamageReductionCapability;
import thebetweenlands.common.capability.fishing.RotSmellEntityCapability;
import thebetweenlands.common.capability.lastkilled.LastKilledCapability;
import thebetweenlands.common.capability.playermounts.PlayerMountsEntityCapability;
import thebetweenlands.common.capability.swarmed.SwarmedCapability;
import thebetweenlands.common.config.ConfigHelper;
import thebetweenlands.common.entity.EntityVolarkite;
import thebetweenlands.common.entity.draeton.EntityDraeton;
import thebetweenlands.common.entity.mobs.EntityChiromawMatriarch;
import thebetweenlands.common.entity.mobs.EntitySludgeMenace;
import thebetweenlands.common.entity.mobs.EntitySwarm;
import thebetweenlands.common.entity.projectiles.EntitySilkyPebble;
import thebetweenlands.common.entity.rowboat.EntityWeedwoodRowboat;
import thebetweenlands.common.handler.*;
import thebetweenlands.common.herblore.elixir.PotionRootBound;
import thebetweenlands.common.item.armor.ItemAncientArmor;
import thebetweenlands.common.item.equipment.ItemRingOfFlight;
import thebetweenlands.common.item.misc.ItemMagicItemMagnet;
import thebetweenlands.common.item.misc.ItemRingOfGathering;
import thebetweenlands.common.item.shields.ItemDentrothystShield;
import thebetweenlands.common.item.tools.ItemBLShield;
import thebetweenlands.common.item.tools.ItemGreatsword;
import thebetweenlands.common.recipe.censer.CenserRecipeCremains;
import thebetweenlands.common.registries.GameruleRegistry;
import thebetweenlands.common.tile.TileEntitySimulacrum;
import thebetweenlands.common.world.biome.spawning.WorldMobSpawner;
import thebetweenlands.common.world.storage.BetweenlandsChunkStorage;
import thebetweenlands.common.world.storage.OfflinePlayerHandlerImpl;
import thebetweenlands.common.world.storage.WorldStorageImpl;

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
        //Only overridden by ClientProxy
    }

    public void postInit(){
        //NO-OP
    }

	public void registerEventHandlers(){
		//Betweenlands
		WorldStorageImpl.register();

		MinecraftForge.EVENT_BUS.register(ConfigHelper.class);
		MinecraftForge.EVENT_BUS.register(ItemBLShield.EventHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(WorldEventHandler.class);
		MinecraftForge.EVENT_BUS.register(BetweenlandsChunkStorage.class);
		//MinecraftForge.EVENT_BUS.register(new AnvilEventHandler());
		MinecraftForge.EVENT_BUS.register(EnvironmentEventHandler.class);
		MinecraftForge.EVENT_BUS.register(EntityCapabilityHandler.class);
		//MinecraftForge.EVENT_BUS.register(ItemCapabilityHandler.class);
		MinecraftForge.EVENT_BUS.register(PlayerDecayHandler.class);
		MinecraftForge.EVENT_BUS.register(AspectSyncHandler.class);
		MinecraftForge.EVENT_BUS.register(WorldMobSpawner.INSTANCE);
		MinecraftForge.EVENT_BUS.register(BlockBreakHandler.class);
		MinecraftForge.EVENT_BUS.register(LocationHandler.class);
		MinecraftForge.EVENT_BUS.register(AttackDamageHandler.class);
		MinecraftForge.EVENT_BUS.register(ItemEquipmentHandler.class);
		MinecraftForge.EVENT_BUS.register(EntitySpawnHandler.class);
		MinecraftForge.EVENT_BUS.register(ArmorHandler.class);
		MinecraftForge.EVENT_BUS.register(ItemRingOfFlight.class);
		MinecraftForge.EVENT_BUS.register(PuppetHandler.class);
		MinecraftForge.EVENT_BUS.register(OverworldItemHandler.class);
		MinecraftForge.EVENT_BUS.register(PlayerPortalHandler.class);
		MinecraftForge.EVENT_BUS.register(FoodSicknessHandler.class);
		MinecraftForge.EVENT_BUS.register(BlockGenericDugSoil.class);
		MinecraftForge.EVENT_BUS.register(ElixirCommonHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(BlockWeedwoodBush.class);
		MinecraftForge.EVENT_BUS.register(ItemDentrothystShield.class);
		MinecraftForge.EVENT_BUS.register(EnvironmentEventOverridesHandler.class);
		MinecraftForge.EVENT_BUS.register(AdvancementHandler.class);
		MinecraftForge.EVENT_BUS.register(FuelHandler.class);
		MinecraftForge.EVENT_BUS.register(PlayerJoinWorldHandler.class);
		MinecraftForge.EVENT_BUS.register(PlayerRespawnHandler.class);
		MinecraftForge.EVENT_BUS.register(CustomEntityCollisionsHandler.class);
		MinecraftForge.EVENT_BUS.register(PotionRootBound.class);
		MinecraftForge.EVENT_BUS.register(BossHandler.class);
		MinecraftForge.EVENT_BUS.register(ItemMagicItemMagnet.class);
		MinecraftForge.EVENT_BUS.register(EntityWeedwoodRowboat.class);
		MinecraftForge.EVENT_BUS.register(GameruleRegistry.class);
		MinecraftForge.EVENT_BUS.register(RingOfDispersionEntityCapability.class);
		MinecraftForge.EVENT_BUS.register(ItemGreatsword.class);
		MinecraftForge.EVENT_BUS.register(CenserRecipeCremains.class);
		MinecraftForge.EVENT_BUS.register(EntitySludgeMenace.class);
		MinecraftForge.EVENT_BUS.register(OfflinePlayerHandlerImpl.class);
		MinecraftForge.EVENT_BUS.register(ItemRingOfGathering.class);
		MinecraftForge.EVENT_BUS.register(EntityVolarkite.class);
		MinecraftForge.EVENT_BUS.register(BLDataFixers.class);
		MinecraftForge.EVENT_BUS.register(EntityDraeton.class);
		MinecraftForge.EVENT_BUS.register(PlayerMountsEntityCapability.class);
		MinecraftForge.EVENT_BUS.register(EntityChiromawMatriarch.class);
		MinecraftForge.EVENT_BUS.register(ItemAncientArmor.class);
		MinecraftForge.EVENT_BUS.register(EntityUnmountHandler.class);
		MinecraftForge.EVENT_BUS.register(LastKilledCapability.class);
		MinecraftForge.EVENT_BUS.register(TileEntitySimulacrum.class);
		MinecraftForge.EVENT_BUS.register(BlessingEntityCapability.class);
		MinecraftForge.EVENT_BUS.register(SwarmedCapability.class);
		MinecraftForge.EVENT_BUS.register(RotSmellEntityCapability.class);
		MinecraftForge.EVENT_BUS.register(EntitySwarm.class);
		MinecraftForge.EVENT_BUS.register(CorrosiveBootsHandler.class);
		MinecraftForge.EVENT_BUS.register(FallDamageReductionCapability.class);
		MinecraftForge.EVENT_BUS.register(EntitySilkyPebble.class);
	}

	//Utils

    public void registerBlock(Block block, Item item){
        ForgeRegistries.BLOCKS.register(block);
        ForgeRegistries.ITEMS.register(item);
    }
}