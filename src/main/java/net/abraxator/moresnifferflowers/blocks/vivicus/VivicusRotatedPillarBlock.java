package net.abraxator.moresnifferflowers.blocks.vivicus;

import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

public class VivicusRotatedPillarBlock extends RotatedPillarBlock implements ColorableVivicusBlock {
    public VivicusRotatedPillarBlock(Properties p_55926_) {
        super(p_55926_);
        this.registerDefaultState(defaultBlockState().setValue(MSFStateProperties.COLOR, DyeColor.WHITE).setValue(MSFStateProperties.VIVICUS_CURED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MSFStateProperties.COLOR);
        builder.add(MSFStateProperties.VIVICUS_CURED);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return stateForPlacementHelper(super.getStateForPlacement(context), context);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean data) {
        return cloneItemStackHelper(state, super.getCloneItemStack(level, pos, state, data));
    }
}
