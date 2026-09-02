package net.abraxator.moresnifferflowers.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public abstract class GrowingCropBlockEntity extends BlockEntity implements IMSFBlockEntity {
    public float growProgress;
    public boolean hasGrown;
    public final float growRate;
    
    public GrowingCropBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, float growRate) {
        super(type, pos, state);
        this.growRate = growRate;
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Override
    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        if(canGrow(this.growProgress, this.hasGrown)) {
            this.growProgress += growRate;
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
            if(this.growProgress >= 1) {
                this.onGrow(getBlockPos(), getBlockState(), level);
            }
        }
    }

    public boolean canGrow(float growProgress, boolean hasGrown) {
        return !(growProgress >= 1) && !hasGrown;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    public void onGrow(BlockPos blockPos, BlockState state, Level level) {
        this.hasGrown = true;;
        this.level.sendBlockUpdated(blockPos, state, state, Block.UPDATE_CLIENTS);
    }

    public void reset() {
        this.growProgress = 0;
        this.hasGrown = false;
        //level.setBlock(blockPos, state.setValue(AmbushBlock.AGE, 7), 3);
    }

    @Override
    protected void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
        this.hasGrown = tag.getBooleanOr("hasGrown", hasGrown);
        this.growProgress = tag.getFloatOr("progress", growProgress);
    }

    @Override
    protected void saveAdditional(ValueOutput tag) {
        super.saveAdditional(tag);
        tag.putBoolean("hasGrown", this.hasGrown);
        tag.putFloat("progress", this.growProgress);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
