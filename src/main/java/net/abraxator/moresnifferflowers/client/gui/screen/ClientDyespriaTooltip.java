package net.abraxator.moresnifferflowers.client.gui.screen;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

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
    public int getHeight() {
        return 26;
    }

    @Override
    public int getWidth(Font font) {
        return 48;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        guiGraphics.blit(TEXTURE, x -1, y -1, 0, 0, 24, 24);

        int modeOffset = 64 + 16 * dyespriaMode;
        guiGraphics.blit(TEXTURE, x + 32, y + 3, modeOffset, 0, 16, 16);

        int stackXOffset = x + 3;
        int stackYOffset = y + 3;

        if (stack.isEmpty()) {
            int vOffset = isPatternspria ? 48 : 32;
            guiGraphics.blit(TEXTURE, stackXOffset, stackYOffset, vOffset, 0, 16, 16);
        } else {
            guiGraphics.renderItem(stack, stackXOffset, stackYOffset);
            guiGraphics.renderItemDecorations(font, stack, stackXOffset, stackYOffset, String.valueOf(stack.getCount()));
        }
    }
}
