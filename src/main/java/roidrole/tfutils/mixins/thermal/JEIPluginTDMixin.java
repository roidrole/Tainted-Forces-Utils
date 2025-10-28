package roidrole.tfutils.mixins.thermal;

import cofh.thermaldynamics.plugins.jei.JEIPluginTD;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

//Taken from E2EE mixins
@Mixin(JEIPluginTD.class)
public class JEIPluginTDMixin {
	/**
	 * @author ZZZank
	 * @reason Skip cover recipe category initialization
	 */
    @Overwrite(remap = false)
	public void registerCategories(IRecipeCategoryRegistration registry) {
		//NO-OP
	}

	/**
	 * @author ZZZank
	 * @reason Skip cover recipe category initialization
	 */
    @Overwrite(remap = false)
	public void register(IModRegistry registry) {
		//NO-OP
	}

	/**
	 * @author ZZZank
	 * @reason We don't need to differentiate covers since we removed cover recipe category
	 */
    @Overwrite(remap = false)
	public void registerItemSubtypes(ISubtypeRegistry registry) {
		//NO-OP
	}
}

