package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.entities.SaltProjectile;
import net.abraxator.moresnifferflowers.init.MSFEffects;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class SourPuddleBlock extends Block implements SimpleWaterloggedBlock {
    private static final VoxelShape SHAPE = Block.box(0, 0,  0, 16, 2, 16);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public SourPuddleBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(PipeBlock.NORTH, Boolean.FALSE)
                .setValue(PipeBlock.EAST, Boolean.FALSE).setValue(PipeBlock.WEST, Boolean.FALSE)
                .setValue(PipeBlock.SOUTH, Boolean.FALSE).setValue(MSFStateProperties.FULL, Boolean.FALSE)
                .setValue(BlockStateProperties.WATERLOGGED, Boolean.FALSE));

    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter blockgetter = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;

        return super.getStateForPlacement(context)
                .setValue(PipeBlock.NORTH, this.connectsTo(blockgetter, blockpos, Direction.NORTH))
                .setValue(PipeBlock.EAST, this.connectsTo(blockgetter, blockpos, Direction.EAST))
                .setValue(PipeBlock.SOUTH, this.connectsTo(blockgetter, blockpos, Direction.SOUTH))
                .setValue(PipeBlock.WEST, this.connectsTo(blockgetter, blockpos, Direction.WEST))
                .setValue(MSFStateProperties.FULL, this.isFull(blockgetter, blockpos))
                .setValue(WATERLOGGED, flag);
    }

    public boolean connectsTo(BlockGetter level, BlockPos pos, Direction direction) {
        BlockPos pos1 = pos.offset(direction.getUnitVec3i());
        BlockState state = level.getBlockState(pos1);
        return state.is(this);
    }

    public boolean isFull(BlockGetter level, BlockPos pos){
        return Direction.Plane.HORIZONTAL.stream().allMatch(direction -> this.connectsTo(level, pos, direction));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader levelBad, ScheduledTickAccess ticks, BlockPos currentPos, Direction facing, BlockPos facingPos, BlockState facingState, RandomSource random) {
        if (!(levelBad instanceof Level level)) {
            MoreSnifferFlowers.LOGGER.error("Level goofed up oh no");
            return super.updateShape(state, levelBad, ticks, currentPos, facing, facingPos, facingState, random);
        }

        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        boolean isThis = facingState.is(this);
        BlockState newState = switch (facing){
            case UP, DOWN -> state;
            case NORTH -> state.setValue(PipeBlock.NORTH, isThis);
            case SOUTH -> state.setValue(PipeBlock.SOUTH, isThis);
            case EAST -> state.setValue(PipeBlock.EAST, isThis);
            case WEST -> state.setValue(PipeBlock.WEST, isThis);
        };
        if (newState.getValue(PipeBlock.WEST) && newState.getValue(PipeBlock.EAST) && newState.getValue(PipeBlock.NORTH) && newState.getValue(PipeBlock.SOUTH))
            return super.updateShape(newState.setValue(MSFStateProperties.FULL, true), levelBad, ticks, currentPos, facing, facingPos, facingState, random);

        if (isFree(level.getBlockState(currentPos.below()))  && currentPos.getY() >= level.getMinY()){

            level.setBlock(currentPos, Blocks.AIR.defaultBlockState(), 3);
            SaltProjectile projectile = new SaltProjectile((Level) level);
            projectile.setCorrupted(true);
            projectile.setPos(currentPos.below().getCenter());
            projectile.setXRot(Mth.PI / 90.0F);
            level.addFreshEntity(projectile);

            return Blocks.AIR.defaultBlockState();

        }
        if (!canSurvive(state, level, currentPos)) {
            return Blocks.AIR.defaultBlockState();
        }

        return super.updateShape(newState.setValue(MSFStateProperties.FULL, false), levelBad, ticks, currentPos, facing, facingPos, facingState, random);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
        if (!level.isClientSide() && entity instanceof LivingEntity livingEntity){
            livingEntity.addEffect(new MobEffectInstance(MSFEffects.SLIPPERY, 40, 5));
        }

    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }


    public boolean isFree(BlockState state) {
        return state.isAir() || state.is(BlockTags.FIRE) || state.liquid() || state.canBeReplaced();
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PipeBlock.NORTH, PipeBlock.EAST, PipeBlock.WEST, PipeBlock.SOUTH, MSFStateProperties.FULL, WATERLOGGED);
    }
}
