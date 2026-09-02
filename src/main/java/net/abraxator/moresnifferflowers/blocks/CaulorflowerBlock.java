package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.components.BlockPattern;
import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.MSFAdvancementCritters;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

import static net.abraxator.moresnifferflowers.init.MSFStateProperties.*;

public class CaulorflowerBlock extends Block implements BonemealableBlock, MSFCropBlock, Colorable, Corruptable {
    public CaulorflowerBlock(Properties properties) {
        super(properties);

        if (!isCorrupted()) {
            this.registerDefaultState(defaultBlockState()
                    .setValue(FACING, Direction.NORTH)
                    .setValue(FLIPPED, true)
                    .setValue(getAgeProperty(), 0)
                    .setValue(getColorAndEmptyProperties().getA(), DyeColor.WHITE)
                    .setValue(getColorAndEmptyProperties().getB(), true)
                    .setValue(SHEARED, false));
        }
    }

    public boolean isCorrupted(){
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, FLIPPED, SHEARED, getAgeProperty(), getColorAndEmptyProperties().getA(), getColorAndEmptyProperties().getB());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if(canSurvive(state, level, currentPos)) {
            return state.setValue(FLIPPED, currentPos.getY() % 2 == 0);
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos().below());
        if(state.is(this)) {
            return state.setValue(FLIPPED, context.getClickedPos().getY() % 2 == 0);
        }
        return super.getStateForPlacement(context).setValue(FLIPPED, context.getClickedPos().getY() % 2 == 0).setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockPos = pos.below();
        BlockState blockState = level.getBlockState(blockPos);
        BlockPos wallPos = pos.relative(state.getValue(FACING).getOpposite());
        BlockState wallState = level.getBlockState(wallPos);
        return ((blockState.is(MSFBlocks.CAULORFLOWER.get()) || blockState.is(MSFBlocks.PATTERNFLOWER.get())) && getAge(blockState) > 0) || blockState.isFaceSturdy(level, blockPos, Direction.UP) || wallState.isFaceSturdy(level, wallPos, state.getValue(FACING));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        if(random.nextFloat() < 0.15 && !state.getValue(SHEARED)) {
            grow(level, pos, false);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        Optional<BlockPos> highestPos = highestPos(level, pos, true);
        
        if(highestPos.isPresent()) {
            BlockState blockState = level.getBlockState(highestPos.get());
            return level.getBlockState(highestPos.get().above()).is(Blocks.AIR) || (blockState.hasProperty(getAgeProperty()) && !isMaxAge(blockState));
        }
        
        return false;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        grow(level, pos, true);
    }

