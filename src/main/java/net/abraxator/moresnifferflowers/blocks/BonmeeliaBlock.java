package net.abraxator.moresnifferflowers.blocks;

import com.mojang.serialization.MapCodec;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.ScheduledTick;
import org.jspecify.annotations.Nullable;

public class BonmeeliaBlock extends BushBlock implements MSFCropBlock {
    public static final MapCodec<BonmeeliaBlock> CODEC = simpleCodec(properties -> new BonmeeliaBlock(properties, false));
    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 16, 14);
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 6);
    public static final BooleanProperty HAS_BOTTLE = BooleanProperty.create("bottle");
    public static final BooleanProperty SHOW_HINT = BooleanProperty.create("hint");
    public static final int MAX_AGE = AGE
            .getAllValues()
            .map(Property.Value::value)
            .max(Integer::compare)
            .orElse(0);
    
    private final boolean wilted;

    public BonmeeliaBlock(Properties properties, boolean wilted) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(HAS_BOTTLE, false).setValue(SHOW_HINT, false).setValue(AGE, 0));
        this.wilted = wilted;
    }

    @Override
    public @Nullable MapCodec<BushBlock> codec() {
        return null;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, HAS_BOTTLE, SHOW_HINT);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return this.mayPlaceOn(level.getBlockState(pos.below()));
    }

    @Override
    public boolean mayPlaceOn(BlockState state) {
        return MSFCropBlock.super.mayPlaceOn(state);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(stack.is(Items.GLASS_BOTTLE) && canInsertBottle(state)) {
            return addBottle(level, pos, state, stack, player);
        } else if(stack.is(Items.BONE_MEAL) && state.getValue(AGE) < 3)  {
            return InteractionResult.FAIL;
        } 
            
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (state.getValue(HAS_BOTTLE) && state.getValue(AGE) >= MAX_AGE) {
            return takeJarOfBonmeel(level, pos, state);
        } else if (!state.getValue(HAS_BOTTLE) && getAge(state) >= 3) {
            return hint(level, pos, state);
        }

        return InteractionResult.PASS;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.setBlock(pos, state.setValue(SHOW_HINT, false), 3);
    }
    
    private InteractionResult addBottle(Level level, BlockPos blockPos, BlockState blockState, ItemStack stack, Player player) {
        if(!level.isClientSide()) {
            level.setBlock(blockPos, blockState.setValue(HAS_BOTTLE, true), 3);
            if (!player.isCreative()) stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }

    private InteractionResult takeJarOfBonmeel(Level level, BlockPos blockPos, BlockState blockState) {
        level.setBlock(blockPos, blockState.setValue(AGE, 3).setValue(HAS_BOTTLE, false), 3);
        popResource(level, blockPos, wilted ? MSFItems.JAR_OF_ACID.toStack() : MSFItems.JAR_OF_BONMEEL.toStack());
        return InteractionResult.SUCCESS;
    }

    private InteractionResult hint(Level level, BlockPos blockPos, BlockState blockState) {
        level.setBlock(blockPos, blockState.setValue(SHOW_HINT, true), 3);
        level.getBlockTicks().schedule(new ScheduledTick<>(this, blockPos, level.getGameTime() + 40, level.nextSubTickCount()));
        return InteractionResult.SUCCESS;
    }
    
    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return getAge(state) < 3 || (getAge(state) >= 3 && state.getValue(HAS_BOTTLE));
    }

    private boolean canInsertBottle(BlockState blockState) {
        return blockState.getValue(AGE) == 3 && !blockState.getValue(HAS_BOTTLE);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!isMaxAge(state)) {
            level.setBlockAndUpdate(pos, state
                    .setValue(AGE, getAge(state) + 1)
                    .setValue(SHOW_HINT, false));
            var particle = new DustParticleOptions(wilted ? 0xaeff5c: 11162034, 1F);
            if (getAge(state) >= 3) {
                for (int i = 0; i <= random.nextIntBetweenInclusive(5, 10); i++) {
                    level.sendParticles(
                            particle,
                            pos.getX() + random.nextDouble(),
                            pos.getY() + random.nextDouble(),
                            pos.getZ() + random.nextDouble(),
                            1, 0, 0, 0, 0.3D);
                }
            }
        }
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return getAge(state) < 3;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        makeGrowOnBonemeal(level, pos, state);
    }
}
