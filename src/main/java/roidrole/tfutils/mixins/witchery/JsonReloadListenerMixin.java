package roidrole.tfutils.mixins.witchery;

import net.minecraft.resources.JsonReloadListener;
import net.minecraft.resources.Resource;
import net.minecraft.resources.ResourceManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(JsonReloadListener.class)
public abstract class JsonReloadListenerMixin {
	@Unique
	private static final String tfutils_root = "config/witchery/data/";

	@Redirect(
		method = "prepare(Lnet/minecraft/resources/ResourceManager;)Ljava/util/Map;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/resources/ResourceManager;findResources(Ljava/lang/String;Ljava/util/function/Predicate;)Ljava/util/Collection;"
		),
		remap = false
	)
	private Collection<ResourceLocation> scanConfigFolder(ResourceManager instance, String resourceType, Predicate<String> pathPredicate){
		try(Stream<Path> paths = Files.walk(Paths.get(tfutils_root))){
			return paths
				.map(Objects::toString)
				.filter(path -> path.endsWith(".json"))
				.map(path -> path.substring(tfutils_root.length()))
				.map(path -> path.split("/", 3))
				.filter(path -> path[1].equals(resourceType))
				.map(path -> new ResourceLocation(path[0], path[1] + '/' + path[2]))
				.collect(Collectors.toList());
		} catch (IOException e) {
			return Collections.emptyList();
		}
	}

	@Redirect(
		method = "prepare(Lnet/minecraft/resources/ResourceManager;)Ljava/util/Map;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/resources/ResourceManager;getResource(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/resources/Resource;"
		),
		remap = false
	)
	private static Resource useConfigFolder(ResourceManager instance, ResourceLocation id){
		try {
			return new Resource(Files.newInputStream(Paths.get(tfutils_root, id.getNamespace(), id.getPath())));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
