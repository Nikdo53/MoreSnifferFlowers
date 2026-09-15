package net.abraxator.moresnifferflowers.blocks.vivicus;

import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.blocks.MSFCropBlock;
import net.abraxator.moresnifferflowers.entities.BoblingEntity;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class VivicusSproutingBlock extends Block implements MSFCropBlock, ColorableVivicusBlock {
    public VivicusSproutingBlock(Properties p_54422_) {
        super(p_54422_);
        this.registerDefaultState(defaultBlockState().setValue(MSFStateProperties.VIVICUS_CURED, false).setValue(MSFStateProperties.COLOR, DyeColor.WHITE));
    }
    private static final VoxelShape SHAPE0 = Block.box(3, 6,  3, 13, 16, 13);
    private static final VoxelShape SHAPE1 = Block.box(3, 2,  3, 13, 16, 13);
    private static final VoxelShape SHAPE2 = Block.box(3, 0,  3, 13, 16, 13);


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MSFStateProperties.AGE_3);
        builder.add(MSFStateProperties.VIVICUS_CURED);
        builder.add(MSFStateProperties.COLOR);
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return MSFStateProperties.AGE_3;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return super.isRandomlyTicking(state) || !isMaxAge(state);
    }

    public void grow(BlockState state, Level level, BlockPos pos) {
        makeGrowOnBonemeal(level, pos, state);
        
        if(isMaxAge(level.getBlockState(pos))) {
            BoblingEntity boblingEntity = new BoblingEntity(level, state.getValue(MSFStateProperties.VIVICUS_CURED));
            boblingEntity.setPos(pos.getCenter());
            level.addFreshEntity(boblingEntity);
            level.removeBlock(pos, false);
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        TriState soilDecision = level.getBlockState(pos.above()).canSustainPlant(level, pos.above(), Direction.DOWN, state);
        if (!soilDecision.isDefault()) return soilDecision.isTrue();
        return level.getBlockState(pos.above()).is(MSFBlocks.VIVICUS_LEAVES.get());
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if(random.nextDouble() <= 0.5D) {
            grow(state, level, pos);
        }
        
        super.randomTick(state, level, pos, random);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !isMaxAge(state);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        grow(state, level, pos);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(pos);
        return switch (state.getValue(MSFStateProperties.AGE_3)) {
            case 1 -> SHAPE1.move(vec3.x, vec3.y, vec3.z);
            case 2 -> SHAPE2.move(vec3.x, vec3.y, vec3.z);
            default -> SHAPE0.move(vec3.x, vec3.y, vec3.z);
        };
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
