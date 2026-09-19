package net.abraxator.moresnifferflowers.mixins.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.client.gui.screens.inventory.HangingSignEditScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HangingSignEditScreen.class)
public abstract class SignEditScreenMixin extends AbstractSignEditScreen{
    @Shadow
    @Final
    private Identifier texture;

    public SignEditScreenMixin(SignBlockEntity sign, boolean isFrontText, boolean shouldFilter) {
        super(sign, isFrontText, shouldFilter);
    }

    @WrapOperation(method = "extractSignBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIII)V"))
    private void colorVivicusWrap(GuiGraphicsExtractor instance, RenderPipeline renderPipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, Operation<Void> original) {
        BlockState state = sign.getBlockState();
        if (state.is(MSFBlocks.VIVICUS_SIGN.get()) || state.is(MSFBlocks.VIVICUS_WALL_SIGN.get()) || state.is(MSFBlocks.VIVICUS_HANGING_SIGN.get()) || state.is(MSFBlocks.VIVICUS_WALL_HANGING_SIGN.get())) {
            int color = ((ColorableVivicusBlock) state.getBlock()).colorValues().get(state.getValue(MSFStateProperties.COLOR));

            instance.blit(RenderPipelines.GUI_TEXTURED, this.texture, -8, -8, 0.0F, 0.0F, 16, 16, 16, 16, color);
        } else
            original.call(instance, renderPipeline, texture, x, y, u, v, width, height, textureWidth, textureHeight);
    }

}
