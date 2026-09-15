package net.abraxator.moresnifferflowers.blocks.vivicus;

import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TintedParticleLeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class VivicusLeavesBlock extends TintedParticleLeavesBlock implements ColorableVivicusBlock {
    public VivicusLeavesBlock(Properties p_54422_) {
        super(0.01f, p_54422_);
        defaultBlockState().setValue(MSFStateProperties.COLOR, DyeColor.WHITE);
    }

    @Override
    protected void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) {
        BlockState blockState = level.getBlockState(pos);
        ColorParticleOption particle = ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, colorValues().get(blockState.getValue(MSFStateProperties.COLOR)));
        ParticleUtils.spawnParticleBelow(level, pos, random, particle);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MSFStateProperties.COLOR);
    }

    @Override
    public Map<DyeColor, Integer> colorValues() {
        Map<DyeColor, Integer> map = ColorableVivicusBlock.super.colorValues();
        map.put(DyeColor.WHITE, 0xFFf2fcfc);
        map.put(DyeColor.LIGHT_GRAY, 0xFFd2cad8);
        map.put(DyeColor.GRAY, 0xFFa4a9be);
        map.put(DyeColor.BLACK, 0xFF585560);
        map.put(DyeColor.BROWN, 0xFFe8b5bb);
        map.put(DyeColor.RED, 0xFFff9ab7);
        map.put(DyeColor.ORANGE, 0xFFffa586);
        map.put(DyeColor.YELLOW, 0xFFffd2bf);
        map.put(DyeColor.LIME, 0xFFddff97);
        map.put(DyeColor.GREEN, 0xFFa2ffb2);
        map.put(DyeColor.CYAN, 0xFF9bffda);
        map.put(DyeColor.LIGHT_BLUE, 0xFFc7fff2);
        map.put(DyeColor.BLUE, 0xFFa7cdff);
        map.put(DyeColor.PURPLE, 0xFFb4a5fb);
        map.put(DyeColor.MAGENTA, 0xFFe9adff);
        map.put(DyeColor.PINK, 0xFFfbe0ff);
        return map;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return stateForPlacementHelper(super.getStateForPlacement(context), context);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean data) {
        return cloneItemStackHelper(state, super.getCloneItemStack(level, pos, state, data));
    }
}
