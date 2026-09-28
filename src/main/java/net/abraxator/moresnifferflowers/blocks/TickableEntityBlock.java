package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.IMSFBlockEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.nikdo53.tinymultiblocklib.block.BaseMultiblock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TickableEntityBlock extends EntityBlock {
    @Nullable
    default <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return canTick(level,state) ? createTickerHelper() : null;
    }

    private <T extends BlockEntity> @NotNull BlockEntityTicker<T> createTickerHelper() {
        return (lvl, pos, state, blockEntity) -> {
            if (!state.is((Block) this)){
                MoreSnifferFlowers.LOGGER.warn("BlockEntity {} at {} is on an invalid blockstate {}", blockEntity, pos, state);
                return;
            }
            if (blockEntity instanceof IMSFBlockEntity imsfBlockEntity) {
                imsfBlockEntity.tick(lvl, pos, state);
                if (lvl instanceof ServerLevel serverLevel) {
                    imsfBlockEntity.serverTick(serverLevel, pos, state);
                } else {
                    imsfBlockEntity.clientTick(lvl, pos, state);
                }
            }
        };
    }

    default boolean canTick(Level level, BlockState state) {
        if (state.hasProperty(BaseMultiblock.CENTER)) {
            return state.getValue(BaseMultiblock.CENTER);
        }
        return true;
    }
}
