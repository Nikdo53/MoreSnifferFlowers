package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.entities.CorruptedProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public interface Corruptable {
    default Optional<Block> getCorruptedBlock(Block block, RandomSource random) {
        return net.abraxator.moresnifferflowers.components.Corruptable.getCorruptedBlock(block, random);
    }
    
    default void onCorrupt(Level level, BlockPos pos, BlockState oldState, Block corruptedBlock) {
        var corruptedState = corruptedBlock.withPropertiesOf(oldState);
        level.setBlockAndUpdate(pos, corruptedState);
    }

    default void onCorruptByEntity(Entity entity, BlockPos blockPos, BlockState blockState, Block block, Level level) {
        if(entity instanceof CorruptedProjectile corruptedProjectile && net.abraxator.moresnifferflowers.components.Corruptable.canBeCorrupted(block, level)) {
            onCorrupt(level, blockPos, blockState, getCorruptedBlock(block, level.getRandom()).get());
            corruptedProjectile.remove(Entity.RemovalReason.DISCARDED);

            if(level.isClientSide()) {
                level.addParticle(new DustParticleOptions(0x36283D, 1.0F), entity.getX(), entity.getY(), entity.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }
}
