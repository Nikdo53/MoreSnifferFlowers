package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.boblingtree.BoblingTreeTrunkPlacer;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.corrupted.CorruptedGiantTrunkPlacer;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.corrupted.CorruptedTrunkPlacer;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.vivicus.VivicusTrunkPlacer;
import net.abraxator.moresnifferflowers.worldgen.feature.VivicusTreeFeature;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLeavesDecorator;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.OptionalInt;

public interface MSFWorldGen {
    interface PlacedFeatures {
        ResourceKey<PlacedFeature> CORRUPTED_TREE = key("corrupted_tree");
        ResourceKey<PlacedFeature> GIANT_CORRUPTED_TREE = key("giant_corrupted_tree");
        ResourceKey<PlacedFeature> CURED_VIVICUS_TREE = key("cured_vivicus_tree");
        ResourceKey<PlacedFeature> CORRUPTED_VIVICUS_TREE = key("corrupted_vivicus_tree");
        
        static void bootstrap(BootstrapContext<PlacedFeature> context) {
            register(context, CORRUPTED_TREE, ConfiguredFeatures.CORRUPTED_TREE);
            register(context, GIANT_CORRUPTED_TREE, ConfiguredFeatures.GIANT_CORRUPTED_TREE);
            register(context, CURED_VIVICUS_TREE, ConfiguredFeatures.CURED_VIVICUS_TREE);
            register(context, CORRUPTED_VIVICUS_TREE, ConfiguredFeatures.CORRUPTED_VIVICUS_TREE);
        }
    
        private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> configuredFeature){
            var holderGetter = context.lookup(Registries.CONFIGURED_FEATURE);
            PlacementUtils.register(context, key, holderGetter.getOrThrow(configuredFeature), List.of());
        }
        