    protected void grow(ServerLevel level, BlockPos originalPos, boolean bonemeal) {
        if(!isMaxAge(level.getBlockState(originalPos))) {
            makeGrowOnBonemeal(level, originalPos, level.getBlockState(originalPos));
        } else {
            highestPos(level, originalPos, bonemeal).ifPresent(highestPos -> {
                var posBelow = highestPos.below();
                var stateBelow = level.getBlockState(posBelow);
                if (isMaxAge(stateBelow)) {
                    level.setBlockAndUpdate(highestPos, this.withPropertiesOf(stateBelow)
                            .setValue(this.getAgeProperty(), 0).setValue(FLIPPED, highestPos.getY() % 2 == 0));
                } else {
                    makeGrowOnBonemeal(level, posBelow, stateBelow);
                }
            });
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (shear(player, level, pos, hand)){
            return InteractionResult.SUCCESS;
        }
        return harvestable(state) && stack.is(Items.BONE_MEAL)
                ? InteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION
                : super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (harvestable(state)) {
            popResource(level, pos, state);
            level.playSound(
                    null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F
            );
            BlockState blockstate = state.setValue(getAgeProperty(), 1);
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
            return InteractionResult.SUCCESS;
        } else {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }
    }

    public void popResource(Level level, BlockPos pos, BlockState state){
        popResource(level, pos, Dye.stackFromDye(new Dye(state.getValue(COLOR), 1)));
    }
    
    public boolean harvestable(BlockState blockState) {
        return isMaxAge(blockState) && !getDyeFromBlock(blockState).isEmpty();
    }

    public Optional<BlockPos> highestPos(BlockGetter level, BlockPos originalPos, boolean bonemeal) {
        var lowestPos = getLowestPos(level, originalPos);
        if(lowestPos.isEmpty()) { return Optional.empty(); }
        var highestPos = getLastConnectedBlock(level, lowestPos.get(), Direction.UP);
        return highestPos.filter(blockPos1 -> bonemeal || !((lowestPos.get().getY() + 5) <= blockPos1.getY())).map(BlockPos::above);
    }

    public Optional<BlockPos> getLowestPos(BlockGetter level, BlockPos originalPos) {
        var posDown = getLastConnectedBlock(level, originalPos, Direction.DOWN).map(BlockPos::above);
        return posDown.filter(blockPos -> level.getBlockState(blockPos).is(this));
    }

    public Optional<BlockPos> getLastConnectedBlock(BlockGetter pGetter, BlockPos pos, Direction pDirection) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();

        while (pGetter.getBlockState(blockpos$mutableblockpos).is(this)){
            blockpos$mutableblockpos.move(pDirection);
        }

        return pDirection == Direction.DOWN ? Optional.of(blockpos$mutableblockpos) : (pGetter.getBlockState(blockpos$mutableblockpos).is(Blocks.AIR) ? Optional.of(blockpos$mutableblockpos.below()) : Optional.empty());
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror pMirror) {
        return state.rotate(pMirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(newState.is(this)) {
            return;
        }

        if (newState.is(MSFBlocks.PATTERNFLOWER.get())){
            return;
        }

        var stateBelow = level.getBlockState(pos.below());
        if(!stateBelow.is(this) && !stateBelow.is(Blocks.AIR)) {
            popResource(level, pos, new ItemStack(MSFItems.CAULORFLOWER_SEEDS.get()));
        }
        
        if(!isColorEmpty(state) && isMaxAge(state)) {
            popResource(level, pos, Dye.stackFromDye(new Dye(state.getValue(COLOR), 1)));
        }
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE_2;
    }

    @Override
    public void colorBlock(Level level, BlockPos blockPos, BlockState blockState, Dye dye) {
        Colorable.super.colorBlock(level, blockPos, blockState.setValue(getColorAndEmptyProperties().getB(), false), dye);
        if(level.getNearestPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 6, false) instanceof ServerPlayer serverPlayer)
            MSFAdvancementCritters.USED_DYESPRIA.get().trigger(serverPlayer);
    }

    @Override
    public boolean canBeColored(BlockState blockState, Dye dye) {
        return Colorable.super.canBeColored(blockState, dye) || Colorable.super.isColorEmpty(blockState);
    }

    @Override
    public Map<DyeColor, Integer> colorValues() {
        Map<DyeColor, Integer> map = Colorable.super.colorValues();

        map.put(DyeColor.WHITE, 0xFFFFFFFF);
        map.put(DyeColor.LIGHT_GRAY, 0xFF9d979b);
        map.put(DyeColor.GRAY, 0xFF474f52);
        map.put(DyeColor.BLACK, 0xFF26262e);
        map.put(DyeColor.BROWN, 0xFF835432);
        map.put(DyeColor.RED, 0xFFd5544e);
        map.put(DyeColor.ORANGE, 0xFFf89635);
        map.put(DyeColor.YELLOW, 0xFFffee53);
        map.put(DyeColor.LIME, 0xFF80c71f);
        map.put(DyeColor.GREEN, 0xFF5e7c16);
        map.put(DyeColor.CYAN, 0xFF00AACC);
        map.put(DyeColor.LIGHT_BLUE, 0xFF70d9e4);
        map.put(DyeColor.BLUE, 0xFF4753ac);
        map.put(DyeColor.PURPLE, 0xFFb15fc2);
        map.put(DyeColor.MAGENTA, 0xFFd276b9);
        map.put(DyeColor.PINK, 0xFFf8b0c4);

        return map;
    }
    
    @Override
    public void onAddDye(@Nullable ItemStack destinationStack, ItemStack dye, int amount) {
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        onCorruptByEntity(entity, pos, state, this, level);
    }

    @Override
    public void onCorrupt(Level level, BlockPos pos, BlockState oldState, Block corruptedBlock) {
        var corruptedState = corruptedBlock.withPropertiesOf(corruptedBlock.defaultBlockState()
                .setValue(FACING, oldState.getValue(FACING))
                .setValue(FLIPPED, oldState.getValue(FLIPPED))
                .setValue(AGE_2, oldState.getValue(AGE_2))
                .setValue(BLOCK_PATTERN, oldState.getValue(EMPTY) ? BlockPattern.EMPTY : BlockPattern.fromDyeColor(oldState.getValue(COLOR)))
                .setValue(EMPTY, oldState.getValue(EMPTY))
        );

        level.setBlockAndUpdate(pos, corruptedState);
    }
}
