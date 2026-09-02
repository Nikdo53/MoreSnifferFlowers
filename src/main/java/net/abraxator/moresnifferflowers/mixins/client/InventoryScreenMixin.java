package net.abraxator.moresnifferflowers.mixins.client;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.effects.HardenedMouthEffect;
import net.abraxator.moresnifferflowers.init.MSFDataAttachments;
import net.abraxator.moresnifferflowers.init.config.MSFClientConfig;
import net.abraxator.moresnifferflowers.init.MSFEffects;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
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
public abstract class InventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> implements RecipeUpdateListener {
    @Unique
    private static final Identifier TEXTURE_LOCATION = MoreSnifferFlowers.loc("textures/gui/container/hardened_mouth.png");

    public InventoryScreenMixin(InventoryMenu menu, Inventory playerInventory, Component title, int moreSnifferFlowers$mouthSlotX, int moreSnifferFlowers$mouthSlotY) {
        super(menu, playerInventory, title);
    }

    @Inject(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/Identifier;IIIIII)V"))
    public void render(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        assert this.minecraft != null;
        Player player = this.minecraft.player;
        if (player == null) return;
        if (player.hasEffect(MSFEffects.HARDENED_MOUTH)){

            int x = this.leftPos + MSFClientConfig.HARDENED_MOUTH_X.get();
            int y = this.topPos + MSFClientConfig.HARDENED_MOUTH_Y.get();
            guiGraphics.blit(TEXTURE_LOCATION, x, y, 0, 0, 24, 60);



            float maxCooldown = HardenedMouthEffect.getMaxCooldown(Objects.requireNonNull(player.getEffect(MSFEffects.HARDENED_MOUTH)).getAmplifier());
            float cooldown = player.getData(MSFDataAttachments.HARDENED_MOUTH_COOLDOWN.get());
            int height = Math.round(14F - (14F * (cooldown / maxCooldown)));

            guiGraphics.blit(TEXTURE_LOCATION, x + 5, y + 23 + 14 - height , 32, 14 - height, 14, height);

        }
    }
}
