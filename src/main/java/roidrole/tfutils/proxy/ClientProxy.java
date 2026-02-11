package roidrole.tfutils.proxy;

import com.cleanroommc.assetmover.AssetMoverAPI;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import roidrole.tfutils.blocks.MetalPanel;
import roidrole.tfutils.blocks.NetherMetal;
import roidrole.tfutils.blocks.NetherSteelSlab;
import roidrole.tfutils.handlers.ItemTooltipHandler;
import thebetweenlands.client.gui.GuiFishStaminaBar;
import thebetweenlands.client.gui.menu.GuiBLMainMenu;
import thebetweenlands.client.gui.menu.GuiDownloadTerrainBetweenlands;
import thebetweenlands.client.handler.*;
import thebetweenlands.client.handler.equipment.RadialMenuHandler;
import thebetweenlands.client.render.entity.RenderGrapplingHookNode;
import thebetweenlands.client.render.entity.RenderVolarkite;
import thebetweenlands.client.render.sky.BLSkyRenderer;
import thebetweenlands.common.entity.mobs.EntityChiromawTame;
import thebetweenlands.common.item.equipment.ItemAmulet;
import thebetweenlands.common.item.equipment.ItemLurkerSkinPouch;
import thebetweenlands.common.item.misc.ItemBarkAmulet;
import thebetweenlands.common.item.shields.ItemSwatShield;
import thebetweenlands.common.item.tools.ItemBLFishingRod;
import thebetweenlands.common.item.tools.bow.ItemBLBow;
import thebetweenlands.common.recipe.misc.CompostRecipe;
import thebetweenlands.common.recipe.misc.SteepingPotRecipes;
import thebetweenlands.common.registries.ItemRegistry;
import thebetweenlands.common.world.event.EventHeavyRain;
import thebetweenlands.common.world.event.EventSpoopy;
import thebetweenlands.common.world.event.EventWinter;
import thebetweenlands.util.GLUProjection;
import thebetweenlands.util.RenderUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	public static Map<Pair<Item, Integer>, Set<String>> betweenlandsItemStackTooltips = new HashMap<>();
	public static Map<Item, Set<String>> betweenlandsItemTooltips = new HashMap<>();
	@Override
	public void construct() {
		super.construct();
		//TechGuns
		Map<String, String> techGuns = new HashMap<>();
		addToMap(techGuns, "assets/techguns/textures/blocks/nethermetal_border_alpha.png");
		addToMap(techGuns, "assets/techguns/textures/blocks/nethermetal_border_alpha-ctm.png");
		addToMap(techGuns, "assets/techguns/textures/blocks/nethermetal_border_alpha.png.mcmeta");
		addToMap(techGuns, "assets/techguns/models/block/nethermetal_border_lava.json");
		addCTMTexture(techGuns, "nethermetal_border_red");
		addBlockTexture(techGuns, "nethermetal_grate1");
		addBlockTexture(techGuns, "nethermetal_grate2");
		addBlockTexture(techGuns, "nethermetal_grey");
		addBlockTexture(techGuns, "nethermetal_grey_dark");
		addBlockTexture(techGuns, "nethermetal_grey_tiles");
		addBlockTexture(techGuns, "nethermetal_panel");
		addCTMTexture(techGuns, "nethermetal_plate_black");
		addCTMTexture(techGuns, "nethermetal_plate_red");
		addCTMTexture(techGuns, "steelframe_dark");
		addCTMTexture(techGuns, "steelframe_blue");
		addCTMTexture(techGuns, "container_red");
		addCTMTexture(techGuns, "container_green");
		addCTMTexture(techGuns, "container_blue");
		addCTMTexture(techGuns, "container_orange");
		addCTMTexture(techGuns, "panel_large_border");
		addCTMTexture(techGuns, "steelframe_scaffold");
		addToMap(techGuns, "assets/techguns/blockstates/nethermetal.json");
		addToMap(techGuns, "assets/techguns/blockstates/metalpanel.json");
		addToMap(techGuns, "assets/techguns/models/block/cube_glow_frame_all.json");
		addToMap(techGuns, "assets/techguns/models/block/cube_glow_frame.json");
		AssetMoverAPI.fromCurseForgeMod("244201", "2958103", techGuns);

		//Bewitchment
		Map<String, String> assets = new HashMap<>();
		addToMap(assets, "assets/bewitchment/textures/blocks/nethersteel.png");
		addToMap(assets, "assets/bewitchment/textures/blocks/chisel/nethersteel/eye.png");
		addToMap(assets, "assets/bewitchment/textures/blocks/chisel/nethersteel/bevel.png");
		addToMap(assets, "assets/bewitchment/textures/blocks/chisel/nethersteel/skull.png");
		addToMap(assets, "assets/bewitchment/textures/blocks/chisel/nethersteel/symbol.png");
		addToMap(assets, "assets/bewitchment/textures/blocks/chisel/nethersteel/hellish.png");
		addToMap(assets, "assets/bewitchment/textures/blocks/chisel/nethersteel/pentacle.png");
		addToMap(assets, "assets/bewitchment/textures/blocks/chisel/nethersteel/polished.png");
		addToMap(assets, "assets/bewitchment/textures/blocks/chisel/nethersteel/sentient.png");
		addToMap(assets, "assets/bewitchment/textures/blocks/chisel/nethersteel/pentagram.png");
		addToMap(assets, "assets/bewitchment/textures/blocks/chisel/nethersteel/watching_eye.png");
		addToMap(assets, "assets/bewitchment/textures/blocks/chisel/nethersteel/watching_hellish.png");
		addToMap(assets, "assets/bewitchment/blockstates/nethersteel.json");
		addToMap(assets, "assets/bewitchment/blockstates/nethersteel_eye.json");
		addToMap(assets, "assets/bewitchment/blockstates/nethersteel_slab.json");
		addToMap(assets, "assets/bewitchment/blockstates/nethersteel_wall.json");
		addToMap(assets, "assets/bewitchment/blockstates/nethersteel_bevel.json");
		addToMap(assets, "assets/bewitchment/blockstates/nethersteel_fence.json");
		addToMap(assets, "assets/bewitchment/blockstates/nethersteel_skull.json");
		addToMap(assets, "assets/bewitchment/blockstates/nethersteel_stairs.json");
		addToMap(assets, "assets/bewitchment/blockstates/nethersteel_symbol.json");
		addToMap(assets, "assets/bewitchment/blockstates/nethersteel_hellish.json");
		addToMap(assets, "assets/bewitchment/blockstates/nethersteel_pentacle.json");
		addToMap(assets, "assets/bewitchment/blockstates/nethersteel_polished.json");
		addToMap(assets, "assets/bewitchment/blockstates/nethersteel_sentient.json");
		addToMap(assets, "assets/bewitchment/blockstates/nethersteel_pentagram.json");
		addToMap(assets, "assets/bewitchment/blockstates/nethersteel_slab_double.json");
		addToMap(assets, "assets/bewitchment/blockstates/nethersteel_watching_eye.json");
		addToMap(assets, "assets/bewitchment/blockstates/nethersteel_watching_hellish.json");
		addToMap(assets, "assets/bewitchment/models/item/nethersteel_wall.json");
		addToMap(assets, "assets/bewitchment/models/block/nethersteel_wall_post.json");
		addToMap(assets, "assets/bewitchment/models/block/nethersteel_wall_side.json");
		addToMap(assets, "assets/bewitchment/models/block/nethersteel_wall_inventory.json");
		AssetMoverAPI.fromCurseForgeMod("285439", "6379256", assets);
	}

	@Override
	public void preInit(){
		super.preInit();
		for(int i = 0; i< MetalPanel.Type.values.length; i++) {
			ModelLoader.setCustomModelResourceLocation(
				MetalPanel.ITEM,
				i,
				new ModelResourceLocation("techguns:"+MetalPanel.Type.values[i].getName())
			);
		}
		for(int i = 0; i< NetherMetal.Type.values.length; i++) {
			ModelLoader.setCustomModelResourceLocation(
				NetherMetal.ITEM,
				i,
				new ModelResourceLocation("techguns:"+NetherMetal.Type.values[i].getName())
			);
		}
		ModelLoader.setCustomModelResourceLocation(NetherSteelSlab.ITEM, 0, new ModelResourceLocation(NetherSteelSlab.ITEM.getRegistryName(), "normal"));
	}

	@Override
	public void init() {
		super.init();

		//Compute Betweenlands' Tooltips
		SteepingPotRecipes.getRecipeList().forEach(recipe -> {
			for (Object ingredient : recipe.getInputs()){
				if(ingredient instanceof ItemStack){
					ItemStack stack = (ItemStack) ingredient;
					if(stack.isEmpty()){continue;}
					Pair<Item, Integer> key = new ImmutablePair<>(stack.getItem(), stack.getItemDamage());
					Set<String> machines = betweenlandsItemStackTooltips.computeIfAbsent(key, (a) -> new HashSet<>());
					machines.add(I18n.format("tooltip.bl.recipes.silk_bundle"));
					machines.add(I18n.format("tooltip.bl.recipes.steeping_pot"));
				}
			}
		});
		CompostRecipe.RECIPES.forEach(recipe -> {
			if(recipe instanceof CompostRecipe){
				ItemStack stack = ((CompostRecipe) recipe).getInput();
				Set<String> machines;
				if(stack.getItemDamage() == OreDictionary.WILDCARD_VALUE){
					machines = betweenlandsItemTooltips.computeIfAbsent(stack.getItem(), a -> new HashSet<>());
				} else {
					Pair<Item, Integer> key = new ImmutablePair<>(stack.getItem(), stack.getItemDamage());
					machines = betweenlandsItemStackTooltips.computeIfAbsent(key, a -> new HashSet<>());
				}
				machines.add(I18n.format("tooltip.bl.recipes.compost_bin"));
			}
		});

		betweenlandsItemTooltips.computeIfAbsent(ItemRegistry.SPIRIT_FRUIT, a -> new HashSet<>()).add(I18n.format("tooltip.bl.recipes.offering_table"));
		betweenlandsItemTooltips.computeIfAbsent(ItemRegistry.MOSS_FILTER, a -> new HashSet<>()).add(I18n.format("tooltip.bl.recipes.water_filter"));
		betweenlandsItemTooltips.computeIfAbsent(ItemRegistry.SILK_FILTER, a -> new HashSet<>()).add(I18n.format("tooltip.bl.recipes.water_filter"));
		betweenlandsItemTooltips.computeIfAbsent(ItemRegistry.SILK_BUNDLE, a -> new HashSet<>()).add(I18n.format("tooltip.bl.recipes.steeping_pot"));
	}

	@Override
	public void registerEventHandlers(){
		super.registerEventHandlers();
		//Commented out handlers are handlers from the mod that were replaced/included
		MinecraftForge.EVENT_BUS.register(ShaderHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(FogHandler.class);
		MinecraftForge.EVENT_BUS.register(AmbienceSoundPlayHandler.class);
		MinecraftForge.EVENT_BUS.register(GLUProjection.getInstance());
		MinecraftForge.EVENT_BUS.register(WorldRenderHandler.class);
		MinecraftForge.EVENT_BUS.register(ScreenRenderHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(DecayRenderHandler.class);
		MinecraftForge.EVENT_BUS.register(CameraPositionHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(MusicHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(ThemHandler.class);
		MinecraftForge.EVENT_BUS.register(RadialMenuHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(ItemAmulet.class);
		MinecraftForge.EVENT_BUS.register(InputHandler.class);
		MinecraftForge.EVENT_BUS.register(ItemLurkerSkinPouch.class);
		MinecraftForge.EVENT_BUS.register(BrightnessHandler.class);
		MinecraftForge.EVENT_BUS.register(DebugHandlerClient.class);
		//MinecraftForge.EVENT_BUS.register(ItemTooltipHandler.class);
		MinecraftForge.EVENT_BUS.register(GuiBLMainMenu.class);
		MinecraftForge.EVENT_BUS.register(WeedwoodRowboatHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(OverlayHandler.class);
		MinecraftForge.EVENT_BUS.register(ElixirClientHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(GuiDownloadTerrainBetweenlands.class);
		MinecraftForge.EVENT_BUS.register(ItemBLBow.class);
		MinecraftForge.EVENT_BUS.register(ItemSwatShield.class);
		MinecraftForge.EVENT_BUS.register(EventWinter.class);
		MinecraftForge.EVENT_BUS.register(EventSpoopy.class);
		MinecraftForge.EVENT_BUS.register(ArmSwingSpeedHandler.class);
		MinecraftForge.EVENT_BUS.register(BLSkyRenderer.class);
		MinecraftForge.EVENT_BUS.register(ItemBarkAmulet.class);
		MinecraftForge.EVENT_BUS.register(RenderGrapplingHookNode.class);
		//MinecraftForge.EVENT_BUS.register(ExtendedReachHandler.class);
		MinecraftForge.EVENT_BUS.register(RenderVolarkite.class);
		MinecraftForge.EVENT_BUS.register(RenderUtils.class);
		MinecraftForge.EVENT_BUS.register(EntityChiromawTame.class);
		MinecraftForge.EVENT_BUS.register(EventHeavyRain.class);
		MinecraftForge.EVENT_BUS.register(new GuiFishStaminaBar());
		MinecraftForge.EVENT_BUS.register(ItemBLFishingRod.class);

		//TFUtils
		MinecraftForge.EVENT_BUS.register(ItemTooltipHandler.class);
		MinecraftForge.EVENT_BUS.register(CameraPositionHandler.INSTANCE);
	}

	//Helpers
	public static void addBlockTexture(Map<String, String> map, String texture){
		addToMap(map, "assets/techguns/textures/blocks/"+texture+".png");
		addToMap(map, "assets/techguns/models/block/"+texture+".json");
	}
	public static void addCTMTexture(Map<String, String> map, String texture){
		addToMap(map, "assets/techguns/textures/blocks/"+texture+"-ctm.png");
		addToMap(map, "assets/techguns/textures/blocks/"+texture+".png");
		addToMap(map, "assets/techguns/textures/blocks/"+texture+".png.mcmeta");
		addToMap(map, "assets/techguns/models/block/"+texture+".json");
	}
	public static void addToMap(Map<String, String> map, String fullTexture){
		map.put(fullTexture, fullTexture);
	}

	@Override
	public void registerBlock(Block block, Item item){
		super.registerBlock(block, item);
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "normal"));
	}
}