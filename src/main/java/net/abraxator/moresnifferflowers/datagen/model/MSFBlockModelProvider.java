package net.abraxator.moresnifferflowers.datagen.model;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;

public class MSFBlockModelProvider extends ModelProvider {
    public MSFBlockModelProvider(PackOutput output) {
        super(output, MoreSnifferFlowers.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        for (int i = 1; i <= 4; i++) {
            this.cubeAll(MSFBlocks.CORRUPTED_SLUDGE.getRegisteredName() + "_stage_" + i, MoreSnifferFlowers.loc("block/corrupted_sludge_stage_" + i));
        }

        this.cubeAll("corrupted_slime_block", MoreSnifferFlowers.loc("block/corrupted_slime_layer")).renderType("translucent");

        for (int i = 0; i <= 8; i++) {
            withExistingParent("cropressor_bar" + i, modLoc("block/cropressor_bar_base")).texture("0", modLoc("block/cropressor_bar" + i));
        }

    }
}
