package roidrole.tfutils.config;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.nbt.*;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

import static com.google.gson.stream.JsonToken.*;

public class BuildingGadgetIgnoredNBTKeys {
	public static BuildingGadgetIgnoredNBTKeys INSTANCE = new BuildingGadgetIgnoredNBTKeys();

	List<Action> actions;

	public void filter(NBTTagCompound tag, NBTTagList cost){
		for(Action action: actions){
			action.filter(tag, cost);
		}
	}

	public static abstract class Action {
		public abstract void filter(NBTTagCompound tag, NBTTagList cost);
		public abstract void read(JsonReader reader) throws IOException;
	}

	public static class Ignore extends Action {
		List<String> keys;

		@Override
		public void filter(NBTTagCompound tag, NBTTagList cost) {
			for(String key : keys){
				tag.removeTag(key);
			}
		}

		@Override
		public void read(JsonReader reader) throws IOException {
			if(reader.peek() == BEGIN_ARRAY){
				reader.beginArray();
				keys = new ArrayList<>();
				while(reader.peek() == STRING){
					keys.add(reader.nextString());
				}
				((ArrayList<String>)keys).trimToSize();
				reader.endArray();
			} else {
				keys = Collections.singletonList(reader.nextString());
			}
		}
	}

	public static class Iterate extends Action {
		public List<String> keys;
		public List<BuildingGadgetIgnoredNBTKeys> actions;

		@Override
		public void filter(NBTTagCompound tag, NBTTagList cost) {
			for (int i = 0; i < keys.size(); i++) {
				NBTBase list = tag.getTag(keys.get(i));
				//OOP gore. Might be refactorable
				if(list instanceof NBTTagList){
					for(NBTBase nbt:((NBTTagList) list).tagList){
						if(nbt instanceof NBTTagCompound){
							actions.get(i).filter((NBTTagCompound) nbt, cost);
						}
					}
				}
			}
		}

		@Override
		public void read(JsonReader reader) throws IOException {
			keys = new ArrayList<>();
			actions = new ArrayList<>();
			reader.beginObject();
			while(reader.peek() == NAME){
				keys.add(reader.nextName());
				BuildingGadgetIgnoredNBTKeys action = new BuildingGadgetIgnoredNBTKeys();
				actions.add(action);
				action.readConfig(reader);
			}
			reader.endObject();
			((ArrayList<String>)keys).trimToSize();
			((ArrayList<BuildingGadgetIgnoredNBTKeys>)actions).trimToSize();
		}
	}

	public static class Access extends Action {
		public List<String> keys;
		public List<BuildingGadgetIgnoredNBTKeys> actions;

		@Override
		public void filter(NBTTagCompound tag, NBTTagList cost) {
			for (int i = 0; i < keys.size(); i++) {
				NBTBase map = tag.getTag(keys.get(i));
				if(map instanceof NBTTagCompound){
					actions.get(i).filter((NBTTagCompound) map, cost);
				}
			}
		}

		@Override
		public void read(JsonReader reader) throws IOException {
			keys = new ArrayList<>();
			reader.beginObject();
			while (reader.peek() == NAME) {
				keys.add(reader.nextName());
				BuildingGadgetIgnoredNBTKeys action = new BuildingGadgetIgnoredNBTKeys();
				actions.add(action);
				action.readConfig(reader);
			}
			reader.endObject();
			((ArrayList<String>)keys).trimToSize();
			((ArrayList<BuildingGadgetIgnoredNBTKeys>)actions).trimToSize();
		}
	}

	public static class MakeItem extends Action {
		static Function<NBTTagCompound, Integer> defaultCountSupplier = tag -> 1;
		static Function<NBTTagCompound, NBTTagCompound> defaultTagSupplier = tag -> new NBTTagCompound();
		Function<NBTTagCompound, String> idSupplier;
		Function<NBTTagCompound, Integer> countSupplier = defaultCountSupplier;
		Function<NBTTagCompound, NBTTagCompound> tagSupplier = defaultTagSupplier;
		@Override
		public void filter(NBTTagCompound tag, NBTTagList cost) {
			NBTTagCompound item = new NBTTagCompound();
			item.setString("id", idSupplier.apply(tag));
			item.setInteger("Count", countSupplier.apply(tag));
			item.setTag("id", tagSupplier.apply(tag));
			cost.appendTag(item);
		}

