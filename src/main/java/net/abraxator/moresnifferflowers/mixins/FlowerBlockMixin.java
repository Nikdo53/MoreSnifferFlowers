package net.abraxator.moresnifferflowers.mixins;

import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.abraxator.moresnifferflowers.init.config.MSFServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FlowerBlock.class)
public abstract class FlowerBlockMixin extends BushBlock implements SuspiciousEffectHolder {
    public FlowerBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.is(Blocks.TORCHFLOWER) || super.isRandomlyTicking(state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        if (!state.is(Blocks.TORCHFLOWER) || !MSFServerConfig.TORCHFLOWER_CONVERSION.get()) return;

        boolean isWaterUnderneath = level.getFluidState(pos.below(2)).is(FluidTags.WATER);

        if (level.getBrightness(LightLayer.SKY, pos) > 13 && !level.isDarkOutside() && !level.isRaining() && !isWaterUnderneath) {
            level.setBlock(pos, MSFBlocks.TORCHFLOWER_AFLAME.get().defaultBlockState().setValue(MSFStateProperties.AGE_2, 1).setValue(MSFStateProperties.FIRE_TICKS, 0), 3);
        }
    }
}