        private static @NotNull ResourceKey<PlacedFeature> key(String name) {
            return ResourceKey.create(Registries.PLACED_FEATURE, MoreSnifferFlowers.loc(name));
        }
    }

    interface ConfiguredFeatures {
        ResourceKey<ConfiguredFeature<?, ?>> CORRUPTED_TREE = key("corrupted_tree");
        ResourceKey<ConfiguredFeature<?, ?>> GIANT_CORRUPTED_TREE = key("giant_corrupted_tree");
        ResourceKey<ConfiguredFeature<?, ?>> CURED_VIVICUS_TREE = key("cured_vivicus_tree");
        ResourceKey<ConfiguredFeature<?, ?>> CORRUPTED_VIVICUS_TREE = key("corrupted_vivicus_tree");
        ResourceKey<ConfiguredFeature<?, ?>> BOBLING_TREE = key("bobling_tree");

        private static @NotNull ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
            return ResourceKey.create(Registries.CONFIGURED_FEATURE, MoreSnifferFlowers.loc(name));
        }

        static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
            HolderGetter<Block> blockHolderGetter = context.lookup(Registries.BLOCK);
            
            FeatureUtils.register(
                    context, CORRUPTED_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                            new WeightedStateProvider(
                                    WeightedList.<BlockState>builder()
                                            .add(MSFBlocks.CORRUPTED_LOG.get().defaultBlockState(), 10)
                                            .add(MSFBlocks.STRIPPED_CORRUPTED_LOG.get().defaultBlockState(), 2)
                            ),
                            new CorruptedTrunkPlacer(6, 1, 4),
                            new WeightedStateProvider(
                                    WeightedList.<BlockState>builder()
                                            .add(MSFBlocks.CORRUPTED_LEAVES.get().defaultBlockState(), 10)
                                            .add(MSFBlocks.CORRUPTED_LEAVES_BUSH.get().defaultBlockState(), 2)
                            ),
                            //new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), 2),
                            new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 2),
                            new TwoLayersFeatureSize(2, 0, 2))
                            .decorators(
                                    List.of(
                                            new AttachedToLeavesDecorator(
                                                    0.4F,
                                                    5,
                                                    3,
                                                    BlockStateProvider.simple(MSFBlocks.CORRUPTED_SLUDGE.get().defaultBlockState()),
                                                    4,
                                                    List.of(Direction.DOWN)
                                            )
                                    )
                            ).ignoreVines()
                            .build());
            FeatureUtils.register(
                    context, GIANT_CORRUPTED_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                            new WeightedStateProvider(
                                    WeightedList.<BlockState>builder()
                                            .add(MSFBlocks.CORRUPTED_LOG.get().defaultBlockState(), 10)
                                            .add(MSFBlocks.STRIPPED_CORRUPTED_LOG.get().defaultBlockState(), 2)
                            ),
                            new CorruptedGiantTrunkPlacer(15, 1, 8),
                            new WeightedStateProvider(
                                    WeightedList.<BlockState>builder()
                                            .add(MSFBlocks.CORRUPTED_LEAVES.get().defaultBlockState(), 10)
                                            .add(MSFBlocks.CORRUPTED_LEAVES_BUSH.get().defaultBlockState(), 2)
                            ),
                            //new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), 2),
                            new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 2),
                            new TwoLayersFeatureSize(2, 0, 2))
                            .decorators(
                                    List.of(
                                            new AttachedToLeavesDecorator(
                                                    0.01F,
                                                    5,
                                                    3,
                                                    BlockStateProvider.simple(MSFBlocks.CORRUPTED_SLUDGE.get().defaultBlockState()),
                                                    4,
                                                    List.of(Direction.DOWN)
                                            )
                                    )
                            ).belowTrunkProvider(BlockStateProvider.simple(MSFBlocks.CORRUPTED_GRASS_BLOCK.get()))
                            .ignoreVines()
                            .build());
            FeatureUtils.register(
                    context, CURED_VIVICUS_TREE, Features.VIVICUS_TREE.get(), vivicusTree()
                            .ignoreVines()
                            .decorators(List.of(
                                    new AttachedToLeavesDecorator(
                                            0.14F,
                                            1,
                                            0,
                                            BlockStateProvider.simple(MSFBlocks.VIVICUS_LEAVES_SPROUT.get().defaultBlockState()
                                                    .setValue(MSFStateProperties.VIVICUS_CURED, true)),
                                            2,
                                            List.of(Direction.DOWN)
                                    )
                            )).build());
            FeatureUtils.register(
                    context, CORRUPTED_VIVICUS_TREE, Features.VIVICUS_TREE.get(), vivicusTree()
                            .ignoreVines()
                            .decorators(List.of(
                                    new AttachedToLeavesDecorator(
                                            0.14F,
                                            1,
                                            0,
                                            BlockStateProvider.simple(MSFBlocks.VIVICUS_LEAVES_SPROUT.get().defaultBlockState()
                                                    .setValue(MSFStateProperties.VIVICUS_CURED, false)),
                                            2,
                                            List.of(Direction.DOWN)
                                    )
                            )).build());
            FeatureUtils.register(
                    context, BOBLING_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                            SimpleStateProvider.simple(Blocks.STONE.defaultBlockState()),
                            new BoblingTreeTrunkPlacer(4, 2, 1),
                            SimpleStateProvider.simple(Blocks.AIR.defaultBlockState()),
                            new RandomSpreadFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), UniformInt.of(2, 3), 20),
                            new TwoLayersFeatureSize(2, 0, 2)
                    ).ignoreVines().build());
        }
        
        private static TreeConfiguration.TreeConfigurationBuilder vivicusTree() {
            return new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(MSFBlocks.VIVICUS_LOG.get()),
                    new VivicusTrunkPlacer(8, 2, 2),
                    BlockStateProvider.simple(MSFBlocks.VIVICUS_LEAVES.get().defaultBlockState()),
                    new FancyFoliagePlacer(ConstantInt.of(3), ConstantInt.of(2), 4),
                    new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
            );
        }
    }

    interface Features {
        DeferredRegister<Feature<?>> FEATURES =
                DeferredRegister.create(BuiltInRegistries.FEATURE, MoreSnifferFlowers.MOD_ID);

        DeferredHolder<Feature<?>, VivicusTreeFeature> VIVICUS_TREE = FEATURES.register("vivicus_tree", () -> new VivicusTreeFeature(TreeConfiguration.CODEC));
    }

}
