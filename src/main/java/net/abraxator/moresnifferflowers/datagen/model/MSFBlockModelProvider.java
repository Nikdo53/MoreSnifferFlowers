package net.abraxator.moresnifferflowers.datagen.model;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

public class MSFBlockModelProvider extends ModelProvider {
    public static final TextureSlot TEXTURE_0 = TextureSlot.create("0");

    BlockModelGenerators blockModels;
    ItemModelGenerators itemModels;

    public MSFBlockModelProvider(PackOutput output) {
        super(output, MoreSnifferFlowers.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.blockModels = blockModels;
        this.itemModels = itemModels;

        for (int i = 1; i <= 4; i++) {
            this.cubeAll(MSFBlocks.CORRUPTED_SLUDGE.getRegisteredName() + "_stage_" + i, MoreSnifferFlowers.loc("block/corrupted_sludge_stage_" + i));
        }

        this.cubeAll("corrupted_slime_block", MoreSnifferFlowers.loc("block/corrupted_slime_layer"));

        for (int i = 0; i <= 8; i++) {
            template("block/cropressor_bar_base", TEXTURE_0)
                    .create(MoreSnifferFlowers.loc("block/cropressor_bar" + i),
                            new TextureMapping()
                                    .put(TEXTURE_0, new Material(MoreSnifferFlowers.loc("block/cropressor_bar" + i))),
                            blockModels.modelOutput);
        }

    }

    public void cubeAll(String id, Identifier texture){
        ModelTemplates.CUBE_ALL.create(MoreSnifferFlowers.loc(id).withPrefix("block/"), new TextureMapping().put(TextureSlot.ALL, new Material(texture)), blockModels.modelOutput);
    }


    protected ModelTemplate template(String string, TextureSlot... slots) {
        return ModelTemplates.create(MoreSnifferFlowers.sLoc(string), slots);
    }
}
