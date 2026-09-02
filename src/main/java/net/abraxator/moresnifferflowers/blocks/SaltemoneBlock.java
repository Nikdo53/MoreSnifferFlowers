package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.blockentities.SaltemoneBlockEntity;
import net.abraxator.moresnifferflowers.blocks.multiblock.ICorruptableMultiblock;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.nikdo53.tinymultiblocklib.block.AbstractMultiBlock;
import net.nikdo53.tinymultiblocklib.block.IMultiBlock;
import net.nikdo53.tinymultiblocklib.block.IPreviewableMultiblock;
import net.nikdo53.tinymultiblocklib.block.shape.MultiblockShape;
import net.nikdo53.tinymultiblocklib.blockentities.AbstractMultiBlockEntity;
import net.nikdo53.tinymultiblocklib.components.SharedStatePropertiesBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SaltemoneBlock extends AbstractMultiBlock implements TickableEntityBlock, Corruptable, MSFCropBlock, IPreviewableMultiblock, ICorruptableMultiblock {
    public SaltemoneBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(defaultBlockState()
                .setValue(getAgeProperty(), 0)
                .setValue(MSFStateProperties.SHEARED, false));
    }
    protected static final VoxelShape AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

    @Override
    public void createSharedBlockStates(SharedStatePropertiesBuilder builder) {
        super.createSharedBlockStates(builder);
        builder.add(MSFStateProperties.SHEARED);
        builder.add(getAgeProperty());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add( getAgeProperty(), MSFStateProperties.SHEARED);
    }

    @Override
    public boolean extraSurviveRequirements(LevelReader level, BlockPos pos, BlockState state, BlockPos offset, MultiblockShape shape) {
        return !level.isWaterAt(pos) && level.isWaterAt(pos.below());
    }

    @Override
    public @Nullable DirectionContext makeDirectional() {
        return DirectionContext.horizontal();
    }

    @Override
    public BlockState getDefaultStateForPreviews(BlockState state, BlockPlaceContext blockPlaceContext) {
        return IPreviewableMultiblock.super.getDefaultStateForPreviews(state, blockPlaceContext).setValue(getAgeProperty(), getMaxAge());
    }

    @Override
    public RenderShape getMultiblockRenderShape(BlockState state, boolean c) {
        if (!c) return  RenderShape.INVISIBLE;
        if (getAge(state) == getMaxAge()) return RenderShape.MODEL;
        return RenderShape.MODEL;
    }

    @Override
    public List<BlockPos> makeFullBlockShape(Level level, BlockPos center, BlockState blockState, @Nullable BlockEntity blockEntity, @Nullable Direction direction) {
        BlockPos relative = center.relative(direction).relative(direction.getClockWise());
        return IMultiBlock.posStreamToList(BlockPos.betweenClosedStream(center, relative));
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return MSFStateProperties.AGE_2;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return !isMaxAge(blockState);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        makeGrowOnBonemeal(level, pos, state);
    }

    public boolean isCorrupted(){
        return false;
    }

    @Override
    public boolean hasCustomBE() {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);

        if (IMultiBlock.isCenter(state) && !state.getValue(MSFStateProperties.SHEARED)) {
            makeGrowOnTick(state, level, pos);
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (shear(player, level, pos, hand)){
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        corruptionHelper(state, level, pos, entity);
    }


    @Override
    public @Nullable AbstractMultiBlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SaltemoneBlockEntity(blockPos, blockState);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AABB;
    }

    @Override
    public Block getCuredBlock() {
        return MSFBlocks.SALTEMONE.get();
    }

    @Override
    public Block getCorruptedBlock() {
        return MSFBlocks.SOURLEMONE.get();
    }
}
