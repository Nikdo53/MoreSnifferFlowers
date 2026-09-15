package net.abraxator.moresnifferflowers.blocks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.MultifaceSpreadeableBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DawnberryVineBlock extends MultifaceSpreadeableBlock implements BonemealableBlock, MSFCropBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_4;
    public static final BooleanProperty SHEARED = MSFStateProperties.SHEARED;
    public static final MapCodec<DawnberryVineBlock> CODEC = RecordCodecBuilder.mapCodec(p_304392_ ->
            p_304392_.group(propertiesCodec(), Codec.BOOL.fieldOf("evil").forGetter(DawnberryVineBlock::isEvil))
                    .apply(p_304392_, DawnberryVineBlock::new));

    private final MultifaceSpreader spreader = new MultifaceSpreader(this);
    private final boolean evil;

    public DawnberryVineBlock(Properties properties, boolean evil) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(AGE, 0).setValue(SHEARED, Boolean.FALSE));

        this.evil = evil;
    }

    @Override
    public MapCodec<? extends MultifaceSpreadeableBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE, SHEARED);
    }

    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    public int getAge(BlockState state) {
        return state.getValue(this.getAgeProperty());
    }

    public int getMaxAge() {
        return AGE.getPossibleValues().stream().toList().get(AGE.getPossibleValues().size() - 1);
    }

    public final boolean isMaxAge(BlockState state) {
        return this.getAge(state) >= this.getMaxAge();
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return !this.isMaxAge(state) && !state.getValue(SHEARED);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(shear(player, level, pos, hand)) {
            return InteractionResult.SUCCESS;
        } else if(stack.is(Items.BONE_MEAL) && (getAge(state) < 4)) {
            return InteractionResult.FAIL;
        }
        
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (this.isMaxAge(state)) {
            return dropMaxAgeLoot(state, level, pos, player);
        } else if (state.getValue(AGE) == 3) {
            return dropAgeThreeLoot(state, level, pos, player);
        }

        return InteractionResult.PASS;
    }

    private InteractionResult dropMaxAgeLoot(BlockState blockState, Level level, BlockPos pos, Player player) {
        RandomSource randomSource = level.getRandom();
        final ItemStack DAWNBERRY = new ItemStack(evil ? MSFItems.GLOOMBERRY.get() : MSFItems.DAWNBERRY.get(), randomSource.nextIntBetweenInclusive(1, 2));

        popResource(level, pos, DAWNBERRY);
        level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.getRandom().nextFloat() * 0.4F);
        BlockState state = blockState.setValue(AGE, 2);
        level.setBlock(pos, state, 2);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
        return InteractionResult.SUCCESS;
    }

    protected InteractionResult dropAgeThreeLoot(BlockState blockState, Level level, BlockPos pos, Player player) {
        RandomSource randomSource = level.getRandom();
        final ItemStack DAWNBERRY = new ItemStack(evil ? MSFItems.GLOOMBERRY.get() : MSFItems.DAWNBERRY.get(), randomSource.nextIntBetweenInclusive(1, 2));
        
        popResource(level, pos, DAWNBERRY);
        level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.getRandom().nextFloat() * 0.4F);
        BlockState state = blockState.setValue(AGE, 2);
        level.setBlock(pos, state, 2);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
        return InteractionResult.SUCCESS;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (level.getRawBrightness(pos, 0) >= 9) {
            makeGrowOnTick(state, level, pos);
        }
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return spreader;
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
        //grow(state, level, pos, random);
        int age = getAge(state);
        level.setBlock(pos, state.setValue(AGE, age >= 4 ? age : age + 1), 2);
        boolean canSpread = Direction.stream().anyMatch((p_153316_) -> this.spreader.canSpreadInAnyDirection(state, level, pos, p_153316_.getOpposite()));
        if(random.nextFloat() >= 0.3F && random.nextFloat() >= 0.3F && canSpread) {
            this.getSpreader().spreadFromRandomFaceTowardRandomDirection(state, level, pos, random);
            this.getSpreader().spreadFromRandomFaceTowardRandomDirection(state, level, pos, random);
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    public boolean isEvil() {
        return evil;
    }
}
