package roidrole.tfutils.mixins.railcraft;

import mods.railcraft.common.items.ItemTie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ItemTie.class)
public abstract class ItemTieMixin {
	/**
	 * @author roidrole
	 * @reason This operation costs 400 ms
	 */
	@Overwrite(remap = false)
	public void defineRecipes(){
		//NO-OP
	}
}
