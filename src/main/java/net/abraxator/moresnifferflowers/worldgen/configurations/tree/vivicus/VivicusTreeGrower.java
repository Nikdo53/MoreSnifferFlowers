package net.abraxator.moresnifferflowers.worldgen.configurations.tree.vivicus;

import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;

public class VivicusTreeGrower {
    private final String name;
    private final ResourceKey<ConfiguredFeature<?, ?>> cured_tree;
    private final ResourceKey<ConfiguredFeature<?, ?>> corrupted_tree;

    public VivicusTreeGrower(String name, ResourceKey<ConfiguredFeature<?, ?>> cured_tree, ResourceKey<ConfiguredFeature<?, ?>> corrupted_tree) {
        this.name = name;
        this.cured_tree = cured_tree;
        this.corrupted_tree = corrupted_tree;
    }

    @Nullable
    private ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, BlockState saplingState) {
        return saplingState.getValue(MSFStateProperties.VIVICUS_CURED) == false ? corrupted_tree : cured_tree;
    }

    public boolean growTree(ServerLevel level, ChunkGenerator pChunkGenerator, BlockPos pos, BlockState saplingState, RandomSource random) {
        ResourceKey<ConfiguredFeature<?, ?>> resourcekey1 = this.getConfiguredFeature(random, saplingState);
        if (resourcekey1 == null) {
            return false;
        } else {
            Holder<ConfiguredFeature<?, ?>> holder1 = level.registryAccess()
                    .lookupOrThrow(Registries.CONFIGURED_FEATURE)
                    .get(resourcekey1)
                    .orElse(null);
            var event = net.neoforged.neoforge.event.EventHooks.fireBlockGrowFeature(level, random, pos, holder1);
            holder1 = event.getFeature();
            if (event.isCanceled()) return false;
            if (holder1 == null) {
                return false;
            } else {
                ConfiguredFeature<?, ?> configuredfeature1 = holder1.value();
                BlockState blockstate1 = level.getFluidState(pos).createLegacyBlock();
                //level.setBlock(pos, blockstate1, 4);
                if (configuredfeature1.place(level, pChunkGenerator, random, pos)) {
                    if (level.getBlockState(pos) == blockstate1) {
                        level.sendBlockUpdated(pos, saplingState, blockstate1, 2);
                    }

                    return true;
                } else {
                    level.setBlock(pos, saplingState, 4);
                    return false;
                }
            }
        }
    }
}
