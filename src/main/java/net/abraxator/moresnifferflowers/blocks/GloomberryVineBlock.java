package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.init.MSFItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class GloomberryVineBlock extends DawnberryVineBlock {
    public GloomberryVineBlock(Properties properties) {
        super(properties, true);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        if(entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 30, 0));
        }
    }

    @Override
    protected InteractionResult dropAgeThreeLoot(BlockState blockState, Level level, BlockPos pos, Player player) {
        final ItemStack DAWNBERRY = new ItemStack(MSFItems.GLOOMBERRY.get());
        popResource(level, pos, DAWNBERRY);
        level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
        BlockState state = blockState.setValue(AGE, 2);
        level.setBlock(pos, state, 2);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
        return InteractionResult.SUCCESS;
    }
}
