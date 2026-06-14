package roidrole.tfutils.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import net.minecraft.entity.Entity;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenExpansion("crafttweaker.entity.IEntity")
@ZenRegister
@SuppressWarnings("unused")
public class IEntityExpansion {
	@ZenMethod
	public static Class<? extends Entity> getClass(IEntity entity){
		return ((Entity) entity.getInternal()).getClass();
	}
}
