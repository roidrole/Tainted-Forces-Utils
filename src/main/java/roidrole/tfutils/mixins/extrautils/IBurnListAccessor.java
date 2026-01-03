package roidrole.tfutils.mixins.extrautils;

import com.rwtema.extrautils2.crafting.BurnList;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(BurnList.class)
public interface IBurnListAccessor {
	@Accessor(remap = false)
	static void setStacks(List<ItemStack> stacks) {}
}
