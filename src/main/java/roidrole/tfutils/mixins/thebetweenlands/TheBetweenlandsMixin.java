package roidrole.tfutils.mixins.thebetweenlands;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.TheBetweenlands;

@Mixin(TheBetweenlands.class)
public abstract class TheBetweenlandsMixin {
	/**
	 * @author roidrole
	 * @reason taking over event handler registration to move some away from events
	 */
	@Overwrite(remap = false)
	private void registerEventHandlers(){
		//NO-OP
	}
}
