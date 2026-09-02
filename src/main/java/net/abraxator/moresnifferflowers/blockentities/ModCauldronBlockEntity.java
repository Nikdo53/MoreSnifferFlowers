package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.MSFBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class ModCauldronBlockEntity extends BlockEntity {
    public ModCauldronBlockEntity(BlockPos pos, BlockState blockState) {
        super(MSFBlockEntities.MOD_CAULDRON.get(), pos, blockState);
    }

    public BlockState originalCauldron = Blocks.CAULDRON.defaultBlockState();

    public ItemStack getItemstack() {
        return Item.BY_BLOCK.get(originalCauldron.getBlock()).getDefaultInstance();
    }

    public static ItemStack getItemstack(BlockGetter level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof ModCauldronBlockEntity blockEntity) {
            return blockEntity.getItemstack();
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected void saveAdditional(ValueOutput tag) {
        super.saveAdditional(tag);
        tag.putString("cauldron", BuiltInRegistries.BLOCK.getKey(originalCauldron.getBlock()).toString());
    }

    @Override
    protected void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
        originalCauldron = BuiltInRegistries.BLOCK.getValue(Identifier.parse(tag.getStringOr("cauldron", "minecraft:cauldron"))).defaultBlockState();

        if (originalCauldron.isAir()) {
            originalCauldron = Blocks.CAULDRON.defaultBlockState();
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
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        BlockState newState = level.getBlockState(pos);
        if(!newState.is(state.getBlock()) && !(newState.is(BlockTags.CAULDRONS)) && level.getBlockEntity(pos) instanceof ModCauldronBlockEntity entity) {
            ItemStack cauldronItem = entity.getItemstack();

            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), cauldronItem);
        }

        super.preRemoveSideEffects(pos, state);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
