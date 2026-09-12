package net.abraxator.moresnifferflowers.mixins;

import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin extends Block implements SimpleWaterloggedBlock {

    public LeavesBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "createBlockStateDefinition", at = @At("HEAD"))
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(MSFStateProperties.NOT_CORRUPTED).add(MSFStateProperties.NOT_CURED);
    }

    @Inject(method = "updateShape", at = @At("TAIL"), cancellable = true)
    public void updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random, CallbackInfoReturnable<BlockState> cir) {
        if (MSFStateProperties.hasCustomLeavesProperties(state) && MSFStateProperties.hasCustomLeavesProperties(neighbourState)) {

            boolean isCorrupted = !neighbourState.getValue(MSFStateProperties.NOT_CORRUPTED);
            boolean isCured = !neighbourState.getValue(MSFStateProperties.NOT_CURED);

            if (isCorrupted) {
                cir.setReturnValue(state.setValue(MSFStateProperties.NOT_CORRUPTED, false));
            }

            if (isCured) {
                cir.setReturnValue(state.setValue(MSFStateProperties.NOT_CURED, false).setValue(MSFStateProperties.NOT_CORRUPTED, true));
            }

            if (!isCured && !isCorrupted) {
                cir.setReturnValue(state.setValue(MSFStateProperties.NOT_CURED, true));
            }
        }
    }

}
