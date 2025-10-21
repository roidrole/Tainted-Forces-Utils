package roidrole.tfutils.mixins.tfctech;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tfctech.objects.items.metal.ItemTechMetal;

@Mixin(ItemTechMetal.ItemType.class)
public class ItemTypeMixin {
	@Mutable
	@Final
	@Shadow(remap = false)
	private int smeltAmount;

	@Redirect(
		method = "<init>(Ljava/lang/String;IIZLjava/util/function/BiFunction;)V",
		at = @At(
			value = "FIELD",
			target = "Ltfctech/objects/items/metal/ItemTechMetal$ItemType;smeltAmount:I"
		),
		remap = false
	)
	void modifyMetalAmount(ItemTechMetal.ItemType instance, int value){
		switch (instance.name()){
			case "STRIP":
			case "GROOVE":
			case "WIRE":
				this.smeltAmount = (int) (value * 0.72);
				break;
			default:
				this.smeltAmount = (int) (value * 1.44);
		}
	}
}
