package net.abraxator.moresnifferflowers.compat.jei.corruption;

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
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class CorruptionCategory implements IRecipeCategory<CorruptionRecipe> {
    public static final IRecipeType<CorruptionRecipe> CORRUPTING = IRecipeType.create(MoreSnifferFlowers.MOD_ID, "corrupting", CorruptionRecipe.class);
    private final IDrawable icon;
    private final Component localizedName;
    
    public CorruptionCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableItemStack(MSFItems.CORRUPTED_SLIME_BALL.get().getDefaultInstance());
        this.localizedName = Component.translatableWithFallback("gui.moresnifferflowers.corrupting_category", "Corrupting");
    }

    @Override
    public IRecipeType<CorruptionRecipe> getRecipeType() {
        return CORRUPTING;
    }


    @Override
    public int getWidth() {
        return 120;
    }

    @Override
    public int getHeight() {
        return 40;
    }


    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CorruptionRecipe recipe, IFocusGroup iFocusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 15).add(recipe.source());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 92, 15).add(recipe.corrupted()).addRichTooltipCallback((recipeSlotView, tooltip) -> {
            if (recipe.corrupted().isEmpty()) {
                tooltip.add(Component.literal("Air").withStyle(ChatFormatting.WHITE));
            }
        });
    }

    @Override
    public void draw(CorruptionRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, MoreSnifferFlowers.loc("textures/gui/container/corrupting_jei.png"), 0,0, 0 ,0 ,getWidth() ,getHeight(), 256, 256);

        Minecraft minecraft = Minecraft.getInstance();
        String text = "Chance: " + recipe.chance() + "%";
        int width = minecraft.font.width(text);
        int x = getWidth() - 30 - width;
        int y = 0;
        guiGraphics.text(minecraft.font, text, x, y, 0x714a5f, false);

    }

}
