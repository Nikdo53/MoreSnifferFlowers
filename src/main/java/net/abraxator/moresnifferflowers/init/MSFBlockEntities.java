package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.function.Supplier;

public interface MSFBlockEntities {
    DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MoreSnifferFlowers.MOD_ID);

    Supplier<BlockEntityType<GiantCropBlockEntity>> GIANT_CROP = register("giant_crop", GiantCropBlockEntity::new,
            MSFBlocks.GIANT_CARROT, MSFBlocks.GIANT_POTATO, MSFBlocks.GIANT_NETHERWART,
            MSFBlocks.GIANT_BEETROOT, MSFBlocks.GIANT_WHEAT, MSFBlocks.GIANT_ONION,
            MSFBlocks.GIANT_TOMATO, MSFBlocks.GIANT_CABBAGE, MSFBlocks.GIANT_RICE
    );

    Supplier<BlockEntityType<XbushBlockEntity>> XBUSH = register("xbush", XbushBlockEntity::new, MSFBlocks.AMBUSH_TOP, MSFBlocks.GARBUSH_TOP);
    Supplier<BlockEntityType<CropressorBlockEntity>> CROPRESSOR = register("cropressor", CropressorBlockEntity::new, MSFBlocks.CROPRESSOR_OUT);
    Supplier<BlockEntityType<RebrewingStandBlockEntity>> REBREWING_STAND = register("rebrewing_stand", RebrewingStandBlockEntity::new, MSFBlocks.REBREWING_STAND_TOP);
    Supplier<BlockEntityType<DyespriaPlantBlockEntity>> DYESPRIA_PLANT = register("dyespria_plant", DyespriaPlantBlockEntity::new, MSFBlocks.DYESPRIA_PLANT);
    Supplier<BlockEntityType<CorruptedSludgeBlockEntity>> CORRUPTED_SLUDGE = register("corrupted_sludge", CorruptedSludgeBlockEntity::new, MSFBlocks.CORRUPTED_SLUDGE);
    Supplier<BlockEntityType<BondripiaBlockEntity>> BONDRIPIA = register("bondripia", BondripiaBlockEntity::new, MSFBlocks.BONDRIPIA, MSFBlocks.ACIDRIPIA);
    Supplier<BlockEntityType<SaltemoneBlockEntity>> SALTEMONE = register("saltemone", SaltemoneBlockEntity::new, MSFBlocks.SALTEMONE, MSFBlocks.SOURLEMONE);
    Supplier<BlockEntityType<TorchflowerBlockEntity>> TORCHFLOWER = register("torchflower", TorchflowerBlockEntity::new, MSFBlocks.TORCHFLOWER_AFLAME);

    Supplier<BlockEntityType<ModCauldronBlockEntity>> MOD_CAULDRON = register("mod_cauldron", ModCauldronBlockEntity::new, MSFBlocks.ACID_FILLED_CAULDRON, MSFBlocks.BONMEEL_FILLED_CAULDRON);
    Supplier<BlockEntityType<ModSignBlockEntity>> MOD_SIGN = register("mod_sign", ModSignBlockEntity::new, MSFBlocks.CORRUPTED_SIGN, MSFBlocks.CORRUPTED_WALL_SIGN);
    Supplier<BlockEntityType<VivicusSignBlockEntity>> VIVICUS_SIGN = register("vivicus_sign", VivicusSignBlockEntity::new, MSFBlocks.VIVICUS_SIGN, MSFBlocks.VIVICUS_WALL_SIGN);
    Supplier<BlockEntityType<ModHangingSignBlockEntity>> MOD_HANGING_SIGN = register("mod_hanging_sign", ModHangingSignBlockEntity::new, MSFBlocks.CORRUPTED_HANGING_SIGN, MSFBlocks.CORRUPTED_WALL_HANGING_SIGN);
    Supplier<BlockEntityType<VivicusHangingSignBlockEntity>> VIVICUS_HANGING_SIGN = register("vivicus_hanging_sign", VivicusHangingSignBlockEntity::new, MSFBlocks.VIVICUS_HANGING_SIGN, MSFBlocks.VIVICUS_WALL_HANGING_SIGN);
    Supplier<BlockEntityType<BerootCauldronBlockEntity>> BEROOT_CAULDRON = register("beroot_cauldron", BerootCauldronBlockEntity::new, MSFBlocks.BEROOT_CAULDRON);

    @SafeVarargs
    static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<Block>... blocks) {
        return BLOCK_ENTITIES.register(name, () -> new BlockEntityType<>(supplier, Arrays.stream(blocks).map(Supplier::get).toArray(Block[]::new)));
    }
}
