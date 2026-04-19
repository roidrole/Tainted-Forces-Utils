package roidrole.tfutils.mixins.tfc;

import net.dries007.tfc.objects.te.TEFirePit;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TEFirePit.class)
public abstract class TEFirePitMixin {
	@Redirect(
		method = "isItemValid",
		at = @At(
			value = "INVOKE",
			target = "Lnet/dries007/tfc/util/fuel/FuelManager;isItemForgeFuel(Lnet/minecraft/item/ItemStack;)Z"
		),
		remap = false
	)
	private boolean ignoreForgeFuel(ItemStack stack){
		return true;
	}
}
