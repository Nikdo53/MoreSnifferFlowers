package net.abraxator.moresnifferflowers.compat.jei.rebrewing;

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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

public class RebrewingCategory implements IRecipeCategory<JeiRebrewingRecipe> {
    public static final IRecipeType<JeiRebrewingRecipe> REBREWING = IRecipeType.create(MoreSnifferFlowers.MOD_ID, "rebrewing", JeiRebrewingRecipe.class);
    private final IDrawable icon;
    private final Component localizedName;

    public RebrewingCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemStack(MSFItems.REBREWING_STAND.get().getDefaultInstance());
        this.localizedName = Component.translatableWithFallback("gui.moresnifferflowers.rebrewing_category", "Rebrewing");
    }

    @Override
    public IRecipeType<JeiRebrewingRecipe> getRecipeType() {
        return REBREWING;
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
        return 72;
    }


    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, JeiRebrewingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 40).add(MSFItems.CROPRESSED_NETHERWART.get().getDefaultInstance());
        builder.addSlot(RecipeIngredientRole.INPUT, 62, 36).add(recipe.extractedPotion());
        builder.addSlot(RecipeIngredientRole.INPUT, 90, 36).add(recipe.ingredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 133, 36).add(recipe.rebrewedPotion());
    }


    @Override
    public void draw(JeiRebrewingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, MoreSnifferFlowers.loc("textures/gui/container/rebrewing_jei.png"), 0,0, 0 ,0 ,getWidth() ,getHeight(), 256, 256);
        Minecraft minecraft = Minecraft.getInstance();
        String text = "4";
        int width = minecraft.font.width(text);
        int x = getWidth() - 140 - width;
        int y = 16;
        guiGraphics.text(minecraft.font, text, x, y, 0xa42429, false);
    }
}
