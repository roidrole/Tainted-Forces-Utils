package roidrole.tfutils.mixins.cnpcs;

import noppes.npcs.CustomNpcs;
import noppes.npcs.controllers.ServerCloneController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.File;

@Mixin(ServerCloneController.class)
public abstract class ServerCloneControllerMixin {
	/**
	 * @author roidrole
	 * @reason store the clones outside the world folder
	 */
	@Overwrite(remap = false)
	public File getDir(){
		File out = new File(CustomNpcs.MODID, "clones_bin");
		if(!out.exists()){
			out.mkdir();
		}
		return out;
	}
}
