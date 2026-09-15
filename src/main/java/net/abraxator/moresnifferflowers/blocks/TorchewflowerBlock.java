package net.abraxator.moresnifferflowers.blocks;

import com.mojang.serialization.MapCodec;
import net.abraxator.moresnifferflowers.init.MSFEffects;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TorchewflowerBlock extends BushBlock implements MSFCropBlock, IMSFBlockExtension {
    public static final MapCodec<TorchewflowerBlock> CODEC = simpleCodec(TorchewflowerBlock::new);
    public TorchewflowerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(defaultBlockState().setValue(getAgeProperty(), 0));
    }

    @Override
    public MapCodec<BushBlock> codec() {
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(getAgeProperty());
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return MSFStateProperties.AGE_3;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (getAge(state) == getMaxAge()){
            popResource(level, pos, MSFItems.SWEET_SPICE.get().getDefaultInstance());
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (isValidBonemealTarget(level, pos ,state))
            makeGrowOnTick(state, level, pos);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        int age = getAge(state);
        return age < 2;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
        if (getAge(state) == 2 && entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MSFEffects.GLUED, 100, 0));
            level.setBlock(pos, state.setValue(getAgeProperty(), getMaxAge()), 3);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (isMaxAge(state)){
            level.setBlock(pos, state.setValue(getAgeProperty(), 0), 3);
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(pos);
        return TorchflowerAflameBlock.SHAPE.move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        int age = getAge(state);
        return age < 2;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return TorchflowerAflameBlock.isBonemealSuccess(level);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(getAgeProperty(), getAge(state) + 1), 3);
    }
}
