package net.abraxator.moresnifferflowers.client.gui.screen.cookbook;


import net.abraxator.moresnifferflowers.init.MSFDataAttachments;
import net.abraxator.moresnifferflowers.components.nutrition.Nutrition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

public class ItemWidget extends AbstractWidget {
    private final Nutrition.NutritionPair nutrition;
    private final CookbookScreen screen;
    private final boolean unlocked;
    
    public ItemWidget(int x, int y, Component message, Nutrition.NutritionPair nutrition, CookbookScreen screen) {
        super(x, y, 16, 16, message);
        this.nutrition = nutrition;
        this.screen = screen;
        this.unlocked = Minecraft.getInstance().player.getData(MSFDataAttachments.NUTRITION).unlockedItems().contains(nutrition.item());
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a) {
        if (unlocked) {
            guiGraphics.item(nutrition.item().getDefaultInstance(), this.getX(), this.getY());

            if (this.isHovered()) {
                screen.renderNutritionInfo(guiGraphics, nutrition);
            }
        } else {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, CookbookScreen.RENDERABLES, this.getX(), this.getY(), 156, 0, 16, 16, 256, 256);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
