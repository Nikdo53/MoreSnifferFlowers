package net.abraxator.moresnifferflowers.client.gui.screen;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class ClientDyespriaTooltip implements ClientTooltipComponent {
    public static final Identifier TEXTURE = MoreSnifferFlowers.loc("textures/gui/dyespria_tooltip.png");

    ItemStack stack;
    boolean isPatternspria;
    int dyespriaMode;

    public ClientDyespriaTooltip(DyespriaTooltip dyespriaTooltip) {
        stack = dyespriaTooltip.stack;
        isPatternspria = dyespriaTooltip.isPatternspria;
        dyespriaMode = dyespriaTooltip.dyespriaMode;
    }

    @Override
    public int getHeight(Font font) {
        return 26;
    }

    @Override
    public int getWidth(Font font) {
        return 48;
    }

    @Override
    public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x -1, y -1, 0, 0, 24, 24, 256, 256);

        int modeOffset = 64 + 16 * dyespriaMode;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 32, y + 3, modeOffset, 0, 16, 16, 256, 256);

        int stackXOffset = x + 3;
        int stackYOffset = y + 3;

        if (stack.isEmpty()) {
            int vOffset = isPatternspria ? 48 : 32;
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, stackXOffset, stackYOffset, vOffset, 0, 16, 16, 256, 256);
        } else {
            graphics.item(stack, stackXOffset, stackYOffset);
            graphics.itemDecorations(font, stack, stackXOffset, stackYOffset, String.valueOf(stack.getCount()));
        };
    }
}
