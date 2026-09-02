package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.entities.SaltProjectile;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SaltyClumpBlock extends Block implements SimpleWaterloggedBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty AMOUNT = MSFStateProperties.AMOUNT_4;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public SaltyClumpBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AMOUNT, 1).setValue(WATERLOGGED, false));
    }

    public BlockState rotate(BlockState p_273485_, Rotation p_273021_) {
        return p_273485_.setValue(FACING, p_273021_.rotate(p_273485_.getValue(FACING)));
    }

    public BlockState mirror(BlockState p_272961_, Mirror p_273278_) {
        return p_272961_.rotate(p_273278_.getRotation(p_272961_.getValue(FACING)));
    }

    @Override
    public boolean canBeReplaced(BlockState p_272922_, BlockPlaceContext p_273534_) {
        return !p_273534_.isSecondaryUseActive() && p_273534_.getItemInHand().is(this.asItem()) && p_272922_.getValue(AMOUNT) < 4 || super.canBeReplaced(p_272922_, p_273534_);
    }

    @Override
    public VoxelShape getShape(BlockState p_273399_, BlockGetter p_273568_, BlockPos p_273314_, CollisionContext p_273274_) {
        return Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos());
        return blockstate.is(this) ? blockstate.setValue(AMOUNT, Math.min(4, blockstate.getValue(AMOUNT) + 1)).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER)
                : this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_272634_) {
        p_272634_.add(FACING, AMOUNT, WATERLOGGED);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState state1 = level.getBlockState(pos.below());
        return state1.isFaceSturdy(level, pos.below(), Direction.UP) || state1.is(MSFBlocks.DRIPSALT.get());
    }


    @Override
    protected BlockState updateShape(BlockState state, LevelReader reader, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if(!(reader instanceof Level level))
            throw new IllegalStateException("Level goofed up oh no");

        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (isFree(level.getBlockState(pos.below()))  && pos.getY() >= level.getMinY()) {
            for (int i = 0; i < state.getValue(MSFStateProperties.AMOUNT_4); i++) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                SaltProjectile projectile = new SaltProjectile((Level) level);
                projectile.setPos(pos.below().getCenter());
                projectile.setXRot(Mth.PI / 90.0F);
                level.addFreshEntity(projectile);
            }
            return Blocks.AIR.defaultBlockState();
        }

        return canSurvive(state, level, pos) ? state : Blocks.AIR.defaultBlockState();
    }

    public boolean isFree(BlockState state) {
        return state.isAir() || state.is(BlockTags.FIRE) || state.liquid() || state.canBeReplaced();
    }

    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        int amount = state.getValue(MSFStateProperties.AMOUNT_4);

        if (amount < 4 && stack.is(MSFItems.SALTY_SPICE.get())){
            level.setBlock(pos, state.setValue(MSFStateProperties.AMOUNT_4, amount + 1), 3);

        } else if (amount == 4 && stack.is(MSFItems.SALTY_SPICE.get())){
            level.setBlock(pos, MSFBlocks.DRIPSALT.get().defaultBlockState(), 3);

        } else return InteractionResult.PASS;

        if (!player.isCreative()) stack.shrink(1);
        return InteractionResult.SUCCESS;
    }
}