package roidrole.tfutils.mixins;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Collections;
import java.util.List;

public class TFUtilsLateMixinLoader implements ILateMixinLoader {
	@Override
	public List<String> getMixinConfigs() {
		return Collections.singletonList("mixins.tfutils.json");
	}
}
