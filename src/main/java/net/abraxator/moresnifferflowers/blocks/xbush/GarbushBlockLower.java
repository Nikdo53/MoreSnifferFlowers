package net.abraxator.moresnifferflowers.blocks.xbush;

import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.init.MSFParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class GarbushBlockLower extends AbstractXBushBlockBase {
    public GarbushBlockLower(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
        return MSFItems.GARBUSH_SEEDS.get().getDefaultInstance();
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(getAge(state) == 7 && random.nextInt(100) < 10 && isLower(state)) {
            level.addAlwaysVisibleParticle(
                    MSFParticles.GARBUSH.get(),
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
    public Block getDropBlock() {
        return MSFBlocks.GARNET_BLOCK.get();
    }

    @Override
    public Block getLowerBlock() {
        return MSFBlocks.GARBUSH_BOTTOM.get();
    }

    @Override
    public Block getCorruptedLowerBlock() {
        return MSFBlocks.AMBUSH_BOTTOM.get();
    }

    @Override
    public Block getUpperBlock() {
        return MSFBlocks.GARBUSH_TOP.get();
    }

    @Override
    public Block getCorruptedUpperBlock() {
        return MSFBlocks.AMBUSH_TOP.get();
    }
}
