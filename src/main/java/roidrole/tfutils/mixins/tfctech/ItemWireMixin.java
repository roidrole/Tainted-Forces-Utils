package roidrole.tfutils.mixins.tfctech;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import tfctech.objects.items.metal.ItemWire;

@Mixin(ItemWire.class)
public abstract class ItemWireMixin extends Item {
	@Override
	public int getMaxDamage() {
		return 4;
	}
}
