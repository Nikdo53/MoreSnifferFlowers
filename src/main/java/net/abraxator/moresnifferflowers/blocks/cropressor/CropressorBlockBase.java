package net.abraxator.moresnifferflowers.blocks.cropressor;

import com.mojang.serialization.MapCodec;
import net.abraxator.moresnifferflowers.blockentities.CropressorBlockEntity;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.abraxator.moresnifferflowers.init.MSFTags;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CropressorBlockBase extends HorizontalDirectionalBlock {
    public final Part PART;
    protected static final VoxelShape OUT_EAST = Block.box(0, 0, 0, 16, 11, 16);
    protected static final VoxelShape OUT_SOUTH = Block.box(0, 0, 0, 16, 11, 16);
    protected static final VoxelShape OUT_WEST = Block.box(0, 0, 0, 16, 11, 16);
    protected static final VoxelShape OUT_NORTH = Block.box(0, 0, 0, 16, 11, 16);
    protected static final VoxelShape CENTER_EAST = Block.box(0, 0, 0, 16, 11, 16);
    protected static final VoxelShape CENTER_SOUTH = Block.box(0, 0, 0, 16, 11, 16);
    protected static final VoxelShape CENTER_WEST = Block.box(0, 0, 0, 16, 11, 16);
    protected static final VoxelShape CENTER_NORTH = Block.box(0, 0, 0, 16, 11, 16);
    public static final MapCodec<CropressorBlockBase> CODEC = simpleCodec(properties1 -> new CropressorBlockBase(properties1, Part.CENTER));

    public CropressorBlockBase(Properties properties, Part part) {
        super(properties);
        PART = part;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = getConnectedDirection(state);
        if(state.getBlock() instanceof CropressorBlockOut) {
            return switch (direction) {
                case EAST -> OUT_EAST;
                case SOUTH -> OUT_SOUTH;
                case WEST -> OUT_WEST;
                default -> OUT_NORTH;
            };
        } else {
            return switch (direction) {
                case EAST -> CENTER_EAST;
                case SOUTH -> CENTER_SOUTH;
                case WEST -> CENTER_WEST;
                default -> CENTER_NORTH;
            };
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, MSFStateProperties.FULLNESS);
    }

    private Direction getNeighbourDirection(Part part, Direction direction) {
        return part == Part.OUT ? direction : direction.getOpposite();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction pDirection, BlockState pNeighborState, LevelAccessor level, BlockPos currentPos, BlockPos pNeighborPos) {
        if(pDirection == getNeighbourDirection(PART, state.getValue(FACING))) {
            var b = pNeighborState.getBlock() instanceof CropressorBlockBase;
            var b1 = getPartFromState(pNeighborState) != PART;
            if(b && b1) {
                return super.updateShape(state, pDirection, pNeighborState, level, currentPos, pNeighborPos);
            } else {
                return Blocks.AIR.defaultBlockState();
            }
        }
        
        return super.updateShape(state, pDirection, pNeighborState, level, currentPos, pNeighborPos);
            
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return MSFItems.CROPRESSOR.get().getDefaultInstance();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection();
        BlockPos blockPos = context.getClickedPos();
        BlockPos blockPos1 = blockPos.relative(direction);
        Level level = context.getLevel();

        return level.getBlockState(blockPos1).canBeReplaced(context) && level.getWorldBorder().isWithinBounds(blockPos1) ? this.defaultBlockState().setValue(FACING, direction) : null;
    }

    public static Direction getConnectedDirection(BlockState state) {
        Direction direction = state.getValue(FACING);
        return getPartFromState(state) == Part.CENTER ? direction.getOpposite() : direction;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity pPlacer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, pPlacer, stack);
        if(!level.isClientSide()) {
            BlockPos blockPos = pos.relative(state.getValue(FACING));
            level.setBlock(blockPos, MSFBlocks.CROPRESSOR_CENTER.get().defaultBlockState().setValue(FACING, state.getValue(FACING)), 3);
            state.updateNeighbourShapes(level, pos, 3);
        }
    }

    @Override
    public InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide() && level.getBlockEntity(getEntityPos(level, pos, PART)) instanceof CropressorBlockEntity entity && entity.canInteract() && player.getMainHandItem().is(MSFTags.ItemTags.CROPRESSABLE)) {

            return entity.addItem(player.getItemInHand(hand));
        }

        return InteractionResult.FAIL;
    }

    @NotNull
    public static BlockPos getEntityPos(Level level, BlockPos blockPos, Part part) {
        if(part == Part.OUT) {
            return blockPos;
        }

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            if(level.getBlockEntity(blockPos.relative(direction)) instanceof CropressorBlockEntity) {
                return blockPos.relative(direction);
            }
        }

        return null;
    }

    public static Part getPartFromState(BlockState blockState) {
        return blockState.getBlock() instanceof CropressorBlockBase baseCropressorBlock ? baseCropressorBlock.PART : null;
    }

    public static enum Part implements StringRepresentable {
        CENTER("center"),
        OUT("out");

        private String name;

        Part(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
