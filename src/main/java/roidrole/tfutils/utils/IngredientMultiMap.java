package roidrole.tfutils.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.*;

public class IngredientMultiMap<V> {
	private final Map<Item, Node<V>> itemMap = new HashMap<>();

	public void collect(ItemStack stack, Collection<V> insertInto){
		Node<V> value = itemMap.get(stack.getItem());
		if(value == null){
			return;
		}
		for (int i = 0; i < value.keys.size(); i++) {
			if(value.keys.get(i).test(stack)){
				insertInto.add(value.values.get(i));
			}
		}
	}

	public void put(Object key, V value) {
		Ingredient ingredient = CraftingHelper.getIngredient(key);
		if(ingredient == null){
			return;
		}
		ItemStack[] matchingStacks = ingredient.getMatchingStacks();
		for(ItemStack stack : matchingStacks){
			itemMap.computeIfAbsent(stack.getItem(), (k) -> new Node<>()).put(ingredient, value);
		}
	}

	private static class Node<V> {
		List<Ingredient> keys = new ArrayList<>(1);
		List<V> values = new ArrayList<>(1);

		public void put(Ingredient key, V value){
			keys.add(key);
			values.add(value);
		}
	}
}
