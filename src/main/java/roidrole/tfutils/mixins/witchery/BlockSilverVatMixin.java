package roidrole.tfutils.mixins.witchery;

import net.minecraft.item.Item;
import net.msrandom.witchery.block.BlockSilverVat;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockSilverVat.class)
public abstract class BlockSilverVatMixin {
	@Redirect(
		method = "onNeighborChange",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/init/Items;GOLD_INGOT:Lnet/minecraft/item/Item;",
			opcode = Opcodes.GETSTATIC
		)
	)
	private Item useTFCGold(){
		return Item.getByNameOrId("tfc:metal/ingot/gold");
	}

	@Redirect(
		method = "<clinit>",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/init/Items;GOLD_INGOT:Lnet/minecraft/item/Item;",
			opcode = Opcodes.GETSTATIC
		)
	)
	private static Item useTFCGold2(){
		return Item.getByNameOrId("tfc:metal/ingot/gold");
	}
}
