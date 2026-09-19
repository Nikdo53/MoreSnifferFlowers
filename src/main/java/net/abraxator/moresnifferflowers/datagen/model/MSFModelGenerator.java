package net.abraxator.moresnifferflowers.datagen.model;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;

public class MSFModelGenerator extends ModelProvider {
    final PackOutput packOutput;

    public MSFModelGenerator(PackOutput output) {
        super(output, MoreSnifferFlowers.MOD_ID);
        this.packOutput = output;
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        MSFBlockFamilies.getAllNoVivicus()
                .filter(BlockFamily::shouldGenerateModel)
                .forEach(blockFamily -> blockModels.family(blockFamily.getBaseBlock()).generateFor(blockFamily));
        new MSFBlockStateGenerator(packOutput).registerModels(blockModels, itemModels);
        new MSFBlockModelProvider(packOutput).registerModels(blockModels, itemModels);
        new MSFItemModelProvider(packOutput).registerModels(blockModels, itemModels);

    }
}
