package net.abraxator.moresnifferflowers.blocks.corrupted;

import com.mojang.serialization.MapCodec;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CorruptedWartBlock extends BushBlock {
    public CorruptedWartBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<BushBlock> codec() {
        return null;
    }

    private static final VoxelShape SHAPE = Block.box(4, 0,  4, 12, 5, 12);

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        explode(pos, level);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos blockPos) {
        return level.getBlockState(blockPos.below()).is(MSFBlocks.CORRUPTED_GRASS_BLOCK.get());
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (!canSurvive(state, level, pos) && level instanceof LevelAccessor levelAccessor) {
            boolean drop = !level.getBlockState(pos.below()).is(MSFBlocks.CURED_GRASS_BLOCK.get());
            levelAccessor.destroyBlock(pos, drop);
        }
        return state;
    }

    public void explode(BlockPos pos, Level level){
        level.destroyBlock(pos, true);
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(MSFBlocks.CORRUPTED_GRASS_BLOCK.get());
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(pos);
        return SHAPE.move(vec3.x, 0, vec3.z);
    }


}
