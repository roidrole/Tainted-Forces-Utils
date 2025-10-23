package roidrole.tfutils.mixins.extrautils;

import com.rwtema.extrautils2.api.machine.Machine;
import com.rwtema.extrautils2.machine.MachineInit;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MachineInit.class)
public class MachineInitMixin {
	//Ok, so the XU2 crusher loops through the oredict every time the addition is loaded. This takes quite a lot of time.
	//During TFCTweaker postInit alone, this listener alone takes 9.3 seconds of the total 9.5
	//Since the crusher listener is the only registered one, we can safely remove the ability to listen
	@Redirect(
		method = "init",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraftforge/fml/common/eventhandler/EventBus;register(Ljava/lang/Object;)V"
		),
		remap = false
	)
	private static void stopListening(EventBus instance, Object eventType){
		//No-op
	}

	//Since we're nuking the crusher, might as well not register it
	//Somehow, there are two methods that register machines...
	@Redirect(
		method = {"init", "register"},
		at = @At(
			value = "INVOKE",
			target = "Lcom/rwtema/extrautils2/api/machine/MachineRegistry;register(Lcom/rwtema/extrautils2/api/machine/Machine;)Lcom/rwtema/extrautils2/api/machine/Machine;",
			ordinal = 2
		),
		remap = false
	)
	private static Machine noCrusher(Machine machine){
		return null;
	}

	//Crusher recipes are registered last. That means I can get lazy
	@Inject(
		method = "addMachineRecipes",
		at = @At(
			value = "INVOKE",
			target = "Lcom/rwtema/extrautils2/api/machine/XUMachineCrusher;addRecipe(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;F)V"
		),
		remap = false,
		cancellable = true
	)
	private static void noCrusherRecipes(CallbackInfo ci){
		ci.cancel();
	}
}
