package net.abraxator.moresnifferflowers.datagen.model;

import com.google.common.collect.ImmutableList;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blocks.BonmeeliaBlock;
import net.abraxator.moresnifferflowers.blocks.ModLayeredCauldronBlock;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.nikdo53.tinymultiblocklib.block.IMultiBlock;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.abraxator.moresnifferflowers.init.MSFBlocks.*;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

public class MSFBlockStateGenerator extends ModelProvider {
    public static final TextureSlot TEXTURE_0 = TextureSlot.create("0");
    public static final TextureSlot TEXTURE_1 = TextureSlot.create("1");
    public static final TextureSlot TEXTURE_2 = TextureSlot.create("2");
    public static final TextureSlot TEXTURE_3 = TextureSlot.create("3");
    public static final TextureSlot TEXTURE_4 = TextureSlot.create("4");

    BlockModelGenerators blockModels;
    ItemModelGenerators itemModels;

    public MSFBlockStateGenerator(PackOutput output) {
        super(output, MoreSnifferFlowers.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.blockModels = blockModels;
        this.itemModels = itemModels;

        simpleState(AMBER_BLOCK, GARNET_BLOCK);

        BlockModelGenerators.PlantType.NOT_TINTED.getCrossPot().create(POTTED_DYESPRIA.get(), BlockModelGenerators.PlantType.NOT_TINTED.getPlantTextureMapping(POTTED_DYESPRIA.get()), blockModels.modelOutput);
        BlockModelGenerators.PlantType.NOT_TINTED.getCrossPot().create(POTTED_CORRUPTED_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED.getPlantTextureMapping(POTTED_CORRUPTED_SAPLING.get()), blockModels.modelOutput);
        BlockModelGenerators.PlantType.NOT_TINTED.getCrossPot().create(POTTED_VIVICUS_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED.getPlantTextureMapping(POTTED_VIVICUS_SAPLING.get()), blockModels.modelOutput);

        multipleVariantsForStates((state, block) -> {
            int age = state.getValue(BonmeeliaBlock.AGE);
            if (age < 3) return modelFile(block, "_stage" + age);
            if (state.getValue(BonmeeliaBlock.HAS_BOTTLE)) return modelFile(block, "_bottle_" + (age - 3));
            if (state.getValue(BonmeeliaBlock.SHOW_HINT)) return modelFile(block, "_outline");
            return modelFile(block, "_empty");
        }, MSFBlocks.BONMEELIA, MSFBlocks.BONWILTIA);

        multipleVariantsForStates((state, block) -> {
            int age = state.getValue(MSFStateProperties.AGE_2);
            if (!IMultiBlock.isCenter(state)) return modelFile(block, "_stage2");
            return modelFile(block, "_stage" + age);
        }, MSFBlocks.BONDRIPIA, MSFBlocks.ACIDRIPIA);


        multipleVariantsForStates((state, block) -> {
            int age = state.getValue(MSFStateProperties.AGE_8);
            if (age < 4){
                return farmlandCrossModel(block, "_stage_" + 0);
            }
            return farmlandCrossModel(block, "_stage_" + age);
            }, MSFBlocks.AMBUSH_TOP, MSFBlocks.GARBUSH_TOP);

        multipleVariantsForStates((state, block) -> {
            int age = state.getValue(MSFStateProperties.AGE_8);
            if (age > 3 && age != 7){
                return farmlandCrossModel(block, "_stage_" + 3);
            }
            return farmlandCrossModel(block, "_stage_" + age);
        }, MSFBlocks.AMBUSH_BOTTOM, MSFBlocks.GARBUSH_BOTTOM);

        variantForStates(MSFBlocks.CORRUPTED_SLUDGE, state -> modelFile(state::getBlock, "_stage_" + (1 + 3 - state.getValue(MSFStateProperties.USES_4))));

        variantForStates(REBREWING_STAND_BOTTOM, state -> {
            String modelCode = (state.getValue(HAS_BOTTLE_0) ? "1" : "0") + (state.getValue(HAS_BOTTLE_1) ? "1" : "0") + (state.getValue(HAS_BOTTLE_2) ? "1" : "0");
            return rebrewingStandModel(modelCode);
        });

        variantForStates(MSFBlocks.BONMEEL_FILLED_CAULDRON, state -> cauldronModel(state::getBlock, state.getValue(ModLayeredCauldronBlock.LEVEL), "bonmeel_still"));
        variantForStates(MSFBlocks.ACID_FILLED_CAULDRON, state -> cauldronModel(state::getBlock, state.getValue(ModLayeredCauldronBlock.LEVEL), "acid_still"));

        empty(MSFBlocks.GIANT_BEETROOT, MSFBlocks.GIANT_CABBAGE, MSFBlocks.GIANT_CARROT,
                MSFBlocks.GIANT_POTATO, MSFBlocks.GIANT_RICE, MSFBlocks.GIANT_TOMATO,
                MSFBlocks.GIANT_ONION, MSFBlocks.GIANT_NETHERWART, MSFBlocks.GIANT_WHEAT,
                MSFBlocks.BEROOT_CAULDRON
        );

        particleOnly(VIVICUS_SIGN, VIVICUS_PLANKS);
        particleOnly(VIVICUS_WALL_SIGN, VIVICUS_PLANKS);
        particleOnly(VIVICUS_HANGING_SIGN, VIVICUS_PLANKS);
        particleOnly(VIVICUS_WALL_HANGING_SIGN, VIVICUS_PLANKS);

        simpleBlock(CORRUPTED_LEAVES.get());
        simpleState(CORRUPTED_WART, CORRUPTED_LEAVES_BUSH, TORCHFLAME, REBREWING_STAND_TOP);
        blockModels.createCrossBlock(CORRUPTED_GRASS.get(), BlockModelGenerators.PlantType.NOT_TINTED);

        variantForStates(CORRUPTED_TALL_GRASS, this::crossModel);

        blockModels.createHangingSign(CORRUPTED_PLANKS.get(), CORRUPTED_HANGING_SIGN.get(), CORRUPTED_WALL_HANGING_SIGN.get());
        blockModels.createHangingSign(VIVICUS_PLANKS.get(), VIVICUS_HANGING_SIGN.get(), VIVICUS_WALL_HANGING_SIGN.get());

        simpleVariantForStates(TORCHFLOWER_AFLAME, state -> "" + (state.getValue(MSFStateProperties.AGE_2)));
        simpleVariantForStates(TORCHEWFLOWER, state -> "" + (state.getValue(MSFStateProperties.AGE_3)));

        variantForStates(CORRUPTED_SAPLING, state -> farmlandCrossModel(state::getBlock, "_" + (state.getValue(SaplingBlock.STAGE))));
        logAndWood(CORRUPTED_LOG, CORRUPTED_WOOD);
        logAndWood(STRIPPED_CORRUPTED_LOG, STRIPPED_CORRUPTED_WOOD);

        blockModels.woodProvider(DECAYED_LOG.value()).log(DECAYED_LOG.value());
    }

    private void logAndWood(Holder<Block> log, Holder<Block> wood){
        blockModels.woodProvider(log.value()).log(log.value()).wood(wood.value());
    }


    private @NotNull MultiVariant crossModel(BlockState state) {
        Identifier sprite = key(state.getBlock()).withPrefix("block/").withSuffix(state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER ? "_top" : "_bottom");
        TextureMapping textureMapping = new TextureMapping()
                .put(TextureSlot.CROSS, new Material(sprite));

        return BlockModelGenerators.plainVariant(ModelTemplates.CROSS.create(sprite, textureMapping, modelOutput()));
    }

    @SafeVarargs
    public final void empty(Supplier<Block>... blocks) {
        for (Supplier<Block> block : blocks) {
            blockModels.createParticleOnlyBlock(block.get());
        }
    }

    public void particleOnly(Supplier<Block> block, Supplier<Block> particleBlock) {
        blockModels.createParticleOnlyBlock(block.get(), particleBlock.get());
    }

    public MultiVariant farmlandCrossModel(Supplier<Block> block, String... suffix){
        Identifier texture = BuiltInRegistries.BLOCK.getKey(block.get());
        for (String s : suffix) {
            texture = texture.withSuffix(s);
        }
        return BlockModelGenerators.plainVariant(template("farmland_cross", TextureSlot.CROSS)
                .create(texture.withPrefix("block/"), textureMapping(Map.of(
                        TextureSlot.CROSS, texture.getPath()
                )), modelOutput()));
    }

    @SafeVarargs
    public final void multipleVariantsForStates(BiFunction<BlockState, Supplier<Block>, MultiVariant> modelFunction, Supplier<Block> block, Supplier<Block>... blocks) {
        variantForStates(block, state -> modelFunction.apply(state, block));
        for (Supplier<Block> blockSupplier : blocks) {
            variantForStates(blockSupplier, state -> modelFunction.apply(state, blockSupplier));
        }
    }

    public void variantForStates(Supplier<Block> block, Function<BlockState, MultiVariant> modelFunction) {
        forAllStates(block, modelFunction, MSFStateProperties.SHEARED);
    }

    public void simpleVariantForStates(Supplier<Block> block, Function<BlockState, String> suffixFunction) {
        variantForStates(block, state -> modelFile(block, suffixFunction.apply(state)));
    }

    public BiConsumer<Identifier, ModelInstance> modelOutput() {
/*
        if (duplicateModelCollector != null){
            return duplicateModelCollector;
        }
        if (blockModels.modelOutput instanceof SimpleModelCollector simpleModelCollector){
            duplicateModelCollector = new DuplicateModelCollector(simpleModelCollector);
            return duplicateModelCollector;
        }
*/

        return blockModels.modelOutput;
    }

    public void simpleBlock(Block block) {
        blockModels.createTrivialCube(block);
    }

    @SafeVarargs
    public final void simpleState(Supplier<Block>... blocks){
        for (Supplier<Block> block : blocks) {
            blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block.get(), BlockModelGenerators.plainVariant(key(block.get()).withPrefix("block/"))));
        }
    }

    private MultiVariant modelFile(Supplier<Block> block) {
        return BlockModelGenerators.plainVariant(key(block.get()).withPrefix("block/"));
    }

    private MultiVariant modelFile(Supplier<Block> block, String suffix) {
        return BlockModelGenerators.plainVariant(key(block.get()).withPrefix("block/").withSuffix(suffix));
    }
    
    private MultiVariant rebrewingStandModel(String index) {
        return BlockModelGenerators.plainVariant(
                template("rebrewing_stand_base", TEXTURE_2, TEXTURE_3, TEXTURE_4)
                .create(MoreSnifferFlowers.loc("block/rebrewing_stand" + index), textureMapping(Map.of(
                        TEXTURE_2, index.charAt(0) == '0' ?  "rebrewing_stand_empty" : "rebrewing_stand_full",
                        TEXTURE_3, index.charAt(1) == '0' ?  "rebrewing_stand_empty" : "rebrewing_stand_full",
                        TEXTURE_4, index.charAt(2) == '0' ?  "rebrewing_stand_empty" : "rebrewing_stand_full"
                )), modelOutput()));
    }

    private MultiVariant cauldronModel(Supplier<Block> block, int level, String contentTexture) {
        String index = level == 3 ? "full" : "level" + level;
        return BlockModelGenerators.plainVariant(
                ModelTemplates.create("template_cauldron_" + index, TextureSlot.CONTENT)
                        .create(key(block.get()).withSuffix("_" + index), textureMapping(Map.of(
                                TextureSlot.CONTENT, contentTexture
                        )), modelOutput()));
    }


    private Identifier prefix(String path) {
        return MoreSnifferFlowers.loc("textures/" + path);
    }
    
    protected Identifier key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    protected ModelTemplate template(String string, TextureSlot... slots) {
        return ModelTemplates.create(MoreSnifferFlowers.sLoc(string), slots);
    }

    protected String name(Block block) {
        return key(block).getPath();
    }

    protected String name(Supplier<Block> block) {
        return key(block.get()).getPath();
    }

    protected TextureMapping textureMapping(Map<TextureSlot, String> textures) {
        TextureMapping textureMapping = new TextureMapping();
        for (Map.Entry<TextureSlot, String> entry : textures.entrySet()) {
            textureMapping.put(entry.getKey(), new Material(MoreSnifferFlowers.loc(entry.getValue()).withPrefix("block/")));
        }
        return textureMapping;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void forAllStates(Supplier<Block> blockSupplier, Function<BlockState, MultiVariant> modelFunction, Property<?>... ignoredProperties) {
        MultiVariantGenerator.Empty generator = MultiVariantGenerator.dispatch(blockSupplier.get());
        List<Property<?>> list = blockSupplier.get().getStateDefinition().getProperties().stream().filter(property -> Arrays.stream(ignoredProperties).noneMatch(property::equals)).toList();
        int size = list.size();

        ImmutableList<BlockState> possibleStatesAll = blockSupplier.get().getStateDefinition().getPossibleStates();
        List<BlockState> possibleStates = new ArrayList<>(); // ignores the ignored properties
        for (BlockState state : possibleStatesAll) {
            boolean alreadyPresent = false;

            for (BlockState state1 : possibleStates) {
                if (state == state1) continue;

                if (list.stream().allMatch(property -> state.getValue(property) == state1.getValue(property))){
                    alreadyPresent = true;
                    break;
                }
            }
            if (!alreadyPresent){
                possibleStates.add(state);
            }
        }

        PropertyDispatch<MultiVariant> dispatch;
        if (size == 1){
            PropertyDispatch.C1 initial = PropertyDispatch.initial(list.getFirst());
            for (BlockState possibleState : possibleStates) {
                initial.select(possibleState.getValue(list.getFirst()), modelFunction.apply(possibleState));
            }
            dispatch = initial;

        } else if (size == 2){
            PropertyDispatch.C2 initial = PropertyDispatch.initial(list.get(0), list.get(1));
            for (BlockState possibleState : possibleStates) {
                initial.select(possibleState.getValue(list.get(0)), possibleState.getValue(list.get(1)), modelFunction.apply(possibleState));
            }

            dispatch = initial;
        } else if (size == 3){
            PropertyDispatch.C3 initial = PropertyDispatch.initial(list.get(0), list.get(1), list.get(2));
            for (BlockState possibleState : possibleStates) {
                initial.select(possibleState.getValue(list.get(0)), possibleState.getValue(list.get(1)), possibleState.getValue(list.get(2)), modelFunction.apply(possibleState));
            }

            dispatch = initial;
        } else if (size == 4){
            PropertyDispatch.C4 initial = PropertyDispatch.initial(list.get(0), list.get(1), list.get(2), list.get(3));
            for (BlockState possibleState : possibleStates) {
                initial.select(possibleState.getValue(list.get(0)), possibleState.getValue(list.get(1)), possibleState.getValue(list.get(2)), possibleState.getValue(list.get(3)), modelFunction.apply(possibleState));
            }

            dispatch = initial;
        } else if (size == 5){
            PropertyDispatch.C5 initial = PropertyDispatch.initial(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4));
            for (BlockState possibleState : possibleStates) {
                initial.select(possibleState.getValue(list.get(0)), possibleState.getValue(list.get(1)), possibleState.getValue(list.get(2)), possibleState.getValue(list.get(3)), possibleState.getValue(list.get(4)), modelFunction.apply(possibleState));
            }
            dispatch = initial;
        } else {
            throw new IllegalArgumentException("Too many properties");
        }

        blockModels.blockStateOutput.accept(generator.with(dispatch));
    }
}
