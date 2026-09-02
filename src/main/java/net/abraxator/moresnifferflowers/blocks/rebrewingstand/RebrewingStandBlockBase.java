package net.abraxator.moresnifferflowers.blocks.rebrewingstand;

import net.abraxator.moresnifferflowers.blockentities.RebrewingStandBlockEntity;
import net.abraxator.moresnifferflowers.blocks.ModEntityDoubleTallBlock;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RebrewingStandBlockBase extends ModEntityDoubleTallBlock {
    public static final BooleanProperty[] HAS_BOTTLE = new BooleanProperty[]{BlockStateProperties.HAS_BOTTLE_0, BlockStateProperties.HAS_BOTTLE_1, BlockStateProperties.HAS_BOTTLE_2};
    public static final VoxelShape BASE = Block.box(0, 0, 0, 16, 1, 16);
    public static VoxelShape ROD_UPPER = Block.box(6.5, 0, 6.5, 9.5, 14, 9.5);
    public static VoxelShape ROD_LOWER = Block.box(6.5, 0, 6.5, 9.5, 16, 9.5);

    public RebrewingStandBlockBase(Properties properties) {
        super(properties);
        if(!(this instanceof RebrewingStandBlockTop)) {
            this.registerDefaultState(defaultBlockState().setValue(HAS_BOTTLE[0], false).setValue(HAS_BOTTLE[1], false).setValue(HAS_BOTTLE[2], false));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HAS_BOTTLE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.or(BASE, ROD_LOWER);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            BlockPos blockPos = pos;
            if(level.getBlockState(pos).is(MSFBlocks.REBREWING_STAND_BOTTOM.get())) {
                blockPos = blockPos.above();
            }

            BlockEntity entity = level.getBlockEntity(blockPos);
            if (entity instanceof RebrewingStandBlockEntity) {
                player.openMenu((RebrewingStandBlockEntity) entity);
            }

            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double d0 = (double)pos.getX() + 0.4D + (double)random.nextFloat() * 0.2D;
        double d1 = (double)pos.getY() + 0.7D + (double)random.nextFloat() * 0.3D;
        double d2 = (double)pos.getZ() + 0.4D + (double)random.nextFloat() * 0.2D;
        level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
        return MSFItems.REBREWING_STAND.toStack();
    }

    @Override
    public Block getLowerBlock() {
        return MSFBlocks.REBREWING_STAND_BOTTOM.get();
    }

    @Override
    public Block getCorruptedLowerBlock() {
        return null;
    }

    @Override
    public Block getUpperBlock() {
        return MSFBlocks.REBREWING_STAND_TOP.get();
    }

    @Override
    public Block getCorruptedUpperBlock() {
        return null;
    }

    @Override
    public boolean isLower(BlockState blockState) {
        return blockState.is(MSFBlocks.REBREWING_STAND_BOTTOM.get());
    }

    @Override
    public boolean isUpper(BlockState blockState) {
        return false;
    }
}
