package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.corrupted.CorruptedGiantTrunkPlacer;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.corrupted.CorruptedSludgeDecorator;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.corrupted.CorruptedTrunkPlacer;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.vivicus.VivicusTreeGrower;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.vivicus.VivicusTrunkPlacer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;
import java.util.function.Supplier;

public interface MSFWood {
    interface TreeGrowers {
        TreeGrower CORRUPTED_TREE = new TreeGrower("corrupted_tree",
                Optional.of(MSFWorldGen.ConfiguredFeatures.GIANT_CORRUPTED_TREE),
                Optional.of(MSFWorldGen.ConfiguredFeatures.CORRUPTED_TREE),
                Optional.empty());

        VivicusTreeGrower VIVICUS_TREE = new VivicusTreeGrower("vivicus_tree",
                MSFWorldGen.ConfiguredFeatures.CURED_VIVICUS_TREE,
                MSFWorldGen.ConfiguredFeatures.CORRUPTED_VIVICUS_TREE);
    }

    interface WoodTypes {
        WoodType CORRUPTED = WoodType.register(new WoodType(MoreSnifferFlowers.MOD_ID + ":corrupted", BlockSetType.WARPED));
        WoodType VIVICUS = WoodType.register(new WoodType(MoreSnifferFlowers.MOD_ID + ":vivicus", BlockSetType.CHERRY));
    }

    interface TreeDecoratorTypes {
        DeferredRegister<TreeDecoratorType<?>> DECORATORS =
                DeferredRegister.create(BuiltInRegistries.TREE_DECORATOR_TYPE, MoreSnifferFlowers.MOD_ID);

        Supplier<TreeDecoratorType<CorruptedSludgeDecorator>> CORRUPTED_SLUDGE =
                DECORATORS.register("corrupted_sludge", () -> new TreeDecoratorType<>(CorruptedSludgeDecorator.CODEC));
    }

    interface TrunkPlacerTypes {
        DeferredRegister<TrunkPlacerType<?>> TRUNKS =
                DeferredRegister.create(BuiltInRegistries.TRUNK_PLACER_TYPE, MoreSnifferFlowers.MOD_ID);

        Supplier<TrunkPlacerType<CorruptedTrunkPlacer>> CORRUPTED_TRUNK_PLACER = TRUNKS.register("corrupted_trunk_placer", () -> new TrunkPlacerType<>(CorruptedTrunkPlacer.CODEC));
        Supplier<TrunkPlacerType<CorruptedGiantTrunkPlacer>> CORRUPTED_GIANT_TRUNK_PLACER = TRUNKS.register("corrupted_giant_trunk_placer", () -> new TrunkPlacerType<>(CorruptedGiantTrunkPlacer.CODEC));
        Supplier<TrunkPlacerType<VivicusTrunkPlacer>> VIVICUS_TRUNK_PLACER = TRUNKS.register("vivicus_trunk_placer", () -> new TrunkPlacerType<>(VivicusTrunkPlacer.CODEC));
    }
}
