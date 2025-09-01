package roidrole.tfutils.proxy;

import com.cleanroommc.assetmover.AssetMoverAPI;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import roidrole.tfutils.blocks.MetalPanel;
import roidrole.tfutils.blocks.NetherMetal;
import roidrole.tfutils.blocks.NetherSteelSlab;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	@Override
	public void construct() {
		super.construct();
		//TechGuns
		Map<String, String> techGuns = new HashMap<>();
		addToMap(techGuns, "assets/techguns/textures/blocks/nethermetal_border_alpha.png");
		addToMap(techGuns, "assets/techguns/textures/blocks/nethermetal_border_alpha-ctm.png");
		addToMap(techGuns, "assets/techguns/textures/blocks/nethermetal_border_alpha.png.mcmeta");
		addToMap(techGuns, "assets/techguns/models/blocks/nethermetal_border_lava.json");
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