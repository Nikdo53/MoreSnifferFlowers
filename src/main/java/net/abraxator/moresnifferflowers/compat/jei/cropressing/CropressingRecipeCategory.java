package net.abraxator.moresnifferflowers.compat.jei.cropressing;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.init.MSFRecipes;
import net.abraxator.moresnifferflowers.recipes.CropressingRecipe;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;

public class CropressingRecipeCategory implements IRecipeCategory<RecipeHolder<CropressingRecipe>> {
    public static final IRecipeType<RecipeHolder<CropressingRecipe>> CROPRESSING = IRecipeType.create(MSFRecipes.Types.CROPRESSING.get());
    private final IDrawable icon;
    private final Component localizedName;

    public CropressingRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableItemStack(MSFItems.CROPRESSOR.get().getDefaultInstance());
        this.localizedName = Component.translatableWithFallback("gui.mores_sniffer_flowers.cropressing_category", "Cropressing");
    }

    @Override
    public IRecipeType<RecipeHolder<CropressingRecipe>> getRecipeType() {
        return CROPRESSING;
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
    }

    @Override
    public int getWidth() {
        return 176;
    }

    @Override
    public int getHeight() {
        return 84;
    }

    @Override
    public void draw(RecipeHolder<CropressingRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, MoreSnifferFlowers.loc("textures/gui/container/cropressor_gui.png"), 0,0, 0 ,0 ,getWidth() ,getHeight(), 256, 256);
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CropressingRecipe> recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 20, 34).add(recipe.value().ingredient().getValues().get(0).value().getDefaultInstance().copyWithCount(recipe.value().count()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 140, 34).add(recipe.value().result());
    }
}
