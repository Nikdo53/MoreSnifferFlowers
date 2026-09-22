package net.abraxator.moresnifferflowers.client.renderer.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public abstract class MSFBERenderer<T extends BlockEntity, S extends MSFBERenderState> implements BlockEntityRenderer<T,S> {
    protected final SpriteGetter sprites;
    protected final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    protected final BlockModelResolver blockModelResolver;
    protected final ItemModelResolver itemModelResolver;
    protected final EntityRenderDispatcher entityRenderer;
    protected final EntityModelSet entityModelSet;
    protected final Font font;
    protected final PlayerSkinRenderCache playerSkinRenderCache;

    protected final Minecraft minecraft = Minecraft.getInstance();

    public MSFBERenderer(BlockEntityRendererProvider.Context context) {
        this.sprites = context.sprites();
        this.blockEntityRenderDispatcher = context.blockEntityRenderDispatcher();
        this.blockModelResolver = context.blockModelResolver();
        this.itemModelResolver = context.itemModelResolver();
        this.entityRenderer = context.entityRenderer();
        this.entityModelSet = context.entityModelSet();
        this.font = context.font();
        this.playerSkinRenderCache = context.playerSkinRenderCache();
    }

    public RandomSource getRandom(){
        return getLevel().getRandom();
    }

    @Override
    public void extractRenderState(T blockEntity, S state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.partialTicks = partialTicks;
    }

    public MovingBlockRenderState getMovingBlockRenderState(BlockPos pos, BlockState blockState){
        MovingBlockRenderState movingBlockRenderState = new MovingBlockRenderState();
        movingBlockRenderState.randomSeedPos = pos;
        movingBlockRenderState.blockPos = pos;
        movingBlockRenderState.blockState = blockState;
        movingBlockRenderState.biome = null;
        movingBlockRenderState.cardinalLighting = getLevel().cardinalLighting();
        movingBlockRenderState.lightEngine = getLevel().getLightEngine();
        return movingBlockRenderState;
    }

    public int overlay(){
        return OverlayTexture.NO_OVERLAY;
    }

    public ClientLevel getLevel(){
        
        ClientLevel level = minecraft.level;
        if (level == null)
            throw new IllegalStateException("Tried rendering a block entity in a null world!");
        return level;
    }

    public LocalPlayer getPlayer(){
        LocalPlayer player = minecraft.player;
        if (player == null)
            throw new IllegalStateException("Tried rendering a block entity without a player!");
        return player;
    }

}
