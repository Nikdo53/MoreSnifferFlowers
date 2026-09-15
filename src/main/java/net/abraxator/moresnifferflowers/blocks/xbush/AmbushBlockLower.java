package net.abraxator.moresnifferflowers.blocks.xbush;

import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class AmbushBlockLower extends AbstractXBushBlockBase {
    public AmbushBlockLower(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
        return MSFItems.AMBUSH_SEEDS.get().getDefaultInstance();
    }

    @Override
    public Block getDropBlock() {
        return MSFBlocks.AMBER_BLOCK.get();
    }

    @Override
    public Block getLowerBlock() {
        return MSFBlocks.AMBUSH_BOTTOM.get();
    }

    @Override
    public Block getCorruptedLowerBlock() {
        return MSFBlocks.GARBUSH_BOTTOM.get();
    }

    @Override
    public Block getUpperBlock() {
        return MSFBlocks.AMBUSH_TOP.get();
    }

    @Override
    public Block getCorruptedUpperBlock() {
        return MSFBlocks.GARBUSH_TOP.get();
    }
}
