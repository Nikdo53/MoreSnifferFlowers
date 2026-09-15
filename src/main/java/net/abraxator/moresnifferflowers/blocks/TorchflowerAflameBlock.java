package net.abraxator.moresnifferflowers.blocks;

import com.mojang.serialization.MapCodec;
import net.abraxator.moresnifferflowers.blockentities.TorchflowerBlockEntity;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.init.MSFParticles;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class TorchflowerAflameBlock extends BushBlock implements EntityBlock, MSFCropBlock, IMSFBlockExtension {
    public static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);
    public static final MapCodec<TorchflowerAflameBlock> CODEC = simpleCodec(TorchflowerAflameBlock::new);

    public TorchflowerAflameBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(defaultBlockState().setValue(getAgeProperty(), 0).setValue(MSFStateProperties.FIRE_TICKS, 0));
    }

    @Override
    public MapCodec<BushBlock> codec() {
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(getAgeProperty()).add(MSFStateProperties.FIRE_TICKS);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (getAge(state) == getMaxAge()){
            popResource(level, pos, MSFItems.FIERY_SPICE.get().getDefaultInstance());
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!level.isClientSide()) return;
        super.animateTick(state, level, pos, random);
        Vec3 vec3 = state.getOffset(pos);
        Vec3 offset = new Vec3(pos.getX() + vec3.x, pos.getY() + vec3.y, pos.getZ() + vec3.z);
        Vec3 center = pos.getCenter().add(vec3);

        if (getAge(state) == 1) {
            if (random.nextInt(24) == 0) {
                level.playLocalSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
            }

            for (int j1 = 0; j1 < 2; ++j1) {
                if (random.nextFloat() < 0.6F) {
                    double d7 = offset.x + random.nextDouble();
                    double d12 = (offset.y + 1) - random.nextDouble() * (double) 0.1F;
                    double d17 = offset.z + random.nextDouble();
                    level.addParticle(ParticleTypes.LARGE_SMOKE, d7, d12, d17, 0.0D, 0.0D, 0.0D);
                }
            }
            if (random.nextFloat() < 0.3F) {
                double d1 = center.x + random.nextDouble() / 3;
                double d2 = (center.y + 0.7) - random.nextDouble() / 2;
                double d3 = center.z + random.nextDouble() / 3;
                Particle particle = Minecraft.getInstance().particleEngine.createParticle(MSFParticles.TORCHFLAME.get(), d1, d2, d3, 0.0D, 0.0D, 0.0D);
                if (particle != null) {
                    particle.scale(0.5F + random.nextFloat());
                }
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        int age = getAge(state);
        return age == 0 || age == 1 ;
    }



    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = getAge(state);
        if (age == 0) {
            makeGrowOnTick(state, level, pos);
        }
        if (age == 1 && (!level.getBlockState(pos.below(2)).is(Blocks.NETHERRACK) || level.isRainingAt(pos))) {
            int fire = state.getValue(MSFStateProperties.FIRE_TICKS);

            if (fire < 5 && !level.isRainingAt(pos)) {
                level.setBlockAndUpdate(pos, state.setValue(MSFStateProperties.FIRE_TICKS, fire + 1));
            } else {
                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, (1.0F + level.getRandom().nextFloat() * 0.2F) * 0.7F);
                level.setBlockAndUpdate(pos, state.setValue(getAgeProperty(), 2));
            }
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        int age = getAge(state);

        if (age == 2){
            level.setBlock(pos, state.setValue(getAgeProperty(), 0), 3);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        int age = getAge(state);
        int fire = state.getValue(MSFStateProperties.FIRE_TICKS);


        if (age == 0 && stack.is(Items.BONE_MEAL)) {
            if (isBonemealSuccess(level)) {
                if (level instanceof ServerLevel serverLevel) performBonemeal(serverLevel, level.getRandom(), pos, state);
            }else if (!player.isCreative()) stack.shrink(1);

            BoneMealItem.addGrowthParticles(level, pos, 10);
            return InteractionResult.SUCCESS;
        }

        if (age == 1 && stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.WATER)){
            RandomSource random = level.getRandom();
            for(int j1 = 0; j1 < (fire + 1)*2; ++j1) {
                Vec3 vec3 = state.getOffset(pos);
                Vec3 center = pos.getCenter().add(vec3);
                double d7 = center.x + random.nextDouble() - 0.5;
                double d12 = center.y - random.nextDouble()+ 0.5;
                double d17 = center.z + random.nextDouble()- 0.5;

                level.addParticle(new DustParticleOptions(0xFFFFFFFF, 2), d7, d12, d17, 0.0D, 0.0D, 0.0D);
            }

            if (!player.isCreative()) player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));

            if (fire < 5) {
                level.setBlockAndUpdate(pos, state.setValue(MSFStateProperties.FIRE_TICKS, fire + 1));
                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, (1.0F + level.getRandom().nextFloat() * 0.2F) * 0.7F);

            } else {
                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1F, (1.0F + level.getRandom().nextFloat() * 0.2F) * 0.7F);
                level.setBlockAndUpdate(pos, state.setValue(getAgeProperty(), 2));
            }

            return InteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    public static boolean isBonemealSuccess(Level level) {
       return level.getRandom().nextFloat() < 0.3F;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TorchflowerBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(pos);
        return SHAPE.move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return getAge(state) == 0;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
       return isBonemealSuccess(level);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        level.setBlockAndUpdate(pos, Blocks.TORCHFLOWER.defaultBlockState());
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return MSFStateProperties.AGE_2;
    }
}
