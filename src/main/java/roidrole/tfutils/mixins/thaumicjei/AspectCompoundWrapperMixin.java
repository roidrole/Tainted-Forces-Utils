package roidrole.tfutils.mixins.thaumicjei;

import com.buuz135.thaumicjei.ThaumcraftJEIPlugin;
import com.buuz135.thaumicjei.category.AspectCompoundCategory;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.Arrays;
import java.util.stream.Collectors;

@Mixin(AspectCompoundCategory.AspectCompoundWrapper.class)
public abstract class AspectCompoundWrapperMixin {
	@Shadow(remap = false)
	@Final
	private Aspect aspect;

	/**
	 * @author roidrole
	 * @reason invert input and output for JEI things
	 */
	@Overwrite(remap = false)
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ThaumcraftJEIPlugin.ASPECT_LIST, new AspectList().add(this.aspect, 1));
		ingredients.setOutputs(ThaumcraftJEIPlugin.ASPECT_LIST, Arrays.stream(this.aspect.getComponents()).map(aspect1 -> new AspectList().add(aspect1, 1)).collect(Collectors.toList()));
	}

	/**
	 * @author roidrole
	 * @reason invert input and output slot
	 */
	@Overwrite(remap = false)
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glScaled(0.5, 0.5, 0.5);
		String name;
		name = TextFormatting.DARK_GRAY + aspect.getName();
		minecraft.fontRenderer.drawString(name, 32 - minecraft.fontRenderer.getStringWidth(name) / 2, 36, 0);
		name = TextFormatting.DARK_GRAY + aspect.getComponents()[0].getName();
		minecraft.fontRenderer.drawString(name, 109 - minecraft.fontRenderer.getStringWidth(name) / 2, 36, 0);
		name = TextFormatting.DARK_GRAY + aspect.getComponents()[1].getName();
		minecraft.fontRenderer.drawString(name, 181 - minecraft.fontRenderer.getStringWidth(name) / 2, 36, 0);
		GL11.glDisable(3042);
		GL11.glPopMatrix();
	}
}
