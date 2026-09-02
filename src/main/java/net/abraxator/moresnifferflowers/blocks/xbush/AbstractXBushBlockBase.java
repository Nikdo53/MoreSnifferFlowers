package net.abraxator.moresnifferflowers.blocks.xbush;

import net.abraxator.moresnifferflowers.blockentities.XbushBlockEntity;
import net.abraxator.moresnifferflowers.blocks.Corruptable;
import net.abraxator.moresnifferflowers.blocks.MSFCropBlock;
import net.abraxator.moresnifferflowers.blocks.ModEntityDoubleTallBlock;
import net.abraxator.moresnifferflowers.init.MSFParticles;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractXBushBlockBase extends ModEntityDoubleTallBlock implements MSFCropBlock, Corruptable {
    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 16, 14);
    public static final int AGE_TO_GROW_UP = 4;

    public AbstractXBushBlockBase(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(MSFStateProperties.SHEARED, false));
    }
    
    @Override
    public IntegerProperty getAgeProperty() {
        return MSFStateProperties.AGE_8;
    }

    @Override   
    public boolean isRandomlyTicking(BlockState state) {
        return !isMaxAge(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if(isUpper(state) && level.getBlockEntity(pos) instanceof XbushBlockEntity entity && entity.hasGrown) {
            return super.getShape(state, level, pos, context);
        } else {
            return SHAPE;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return this.mayPlaceOn(level.getBlockState(pos.below())) && sufficientLight(level, pos) && super.canSurvive(state, level, pos);
    }
    
    @Override
    public boolean mayPlaceOn(BlockState state) {
        return MSFCropBlock.super.mayPlaceOn(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MSFStateProperties.AGE_8);
        builder.add(MSFStateProperties.SHEARED);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if(entity instanceof Ravager && level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            level.destroyBlock(pos, true, entity);
        }

        super.entityInside(state, level, pos, entity);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext pUseContext) {
        return false;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(getAge(state) == 7 && random.nextInt(100) < 10 && isLower(state)) {
            level.addAlwaysVisibleParticle(
                    MSFParticles.AMBUSH.get(),
                    true,
                    (double)pos.getX() + 0.5 + random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1),
                    (double)pos.getY() + random.nextDouble() + random.nextDouble(),
                    (double)pos.getZ() + 0.5 + random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1),
                    0.0,
                    0.07,
                    0.0
            );
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(MSFStateProperties.SHEARED)) return;
        float f = MSFCropBlock.getGrowthSpeed(state, level, pos);
        if(random.nextInt((int) ((25.0F / f) + 1)) == 0) {
            this.grow(level, state, pos, 1);
        }
    }

    public void grow(ServerLevel level, BlockState state, BlockPos pos, int i) {
        int k = Math.min(getAge(state) + i, getMaxAge());
        if(this.canGrow(level, pos, state, k) && (level.getRandom().nextFloat() < 0.6F)) {
            level.setBlock(pos, state.setValue(getAgeProperty(), k), 2);
            if(k >= AGE_TO_GROW_UP && isLower(state)) {
                level.setBlock(pos.above(), getUpperBlock().defaultBlockState().setValue(getAgeProperty(), k), 3);
            }

            getLowerHalf(level, pos, state).ifPresent(posAndState -> {
                if(level.getBlockEntity(posAndState.blockPos().above()) instanceof XbushBlockEntity entity) {
                    entity.growProgress = 0;
                } 
            });
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (shear(player, level, pos, hand)){
            return InteractionResult.SUCCESS;
        }
        int k = Math.min(getAge(state) + 1, getMaxAge());
        return stack.is(Items.BONE_MEAL) && this.canGrow(level, pos, state, k)
                ? InteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION
                : super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        Optional<PosAndState> optional = getLowerHalf(level, pos, state);
        
        if(optional.isEmpty()) {
            return InteractionResult.PASS;
        }
        
        if(level.getBlockEntity(optional.get().blockPos().above()) instanceof XbushBlockEntity entity && entity.hasGrown) {
            var lowerPos = isLower(state) ? pos : pos.below();
            popResource(level, pos, new ItemStack(getDropBlock()));
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);

            for(int i = 0; i <= 1; i++) {
                var halfPos = i == 0 ? lowerPos : lowerPos.above();
                var halfState = level.getBlockState(halfPos).setValue(getAgeProperty(), 7);
                level.setBlock(halfPos, halfState, 3);
                level.gameEvent(GameEvent.BLOCK_CHANGE, halfPos, GameEvent.Context.of(player, halfState));
            }

            entity.reset();
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    private boolean canGrowInto(BlockState state) {
        return state.isAir() || state.is(getUpperBlock());
    }

    private boolean sufficientLight(LevelReader level, BlockPos pos) {
        return level.getRawBrightness(pos, 0) >= 8 || level.canSeeSky(pos);
    }

    @Override
    public int getMaxAge() {
        return MSFCropBlock.super.getMaxAge() - 1;
    }

    private boolean canGrow(LevelReader level, BlockPos pos, BlockState state, int k) {
        return !this.isMaxAge(state) && sufficientLight(level, pos) && (k < AGE_TO_GROW_UP || canGrowInto(level.getBlockState(pos.above()))) && isLower(state);
    }

    @Override
    public void onCorrupt(Level level, BlockPos pos, BlockState oldState, Block corruptedBlock) {
        var lowerHalf = getLowerHalf(level, pos, oldState);
        lowerHalf.ifPresent(posAndState -> {
            level.setBlockAndUpdate(posAndState.blockPos(), corruptedBlock.withPropertiesOf(oldState));
            if(getAge(lowerHalf.get().state()) > 3) {
                getCorruptedBlock(getUpperBlock(), level.getRandom()).ifPresent(block ->
                        level.setBlockAndUpdate(posAndState.blockPos().above(), block.withPropertiesOf(level.getBlockState(posAndState.blockPos().above()))));
            }
        });
    }

    Optional<PosAndState> getLowerHalf(LevelReader level, BlockPos blockPos, BlockState state) {
        if(isLower(state)) {
            return Optional.of(new PosAndState(blockPos, state));
        } else {
            BlockPos posBelow = blockPos.below();
            BlockState stateBelow = level.getBlockState(posBelow);
            return isLower(stateBelow) ? Optional.of(new PosAndState(posBelow, stateBelow)) : Optional.empty();
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        Optional<PosAndState> posAndState = this.getLowerHalf(level, pos, state);
        return posAndState.isPresent() && this.canGrow(level, posAndState.get().blockPos(), posAndState.get().state(), getAge(posAndState.get().state()) + 1);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        this.getLowerHalf(level, pos, state).ifPresent(posAndState -> {
            if(state.getValue(MSFStateProperties.AGE_8) < 8) {
                this.grow(level, posAndState.state(), posAndState.blockPos(), 1);
            } 
        });
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity pPlacer, ItemStack stack) {}
    
    public abstract Block getDropBlock();
}
