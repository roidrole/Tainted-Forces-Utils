package roidrole.tfutils.mixins.magneticraft;

import com.cout970.magneticraft.misc.inventory.InventoriesKt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoriesKt.class)
public abstract class InventoriesMixin {
	@Inject(
		method = "stack(Lnet/minecraft/block/state/IBlockState;I)Lnet/minecraft/item/ItemStack;",
		at = @At(
			value = "INVOKE_ASSIGN",
			target = "Lnet/minecraftforge/fluids/IFluidBlock;getFluid()Lnet/minecraftforge/fluids/Fluid;"
		),
		cancellable = true,
		remap = false
	)
	private static void injectNullCheck(
		IBlockState $this$stack,
		int size,
		CallbackInfoReturnable<ItemStack> cir
	){
		Fluid fluid = ((IFluidBlock)$this$stack.getBlock()).getFluid();
		if(fluid == null){
			cir.setReturnValue(ItemStack.EMPTY);
		}
	}
}