		@Override
		public void read(JsonReader reader) throws IOException {
			reader.beginObject();
			while(reader.peek() == NAME){
				switch (reader.nextName()){
					case "id":{
						String idString = reader.nextString();
						if(idString.startsWith("this.")){
							idSupplier = tag -> tag.getString(idString.substring(5));
						} else {
							idSupplier = tag -> idString;
						}
						break;
					}
					case "Count":{
						if(reader.peek() == STRING) {
							String countString = reader.nextString();
							if (countString.startsWith("this.")) {
								countSupplier = tag -> tag.getInteger(countString.substring(5));
							}
						} else {
							int count = reader.nextInt();
							countSupplier = tag -> count;
						}
						break;
					}
					case "tag":{
						String tagString = reader.nextString();
						if (tagString.startsWith("this.")) {
							tagSupplier = tag -> tag.getCompoundTag(tagString.substring(5));
						} else {
							tagSupplier = tag -> {
								try {
									return JsonToNBT.getTagFromJson(tagString);
								} catch (NBTException e) {
									throw new RuntimeException(e);
								}
							};
						}
						break;
					}
				}
			}
			reader.endObject();
		}
	}

	public static class MakeItemSimple extends Action {
		String key;

		@Override
		public void filter(NBTTagCompound tag, NBTTagList cost) {
			cost.appendTag(tag.getCompoundTag(key));
		}

		@Override
		public void read(JsonReader reader) throws IOException {
			this.key = reader.nextString();
		}
	}

	public void readConfig(JsonReader reader) throws IOException{
		this.actions = new ArrayList<>();
		reader.beginObject();
		while(reader.peek() == NAME){
			Action action;
			String type = reader.nextName();
			switch (type){
				case "ignore":
				case "ignored":{
					action = new Ignore();
					break;
				}
				case "iterate":{
					action = new Iterate();
					break;
				}
				case "access":{
					action = new Access();
					break;
				}
				case "make_item":{
					if(reader.peek() == STRING){
						action = new MakeItemSimple();
					} else {
						action = new MakeItem();
					}
					break;
				}
				default:{
					throw new IOException("Unrecognized type " + reader.nextName());
				}
			}
			actions.add(action);
			action.read(reader);
		}
		((ArrayList<Action>)actions).trimToSize();
		reader.endObject();
	}

	public static void writeDefaultConfig(JsonWriter writer) throws IOException {
		writer.beginObject();
			writer.name("ignore");
			writer.beginArray();
				writer.value("Items");
				writer.value("InvSlots");
				writer.value("inventory");
				writer.value("inv");
				writer.value("items");
				writer.value("electricityStored");
				writer.value("ifluxEnergy");
				writer.value("Energy");
				writer.value("internalCurrentPower");
				writer.value("Fluid");
				writer.value("tank");
				writer.value("fluids");
				writer.value("Amount");
				writer.value("burnTime");
				writer.value("BurnTime");
				writer.value("TotalTime");
				writer.value("def:1");
				writer.value("def:2");
				writer.value("def:3");
				writer.value("def:4");
				writer.value("def:5");
				writer.value("def:6");
				writer.value("extra:1");
				writer.value("extra:2");
				writer.value("extra:3");
				writer.value("extra:4");
				writer.value("extra:5");
				writer.value("extra:6");
			writer.endArray();
			writer.name("iterate");
			writer.beginObject();
				writer.name("Attachments");
				writer.beginObject();
					writer.name("make_item");
					writer.beginObject();
						writer.name("id");
						writer.value("thermaldynamics:servo");
						writer.name("Damage");
						writer.value("this.type");
					writer.endObject();
				writer.endObject();
				writer.name("Covers");
				writer.beginObject();
					writer.name("make_item");
					writer.beginObject();
						writer.name("id");
						writer.value("appliedenergistics2:facade");
						writer.name("tag");
						writer.beginObject();
							writer.name("damage");
							writer.value("this.damage");
							writer.name("item");
							writer.value("this.block");
						writer.endObject();
					writer.endObject();
				writer.endObject();
			writer.endObject();
			writer.name("access");
			writer.beginObject();
				writer.name("Components");
				writer.beginObject();
					writer.name("ignore");
					writer.value("fluid");
				writer.endObject();
				writer.name("TileData");
				writer.beginObject();
					writer.name("iterate");
					writer.beginObject();
						writer.name("ignore");
						writer.value("module_inventory");
					writer.endObject();
				writer.endObject();
			writer.endObject();
			writer.name("make_item");
			writer.value("upgrades");
		writer.endObject();
	}
}
