package net.abraxator.moresnifferflowers.client.gui.screen.cookbook;

import net.abraxator.moresnifferflowers.components.nutrition.NutritionType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class TypeWidget extends AbstractWidget {
    final @Nullable NutritionType type;
    final private CookbookScreen screen;
    
    public TypeWidget(int x, int y, @Nullable NutritionType type, int width, int height, Component message, CookbookScreen screen) {
        super(x, y, width, height, message);
        this.type = type;
        this.screen = screen;
    }
    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int ordinal = type != null ? this.type.ordinal() : 5;
        if (isMouseOver(mouseX,mouseY)){
            graphics.blit(RenderPipelines.GUI_TEXTURED, CookbookScreen.RENDERABLES, this.getX(), this.getY(), 200, ordinal * 24, this.width, this.height, 256, 256);
        } else
            graphics.blit(RenderPipelines.GUI_TEXTURED,CookbookScreen.RENDERABLES, this.getX(), this.getY(), 176, ordinal * 24, this.width, this.height, 256, 256);

    }

    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        if (this.type == null){
            this.screen.turnPage(CookbookScreen.Page.GUIDE);
            return;
        }
        this.screen.pageToItems(this.type);
        this.screen.page = CookbookScreen.Page.ITEMS;
        screen.type =  this.type;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
