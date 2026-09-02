package net.abraxator.moresnifferflowers.blocks.vivicus;

import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.init.MSFWood;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.vivicus.VivicusTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

public class VivicusSaplingBlock extends SaplingBlock implements ColorableVivicusBlock {
    public final VivicusTreeGrower vivicusTreeGrower = MSFWood.TreeGrowers.VIVICUS_TREE;
    
    public VivicusSaplingBlock(Properties p_55979_) {
        super(TreeGrower.OAK, p_55979_);
        this.registerDefaultState(defaultBlockState().setValue(MSFStateProperties.COLOR, DyeColor.WHITE).setValue(MSFStateProperties.VIVICUS_CURED, false));
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        DyeColor dyeColor = Util.getRandom(DyeColor.values(), level.getRandom());
        
        if(oldState.is(this)) {
            dyeColor = getDyeFromBlock(state).color();    
        }
        
        level.setBlock(pos, state.setValue(MSFStateProperties.COLOR, dyeColor), 3);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MSFStateProperties.COLOR);
        builder.add(MSFStateProperties.VIVICUS_CURED);
    }

    @Override
    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.cycle(STAGE), 4);
        } else {
            this.vivicusTreeGrower.growTree(level, level.getChunkSource().getGenerator(), pos, state, random);
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return stateForPlacementHelper(super.getStateForPlacement(context), context);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean data) {
        return cloneItemStackHelper(state, super.getCloneItemStack(level, pos, state, data));
    }
}
