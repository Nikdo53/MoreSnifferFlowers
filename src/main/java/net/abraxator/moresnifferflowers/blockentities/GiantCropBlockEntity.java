package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.MSFBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.nikdo53.tinymultiblocklib.blockentities.AbstractMultiBlockEntity;
import org.jetbrains.annotations.Nullable;

public class GiantCropBlockEntity extends AbstractMultiBlockEntity implements IMSFBlockEntity {
    public boolean canGrow = false;
    public double growProgress = 0;
    public int state = 0; //0 NONE; 1 ANIMATION; 2 SACK;
    public float staticGameTime;

    public GiantCropBlockEntity(BlockPos pos, BlockState state) {
        super(MSFBlockEntities.GIANT_CROP.get(), pos, state);
    }

    @Override
    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        if(canGrow) {
            if(staticGameTime==0){
                staticGameTime = level.getGameTime();
               // System.out.println("staticgametime="+staticGameTime);
            }
            growProgress += 0.10;
            this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
            if(growProgress >= 1) {
                canGrow = false;
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag;
        try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(this.problemPath(), MoreSnifferFlowers.LOGGER)) {
            TagValueOutput output = TagValueOutput.createWithContext(reporter, registries);
            saveAdditional(output);
            tag = output.buildResult();
        }
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void saveAdditional(ValueOutput tag) {
        super.saveAdditional(tag);
        tag.putBoolean("canGrow", canGrow);
        tag.putDouble("growProgress", growProgress);
        tag.putFloat("staticGameTime", staticGameTime);
        tag.putInt("state", this.state);
    }

    @Override
    public void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
        this.canGrow = tag.getBooleanOr("canGrow", canGrow);
        this.growProgress = tag.getDoubleOr("growProgress", growProgress);
        this.staticGameTime = tag.getFloatOr("staticGameTime", staticGameTime);
        this.state = tag.getIntOr("state", state);
    }
}
