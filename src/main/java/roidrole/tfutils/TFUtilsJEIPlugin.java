package roidrole.tfutils;

import com.buuz135.thaumicjei.ThaumcraftJEIPlugin;
import com.buuz135.thaumicjei.ThaumicJEI;
import com.buuz135.thaumicjei.category.AspectFromItemStackCategory;
import com.buuz135.thaumicjei.config.ThaumicConfig;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import roidrole.tfutils.config.TFUtilsConfig;
import roidrole.tfutils.utils.ArrayMap;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.items.ItemsTC;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
			parseAspectsFile(aspectFile, registry);
		}

		registry.addRecipeCatalyst(new ItemStack(ItemsTC.alumentum), "inworldcrafting.explode_item", "inworldcrafting.exploding_blocks");
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

		Map<Aspect, ArrayMap<List<String>>> cache = new ConcurrentHashMap<>();
		for(Aspect aspect : Aspect.aspects.values()){
			cache.put(aspect, new ArrayMap<>());
		}

		//Because concurrency
		final int[] cachedAmount = {0};
		final long[] lastTimeChecked = {System.currentTimeMillis()};

		items
			.parallelStream()
			.filter(stack -> !blacklist.contains(Item.REGISTRY.getNameForObject(stack.getItem())))
			//Since Thaumcraft caches ItemStack aspects itself, filtering for empty AspectList is fine
			.filter(stack -> {
				AspectList list = AspectHelper.getObjectAspects(stack);
				if (list == null || list.size() == 0){
					cachedAmount[0]++;
					return false;
				}
				return true;
			})
			.forEach(stack -> {
				AspectList list = AspectHelper.getObjectAspects(stack);
				list.aspects.forEach((aspect, count) -> {
					cache
						.get(aspect)
						.computeIfAbsent(count, ArrayList::new)
						.add(writeItemStack(stack, count))
					;
				});

				cachedAmount[0]++;
				if (lastTimeChecked[0] + 5000 < System.currentTimeMillis()) {
					lastTimeChecked[0] = System.currentTimeMillis();
					ThaumicJEI.LOGGER.info("ItemStack Aspect checking at {}%", 100 * cachedAmount[0] / items.size());
				}
			})
		;


		ThaumicJEI.LOGGER.info("ItemStack Aspect checking at 100%");
		try (JsonWriter writer = new JsonWriter(new FileWriter(aspectFile))){
			writer.setIndent("\t");
			//Write the JSON by hand. Less annoying
			writer.beginObject();
			cache
				.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByKey(Comparator.comparing(Aspect::getTag)))
				.forEach(entry -> {
				try {
					writer.name(entry.getKey().getTag());
					writer.beginArray();
					entry.getValue().forEach(
						(count, list) -> list.forEach(stack -> {
							try {
								writer.value(stack);
							} catch (IOException ignored) { }
						})
					);
					writer.endArray();
				} catch (IOException ignored) { }
			});
			writer.endObject();
		} catch (IOException e) {
			ThaumicJEI.LOGGER.error("Can't write aspect file!", e);
		}
	}

	public void parseAspectsFile(File aspectFile, IModRegistry registry){
		long time = System.currentTimeMillis();
		List<AspectFromItemStackCategory.AspectFromItemStackWrapper> wrappers = new ArrayList<>();
		Map<Aspect, List<ItemStack>> cache = new HashMap<>();

		try (JsonReader reader = new JsonReader(new FileReader(aspectFile))){
			reader.beginObject();

			do {
				Aspect aspect = Aspect.getAspect(reader.nextName());
				List<ItemStack> list = new ArrayList<>();
				cache.put(aspect, list);
				reader.beginArray();
				while(reader.peek() != JsonToken.END_ARRAY){
					list.add(readItemStack(reader.nextString()));
				}
				reader.endArray();

			} while (reader.peek() != JsonToken.END_OBJECT);
			reader.endObject();


			cache.forEach((aspect, stacks) -> {
				AspectList aspectList = new AspectList().add(aspect, 1);
				int start = 0;
				while (start < stacks.size() - 36) {
					List<ItemStack> subList = stacks.subList(start, Math.min(start + 36, stacks.size()));
					wrappers.add(new AspectFromItemStackCategory.AspectFromItemStackWrapper(aspectList, subList));
					start += 36;
				}
			});
		} catch (FileNotFoundException e) {
			ThaumicJEI.LOGGER.error("Can't read aspect file!", e);
			return;
		} catch (NBTException e) {
			ThaumicJEI.LOGGER.error("Malformed aspect file. Please regenerate", e);
		} catch (IOException e) {
			ThaumicJEI.LOGGER.error("Can't read aspect file. Please regenerate", e);
		}
		ThaumicJEI.LOGGER.info("Parsed aspect file in {} ms", System.currentTimeMillis() - time);

		registry.addRecipes(wrappers, ThaumcraftJEIPlugin.aspectFromItemStackCategory.getUid());
	}

	public static String writeItemStack(ItemStack stack, int count){
		StringBuilder itemNbt = new StringBuilder("{");
		itemNbt.append("id:\"");
		itemNbt.append(Item.REGISTRY.getNameForObject(stack.getItem()));
		itemNbt.append("\"");
		if(stack.getItemDamage() != 0){
			itemNbt.append(",Damage:");
			itemNbt.append(stack.getItemDamage());
		}
		if (stack.getTagCompound() != null && !stack.getTagCompound().isEmpty()){
			itemNbt.append(",tag:");
			itemNbt.append(NBTTagString.quoteAndEscape(stack.getTagCompound().toString()));
		}
		if (count > 1){
			itemNbt.append(",Count:");
			itemNbt.append(count);
		}
		itemNbt.append("}");
		return itemNbt.toString();
	}

	public static ItemStack readItemStack(String string) throws IOException, NBTException {
		JsonReader itemReader = new JsonReader(new StringReader(string));
		itemReader.setLenient(true);
		itemReader.beginObject();
		//We know it to be "id"
		itemReader.nextName();
		ItemStack stack = new ItemStack(Item.getByNameOrId(itemReader.nextString()));
		while(itemReader.peek() != JsonToken.END_OBJECT){
			switch (itemReader.nextName()){
				case "Damage":{
					stack.setItemDamage(itemReader.nextInt());
					break;
				}
				case "tag":{
					String tag = itemReader.nextString();
					if(tag.length() <= 2){
						break;
					}
					stack.setTagCompound(JsonToNBT.getTagFromJson(tag));
					break;
				}
				case "Count":{
					stack.setCount(itemReader.nextInt());
					break;
				}
			}
		}
		return stack;
	}
}
