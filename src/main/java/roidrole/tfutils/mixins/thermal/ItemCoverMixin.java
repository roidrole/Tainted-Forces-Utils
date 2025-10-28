package roidrole.tfutils.mixins.thermal;

import cofh.thermaldynamics.item.ItemCover;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Collections;
import java.util.List;

@Mixin(ItemCover.class)
public class ItemCoverMixin{
	/**
	 * @author roidrole
	 * @reason stop registering covers
	 */
	@Overwrite(remap = false)
	public boolean preInit(){
		return true;
	}

	/**
	 * @author roidrole
	 * @reason Remove Thermal Dynamics covers
	 */
	@Overwrite(remap = false)
	public static List<ItemStack> getCoverList() {
		return Collections.emptyList();
	}
}
