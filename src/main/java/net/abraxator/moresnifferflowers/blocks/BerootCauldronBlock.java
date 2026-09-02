package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.blockentities.BerootCauldronBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.nikdo53.tinymultiblocklib.block.AbstractMultiBlock;
import net.nikdo53.tinymultiblocklib.block.IMultiBlock;
import net.nikdo53.tinymultiblocklib.block.IPreviewableMultiblock;
import net.nikdo53.tinymultiblocklib.blockentities.AbstractMultiBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BerootCauldronBlock extends AbstractMultiBlock implements TickableEntityBlock, IPreviewableMultiblock {
    public static final VoxelShape SHAPE_UPPER = makeShapeUpper();
    public static final VoxelShape SHAPE_LOWER = makeShapeLower();
    public static final VoxelShape SHAPE_LOWER_ROTATED = makeShapeLowerRotated();
    public static final VoxelShape SHAPE_INSIDE = Shapes.box(-0.75, 0.5625, -0.875, 0.75, 1, 0.625);
    public static final VoxelShape SHAPE_FULL = Shapes.box(-0.875, 0.5, -1, 0.875, 1.6875, 0.75);

    public BerootCauldronBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getMultiblockRenderShape(BlockState state, boolean isCenter) {
        if (isCenter) {
            return RenderShape.MODEL;
        }
        return RenderShape.INVISIBLE;
    }


    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public @Nullable EnumProperty<Direction> getDirectionProperty() {
        return HorizontalDirectionalBlock.FACING;
    }

    @Override
    public List<BlockPos> makeFullBlockShape(Level level, BlockPos center, BlockState blockState, @Nullable BlockEntity blockEntity, @Nullable Direction direction) {
        BlockPos relative = center.relative(direction).relative(direction.getClockWise()).above();
        return IMultiBlock.posStreamToList(BlockPos.betweenClosedStream(center, relative));
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        var item = player.getItemInHand(InteractionHand.MAIN_HAND);

        if(level.getBlockEntity(IMultiBlock.getCenter(level, pos)) instanceof BerootCauldronBlockEntity blockEntity) {
            return blockEntity.addItem(item, player);
        }

        return InteractionResult.PASS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.getBlockEntity(IMultiBlock.getCenter(level, pos)) instanceof BerootCauldronBlockEntity blockEntity) {
             return blockEntity.useWithoutItem(level);
        }

        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public @Nullable AbstractMultiBlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BerootCauldronBlockEntity(blockPos, blockState);
    }



    @Override
    public boolean hasCustomBE() {
        return true;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context){
        VoxelShape shape = SHAPE_UPPER;

        if (IMultiBlock.getOffset(getter, pos).getY() <= 0) {

            if (state.getValue(HorizontalDirectionalBlock.FACING).getAxis().equals(Direction.Axis.X)) {
                shape = SHAPE_LOWER_ROTATED;
            } else shape = SHAPE_LOWER;
        }
        return voxelShapeHelper(state, getter, pos, shape, 0, 0, 1.125f, true);
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return voxelShapeHelper(state, getter, pos, SHAPE_FULL, 0, 0, 1.125f, true);
    }

    public static VoxelShape makeShapeUpper(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(-0.875, 0.5, -1, 0.875, 1.6875, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.75, 0.5625, -0.875, 0.75, 1.6875, 0.625), BooleanOp.ONLY_FIRST);

        return shape.optimize();
    }

    public static VoxelShape makeShapeLower(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(-1, 0, -0.5, 1, 0.6875, 0.25), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.875, 0.5, -1, 0.875, 1.6875, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.75, 0.5625, -0.875, 0.75, 1.6875, 0.625), BooleanOp.ONLY_FIRST);


        return shape.optimize();
    }

    public static VoxelShape makeShapeLowerRotated(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(-0.375, 0, -1.125, 0.375, 0.6875, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.875, 0.5, -1, 0.875, 1.6875, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.75, 0.5625, -0.875, 0.75, 1.6875, 0.625), BooleanOp.ONLY_FIRST);

        return shape.optimize();
    }
}
