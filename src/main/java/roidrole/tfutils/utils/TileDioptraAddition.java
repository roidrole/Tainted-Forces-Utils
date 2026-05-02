package roidrole.tfutils.utils;

import org.spongepowered.asm.mixin.Unique;

import java.util.Collection;
import java.util.UUID;

public interface TileDioptraAddition {
	@Unique
	Collection<UUID> tfutils_getPlayersToSync();
}
