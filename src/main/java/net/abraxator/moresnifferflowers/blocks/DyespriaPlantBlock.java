package net.abraxator.moresnifferflowers.blocks;

import com.mojang.serialization.MapCodec;
import net.abraxator.moresnifferflowers.blockentities.DyespriaPlantBlockEntity;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DyespriaPlantBlock extends BushBlock implements MSFCropBlock, TickableEntityBlock, Corruptable, IMSFBlockExtension {
    public static final MapCodec<DyespriaPlantBlock> CODEC = simpleCodec(DyespriaPlantBlock::new);
    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 16, 14);

    public DyespriaPlantBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(getAgeProperty(), 0)
                .setValue(MSFStateProperties.SHEARED, false)
                .setValue(MSFStateProperties.COLOR, DyeColor.WHITE));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(getAgeProperty()).add(MSFStateProperties.COLOR).add(MSFStateProperties.SHEARED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity pPlacer, ItemStack stack) {
        if(pPlacer instanceof ServerPlayer serverPlayer) {
            MSFAdvancementCritters.PLACED_DYESPRIA_PLANT.get().trigger(serverPlayer);
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(shear(player, level, pos, hand)){
            return InteractionResult.SUCCESS;
        }
        if(stack.is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        } else if(isMaxAge(state) && level.getBlockEntity(pos) instanceof DyespriaPlantBlockEntity entity) {
            if(stack.getItem() instanceof DyeItem) {
                return addDye(stack, player, level, entity);

            } else if(shear(player, level, pos, hand)) {
                return InteractionResult.SUCCESS;
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(isMaxAge(state) && level.getBlockEntity(pos) instanceof DyespriaPlantBlockEntity entity) {
            player.addItem(Dye.stackFromDye(entity.removeDye()));

            return InteractionResult.SUCCESS;
        }

        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    private InteractionResult addDye(ItemStack dye, Player player, Level level, DyespriaPlantBlockEntity entity) {
        if(!level.isClientSide()) {
            var stack = dye.copy();
            dye.setCount(-1);
            player.addItem(entity.add(null, entity.dye, stack));
        }
        
        level.playSound(null, entity.getBlockPos(), SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, (float) (1.0F + level.getRandom().nextFloat() * 0.2));
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return mayPlaceOn(level.getBlockState(pos.below()));
    }
    
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(!newState.is(MSFBlocks.DYESCRAPIA_PLANT.get()) && !state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof DyespriaPlantBlockEntity entity && isMaxAge(state)) {
            ItemStack dyespria = MSFItems.DYESPRIA.get().getDefaultInstance();

            dyespria.set(MSFDataComponents.DYE, entity.dye);

            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), dyespria);
        }
    }


    @Override
    public void onCorrupt(Level level, BlockPos pos, BlockState oldState, Block corruptedBlock) {
        if(level.getBlockEntity(pos) instanceof DyespriaPlantBlockEntity entity && isMaxAge(oldState)) {
            var dye = entity.dye.toStack();

            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), dye);
        }
        
        Corruptable.super.onCorrupt(level, pos, oldState, corruptedBlock);
    }
    
    @Override
    public boolean mayPlaceOn(BlockState state) {
        return state.is(BlockTags.DIRT) && !(state.getBlock() instanceof FarmlandBlock);
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return MSFStateProperties.AGE_3;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return isMaxAge(state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        makeGrowOnTick(state, level, pos);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !isMaxAge(state);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        makeGrowOnBonemeal(level, pos, state);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
        if(player.isShiftKeyDown() && isMaxAge(state) && level.getBlockEntity(pos) instanceof DyespriaPlantBlockEntity entity) {
            var stack =  MSFItems.DYESPRIA.toStack();
            stack.set(MSFDataComponents.DYE, entity.dye);
            return stack;
        }

        return MSFItems.DYESPRIA_SEEDS.toStack();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DyespriaPlantBlockEntity(pos, state);
    }
}
