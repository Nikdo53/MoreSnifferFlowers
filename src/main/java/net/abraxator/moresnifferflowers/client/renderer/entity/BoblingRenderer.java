package net.abraxator.moresnifferflowers.client.renderer.entity;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.model.entity.BoblingModel;
import net.abraxator.moresnifferflowers.entities.BoblingEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AnimationState;

public class BoblingRenderer extends MobRenderer<BoblingEntity, BoblingRenderer.State, BoblingModel<BoblingEntity>> {
    public static final Identifier CORRUPTED_TEXTURE = MoreSnifferFlowers.loc("textures/entity/bobling/corrupted_bobling.png");
    public static final Identifier CURED_TEXTURE = MoreSnifferFlowers.loc("textures/entity/bobling/bobling.png");
    public static final Identifier BONMEELED_TEXTURE = MoreSnifferFlowers.loc("textures/entity/bobling/bonmeeled_bobling.png");
    
    public BoblingRenderer(EntityRendererProvider.Context context) {
        super(context, new BoblingModel<>(context.bakeLayer(BoblingModel.BOBLING)), 0.4F);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public Identifier getTextureLocation(State state) {
        if (!state.isCured) {
            return CORRUPTED_TEXTURE;
        } else {
            return CURED_TEXTURE;
        }
    }

    @Override
    public void extractRenderState(BoblingEntity entity, State state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isCured = entity.isCured();
        state.plantAnimationState.copyFrom(entity.plantingAnimationState);
        state.idleAnimationState.copyFrom(entity.idleAnimationState);

    }

    public static class State extends LivingEntityRenderState {
        boolean isCured;
        public final AnimationState plantAnimationState = new AnimationState();
        public final AnimationState idleAnimationState = new AnimationState();
    }
}
