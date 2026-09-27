package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.init.MSFBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.nikdo53.tinymultiblocklib.blockentities.AbstractMultiBlockEntity;

public class GiantCropBlockEntity extends AbstractMultiBlockEntity implements IMSFBlockEntity {
    public int clientGrowthTicks = -1;

    public GiantCropBlockEntity(BlockPos pos, BlockState state) {
        super(MSFBlockEntities.GIANT_CROP.get(), pos, state);
    }

    @Override
    public void clientTick(Level level, BlockPos pos, BlockState state) {
        clientGrowthTicks++;
    }

}

