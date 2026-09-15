package net.abraxator.moresnifferflowers.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IMSFBlockExtension {
    default void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {

    }

}
