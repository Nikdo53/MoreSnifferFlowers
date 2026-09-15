package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.components.BlockPattern;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import static net.abraxator.moresnifferflowers.init.MSFStateProperties.*;

public class PatternflowerBlock extends CaulorflowerBlock implements BonemealableBlock, MSFCropBlock {

    public PatternflowerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(FLIPPED, true)
                .setValue(getAgeProperty(), 0)
                .setValue(MSFStateProperties.BLOCK_PATTERN, BlockPattern.EMPTY)
                .setValue(EMPTY, true)
                .setValue(SHEARED, false));

    }

    @Override
    public boolean canBeColored(BlockState blockState, Dye dye) {
        return false;
    }

    @Override
    public boolean isCorrupted() {
        return true;
    }

    @Override
    public void popResource(Level level, BlockPos pos, BlockState state) {
        popResource(level, pos, BlockPattern.fromState(state).getItem().getDefaultInstance());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SHEARED, FLIPPED, getAgeProperty(), MSFStateProperties.BLOCK_PATTERN, EMPTY);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if(canSurvive(state, level, pos)) {
            return state.setValue(FLIPPED, pos.getY() % 2 == 0).setValue(EMPTY, BlockPattern.isEmpty(state));
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return BlockPattern.fromState(state).isBanner() ? random.nextFloat() < 0.2 : random.nextFloat() < 0.50;
    }

    @Override
    public boolean harvestable(BlockState blockState) {
        return isMaxAge(blockState) && !BlockPattern.isEmpty(blockState);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(newState.is(this)) {
            return;
        }

        var stateBelow = level.getBlockState(pos.below());
        if(!stateBelow.is(this) && !stateBelow.is(Blocks.AIR)) {
            popResource(level, pos, new ItemStack(MSFItems.PATTERNFLOWER_SEEDS.get()));
        }

        if(!BlockPattern.isEmpty(state) && isMaxAge(state)) {
            popResource(level, pos, BlockPattern.fromState(state).getItem().getDefaultInstance());
        }
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE_2;
    }
}
