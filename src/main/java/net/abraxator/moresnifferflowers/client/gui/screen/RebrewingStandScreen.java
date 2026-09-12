package net.abraxator.moresnifferflowers.client.gui.screen;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.MoreSnifferFlowersClient;
import net.abraxator.moresnifferflowers.client.gui.menu.RebrewingStandMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class RebrewingStandScreen extends AbstractContainerScreen<RebrewingStandMenu> {
    public static final Identifier TEXTURE = MoreSnifferFlowers.loc("textures/gui/container/rebrewing_stand.png");
    private static final int[] BUBBLELENGTHS = new int[]{0, 5, 8, 12, 17, 22, 27};
    
    public RebrewingStandScreen(RebrewingStandMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, a);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        this.renderOnboardingTooltips(guiGraphics, mouseX, mouseY, x, y);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a) {
        super.extractBackground(guiGraphics, mouseX, mouseY, a);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        int fuel = menu.getFuel();
        int progress = menu.getBrewingTicks();
        int renderFuel;

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
        if(menu.getCost() <= 16) {
            var cost = String.valueOf(menu.getCost());
            var color = MoreSnifferFlowersClient.isBoringLoaded() ? 0x00c6c6c6 : 0x00933c4d;
            var colorOutline = MoreSnifferFlowersClient.isBoringLoaded() ? 0x00373737 : 0x005e224f;
            drawCost(guiGraphics, cost, x, y, colorOutline, -1, 0);
            drawCost(guiGraphics, cost, x, y, colorOutline, +1, 0);
            drawCost(guiGraphics, cost, x, y, colorOutline, 0, -1);
            drawCost(guiGraphics, cost, x, y, colorOutline, 0, +1);
            drawCost(guiGraphics, cost, x, y, color, 0, 0);
        } else {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,TEXTURE, x + 30, y + 45, 197, 0, 19, 11, 256, 256);
        }

        if(fuel > 0) {
            renderFuel = -(fuel * 2);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,TEXTURE, x + 57, y + 42, 209, 40, renderFuel, -11, 256, 256);
        }

        if(progress > 0) {
            int arrowScale = (int) Mth.lerp((float) progress / 100, 0, 27);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,TEXTURE, x + 124, y + 18, 177, 1, 8, arrowScale, 256, 256);

            var bubbleFactor = BUBBLELENGTHS[progress / 2 % 7];
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,TEXTURE, x + 58, y + 37 - bubbleFactor, 186, 28 - bubbleFactor, 11, bubbleFactor, 256, 256);
        }

    }

    public void renderOnboardingTooltips(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int x, int y) {
        Optional<Component> optional = Optional.empty();
        
        if(isMouseOver(mouseX, mouseY, x + 24, y + 31, 33, 11)) {
            guiGraphics.setTooltipForNextFrame(this.font, this.font.split(Component.literal(menu.getFuel() + "/16"), 115), mouseX, mouseY, null);
        }
        
        if(hoveredSlot != null && !this.hoveredSlot.hasItem()) {
            switch (hoveredSlot.index) {
                case 0 -> optional = Optional.of(component("fuel", "Add Cropressed Nether Wart"));
                case 1 -> optional = Optional.of(component("og_potion", "Add Extracted Potion"));
                case 2 -> optional = Optional.of(component("ingredient", "Add Ingredient"));
                case 3, 5, 4 -> optional = Optional.of(component("potion", "Add Water Bottle"));
                default -> {
                    return;
                }
            }
        }
        
        optional.ifPresent(component -> guiGraphics.setTooltipForNextFrame(this.font, this.font.split(component, 115), mouseX, mouseY));
    }
    
    private void drawCost(GuiGraphicsExtractor guiGraphics, String cost, int x, int y, int color, int xOffset, int yOffset) {
        guiGraphics.text(this.font, cost, (x + 40 - this.font.width(cost) / 2) + xOffset, (y + 46) + yOffset, color);
     //   this.font.drawInBatch(cost, (x + 40 - this.font.width(cost) / 2) + xOffset, (y + 46) + yOffset, color, false, guiGraphics., guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880, this.font.isBidirectional());
    }
    
    private Component component(String id, String fallback) {
        return Component.translatableWithFallback("tooltip.moresnifferflowers.rebrewing_stand." + id, fallback);
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int sizeX, int sizeY) {
        return (mouseX >= x && mouseX <= x + sizeX) && (mouseY >= y && mouseY <= y + sizeY);
    }
    
}
