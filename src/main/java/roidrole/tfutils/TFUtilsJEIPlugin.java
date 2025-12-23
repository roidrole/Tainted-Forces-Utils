package roidrole.tfutils;

import com.buuz135.thaumicjei.ThaumcraftJEIPlugin;
import com.buuz135.thaumicjei.ThaumicJEI;
import com.buuz135.thaumicjei.category.AspectFromItemStackCategory;
import com.buuz135.thaumicjei.config.ThaumicConfig;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import it.unimi.dsi.fastutil.ints.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import roidrole.tfutils.utils.ArrayMap;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.*;

@JEIPlugin
public class TFUtilsJEIPlugin implements IModPlugin {
	private static final File CACHE_FOLDER = new File((File) Launch.blackboard.get("CachesFolderFile"), ThaumicJEI.MOD_ID);
	static {
		CACHE_FOLDER.mkdirs();
	}
	public static Thread aspectCacheThread;

	@Override
	public void register(@Nonnull IModRegistry registry){
		File aspectFile = new File(CACHE_FOLDER, "itemstack_aspects.json");

		//Aspect cache
		if (aspectCacheThread == null && (!aspectFile.exists() || TFUtilsConfig.regenAspectCache)) {
			aspectCacheThread = new Thread(() -> {
				createAspectsFile(aspectFile, registry);
				parseAspectsFile(aspectFile, registry);
				ThaumicJEI.LOGGER.info("Finished Aspect ItemStack Thread.");
			}, "ThaumicJEI Aspect Cache");
			aspectCacheThread.start();
		} else {
			long time = System.currentTimeMillis();
			parseAspectsFile(aspectFile, registry);
			ThaumicJEI.LOGGER.info("Parsed aspect file in {} ms", System.currentTimeMillis() - time);
		}

	}


	public void createAspectsFile(File aspectFile, IModRegistry registry) {
		Collection<ItemStack> items = registry.getIngredientRegistry().getAllIngredients(ItemStack.class);
		ThaumicJEI.LOGGER.info("Starting Aspect ItemStack Thread.");
		ThaumicJEI.LOGGER.info("Trying to cache {} aspects.", items.size());
		//Filter out blacklisted items
		Set<ResourceLocation> blacklist = new HashSet<>();
		for (String string : ThaumicConfig.blacklistedFromAspectChecking){
			blacklist.add(new ResourceLocation(string));
		}
		blacklist.add(null);
		blacklist.add(Items.AIR.getRegistryName());
		List<ItemStack> ignoredStacks = new ArrayList<>();

		int cachedAmount = 0;
		long lastTimeChecked = System.currentTimeMillis();
		Map<Aspect, ArrayMap<List<NBTTagCompound>>> aspectCacheHashMap = new HashMap<>();
		for (ItemStack stack : items) {
			ResourceLocation resourcelocation = Item.REGISTRY.getNameForObject(stack.getItem());
			if(blacklist.contains(resourcelocation)){
				cachedAmount++;
				continue;
			}


			AspectList list = AspectHelper.getObjectAspects(stack);
			if (list == null || list.size() == 0){
				cachedAmount++;
				continue;
			}

			NBTTagCompound itemNbt = new NBTTagCompound();
			itemNbt.setString("id", resourcelocation.toString());
			if(stack.getItemDamage() != 0){
				itemNbt.setInteger("Damage", stack.getItemDamage());
			}
			if (stack.getTagCompound() != null){
				itemNbt.setTag("tag", stack.getTagCompound());
			}
			for (Aspect aspect : list.getAspects()) {
				int count = Math.max(list.getAmount(aspect), 1);
				ArrayMap<List<NBTTagCompound>> aspectList = aspectCacheHashMap.computeIfAbsent(aspect, key -> new ArrayMap<>());
				List<NBTTagCompound> countList = aspectList.computeIfAbsent(count, ArrayList::new);
				countList.add(itemNbt);
			}
			cachedAmount++;
			if (lastTimeChecked + 5000 < System.currentTimeMillis()) {
				lastTimeChecked = System.currentTimeMillis();
				ThaumicJEI.LOGGER.info("ItemStack Aspect checking at {}%", 100 * cachedAmount / items.size());
			}

		}
		ThaumicJEI.LOGGER.info("ItemStack Aspect checking at 100%");
		try {
			JsonWriter writer = new JsonWriter(new FileWriter(aspectFile));
			writer.setIndent("	");
			//Write the JSON by hand. Less annoying
			writer.beginObject();
			for(Map.Entry<Aspect, ArrayMap<List<NBTTagCompound>>> entry : aspectCacheHashMap.entrySet()){
				writer.name(entry.getKey().getTag());
				writer.beginArray();
				entry.getValue().forEach((count, itemList) -> {
					for (NBTTagCompound nbtTagCompound : itemList) {
						nbtTagCompound.setInteger("Count", count);
						try {
							writer.value(nbtTagCompound.toString());
						} catch (IOException ignored) {}
					}
				});
				writer.endArray();
			}
			writer.endObject();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			ThaumicJEI.LOGGER.error("Can't write aspect file!", e);
		}
	}

	public void parseAspectsFile(File aspectFile, IModRegistry registry){

		JsonReader reader;
		List<AspectFromItemStackCategory.AspectFromItemStackWrapper> wrappers = new ArrayList<>();
		try {
			reader = new JsonReader(new FileReader(aspectFile));
		} catch (FileNotFoundException e) {
			ThaumicJEI.LOGGER.error("Can't read aspect file!", e);
			return;
		}

		try {
			reader.beginObject();
			do {
				AspectList aspect = new AspectList().add(Aspect.getAspect(reader.nextName()), 1);
				List<ItemStack> stacks = new ArrayList<>();
				reader.beginArray();
				while (reader.peek() == JsonToken.STRING) {
					stacks.add(new ItemStack(JsonToNBT.getTagFromJson(reader.nextString())));
				}
				reader.endArray();
				int start = 0;
				while (start < stacks.size()) {
					List<ItemStack> subList = stacks.subList(start, Math.min(start + 36, stacks.size()));
					wrappers.add(new AspectFromItemStackCategory.AspectFromItemStackWrapper(aspect, subList));
					start += 36;
				}
			} while (reader.peek() != JsonToken.END_OBJECT);
			reader.close();
		} catch (NBTException e) {
			ThaumicJEI.LOGGER.error("Malformed aspect file. Please regenerate", e);
		} catch (IOException e) {
			ThaumicJEI.LOGGER.error("Can't read aspect file. Please regenerate", e);
		}

		registry.addRecipes(wrappers, ThaumcraftJEIPlugin.aspectFromItemStackCategory.getUid());
	}
}
