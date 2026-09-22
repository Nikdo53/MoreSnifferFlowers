package net.abraxator.moresnifferflowers.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.abraxator.moresnifferflowers.blocks.IMSFBlockExtension;
import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.capability.CorruptionCapability;
import net.abraxator.moresnifferflowers.init.MSFTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.debug.DebugValueSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin extends ChunkAccess implements DebugValueSource, net.neoforged.neoforge.attachment.IAttachmentHolder {

    @Shadow
    @Final
    private Level level;

    public LevelChunkMixin(ChunkPos chunkPos, UpgradeData upgradeData, LevelHeightAccessor levelHeightAccessor, PalettedContainerFactory containerFactory, long inhabitedTime, LevelChunkSection @Nullable [] sections, @Nullable BlendingData blendingData) {
        super(chunkPos, upgradeData, levelHeightAccessor, containerFactory, inhabitedTime, sections, blendingData);
    }

    @Inject(method = "setBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/LevelChunkSection;getBlockState(III)Lnet/minecraft/world/level/block/state/BlockState;"))
    public void removeMethodPort(BlockPos pos, BlockState state, int flags,
                                 CallbackInfoReturnable<BlockState> cir, @Local(ordinal = 1) BlockState oldState){ //liar liar pants on fire
        if (oldState.getBlock() instanceof IMSFBlockExtension blockExtension){
            blockExtension.onRemove(oldState, level, pos, state, (flags & 64) != 0);
        }

        if (BlockPatternCapability.hasPattern(pos, level) && state.isAir()) {
            BlockPatternCapability.removePattern(pos, level);
        }

        if (!state.is(MSFTags.MSFBlockTags.CORRUPTION_SHIELDING) && oldState.is(MSFTags.MSFBlockTags.CORRUPTION_SHIELDING) && !level.isClientSide()){
            LevelChunk chunk = level.getChunkAt(pos);
            CorruptionCapability cap = CorruptionCapability.get(chunk);
            cap.flowers.remove(pos);
            if (cap.resistance > 0 && cap.flowers.size() < cap.resistance) cap.resistance--;
        }
    }
}
