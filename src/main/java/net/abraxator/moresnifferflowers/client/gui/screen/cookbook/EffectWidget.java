package net.abraxator.moresnifferflowers.client.gui.screen.cookbook;

import net.abraxator.moresnifferflowers.capability.NutritionCapability;
import net.abraxator.moresnifferflowers.init.MSFDataAttachments;
import net.abraxator.moresnifferflowers.components.nutrition.NutritionType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.ARGB;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;

public class EffectWidget extends AbstractWidget {
    private final NutritionType nutrition;
    private final CookbookScreen screen;
    private final boolean isPositive;

    public EffectWidget(int x, int y, Component message, NutritionType nutrition, CookbookScreen screen, boolean isPositive) {
        super(x, y, 20 , 20, message);
        this.nutrition = nutrition;
        this.screen = screen;
        this.isPositive = isPositive;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a) {
        int id = NutritionCapability.idFromNutrition(nutrition, isPositive);

        NutritionCapability capability = Minecraft.getInstance().player.getData(MSFDataAttachments.NUTRITION);
        boolean isUnlocked = capability.unlockedEffects().contains(id);
        int size = isUnlocked ? 18 : 21;

        Holder<MobEffect> effect = NutritionCapability.effectFromId(id);
        if (isUnlocked) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Gui.getMobEffectSprite(NutritionCapability.effectFromId(id)), getX() + 3, getY() + 3, 18, 18, ARGB.white(alpha));
        } else {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, CookbookScreen.RENDERABLES, getX(), getY(), 0, 32, size, size, 256, 256);
        }

        guiGraphics.blit(CookbookScreen.RENDERABLES, getX() - 10, getY() - 2, 0, 160, 118, 22, 256, 256);

        String text = isPositive ? Component.translatable("gui.moresnifferflowers.cookbook.positive").getString(): Component.translatable("gui.moresnifferflowers.cookbook.negative").getString();
        int color = isPositive ? 0x67911c : 0x8d2a22;

        guiGraphics.textWithWordWrap(screen.getMinecraft().font, FormattedText.of(text, Style.EMPTY.withBold(true).withUnderlined(true)), getX() + 35, getY() + 4, 100, color);

        if (isHovered && isUnlocked) {
            screen.renderEffectInfo(guiGraphics, effect.value(), isPositive);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
