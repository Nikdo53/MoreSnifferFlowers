package net.abraxator.moresnifferflowers.mixins.client;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.effects.HardenedMouthEffect;
import net.abraxator.moresnifferflowers.init.MSFDataAttachments;
import net.abraxator.moresnifferflowers.init.MSFEffects;
import net.abraxator.moresnifferflowers.init.config.MSFClientConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractRecipeBookScreen<InventoryMenu> {
    @Unique
    private static final Identifier TEXTURE_LOCATION = MoreSnifferFlowers.loc("textures/gui/container/hardened_mouth.png");

    public InventoryScreenMixin(InventoryMenu menu, RecipeBookComponent<?> recipeBookComponent, Inventory inventory, Component title) {
        super(menu, recipeBookComponent, inventory, title);
    }


    @Inject(method = "extractBackground", at = @At(value = "TAIL"))
    public void render(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        Player player = this.minecraft.player;
        if (player == null) return;
        if (player.hasEffect(MSFEffects.HARDENED_MOUTH)){

            int x = this.leftPos + MSFClientConfig.HARDENED_MOUTH_X.get();
            int y = this.topPos + MSFClientConfig.HARDENED_MOUTH_Y.get();
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE_LOCATION, x, y, 0, 0, 24, 60, 256, 256);



            float maxCooldown = HardenedMouthEffect.getMaxCooldown(Objects.requireNonNull(player.getEffect(MSFEffects.HARDENED_MOUTH)).getAmplifier());
            float cooldown = player.getData(MSFDataAttachments.HARDENED_MOUTH_COOLDOWN.get());
            int height = Math.round(14F - (14F * (cooldown / maxCooldown)));
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE_LOCATION, x + 5, y + 23 + 14 - height , 32, 14 - height, 14, height, 256, 256);

        }
    }
}
