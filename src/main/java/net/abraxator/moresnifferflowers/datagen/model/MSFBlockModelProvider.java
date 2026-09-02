package net.abraxator.moresnifferflowers.datagen.model;

import com.google.common.collect.ImmutableMap;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class MSFBlockModelProvider extends BlockModelProvider {
    public MSFBlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MoreSnifferFlowers.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (int i = 1; i <= 4; i++) {
            this.cubeAll(MSFBlocks.CORRUPTED_SLUDGE.getRegisteredName() + "_stage_" + i, MoreSnifferFlowers.loc("block/corrupted_sludge_stage_" + i));
        }

        this.cubeAll("corrupted_slime_block", MoreSnifferFlowers.loc("block/corrupted_slime_layer")).renderType("translucent");

        for (int i = 0; i <= 8; i++) {
            withExistingParent("cropressor_bar" + i, modLoc("block/cropressor_bar_base")).texture("0", modLoc("block/cropressor_bar" + i));
        }

    }
/*
    final Set<BlockFamily.Variant> CUSTOM_TEXTURE_VARIANTS = Set.of(BlockFamily.Variant.DOOR, BlockFamily.Variant.CHISELED,BlockFamily.Variant.CRACKED, BlockFamily.Variant.TRAPDOOR);

    final Map<BlockFamily.Variant, BiConsumer<String, Identifier>> FAMILLY_MAP = ImmutableMap.<BlockFamily.Variant, BiConsumer<String, Identifier>>builder()
            .put(BlockFamily.Variant.BUTTON, this::button)
            .put(BlockFamily.Variant.DOOR, (s, r) -> {
                Identifier bottom = Identifier.fromNamespaceAndPath(r.getNamespace(), r.getPath() + "_bottom");
                Identifier top = Identifier.fromNamespaceAndPath(r.getNamespace(), r.getPath() + "_top");

                this.doorBottomLeft(s + "_bottom_left", bottom, top);
                this.doorBottomRight(s + "_bottom_right", bottom, top);
                this.doorTopLeft(s + "_top_left", bottom, top);
                this.doorTopRight(s + "_top_right", bottom, top);

                this.doorBottomLeftOpen(s + "_bottom_left_open", bottom, top);
                this.doorBottomRightOpen(s + "_bottom_right_open", bottom, top);
                this.doorTopLeftOpen(s + "_top_left_open", bottom, top);
                this.doorTopRightOpen(s + "_top_right_open", bottom, top);
            })
            .put(BlockFamily.Variant.CHISELED, this::cubeAll)
            .put(BlockFamily.Variant.CRACKED, this::cubeAll)
            .put(BlockFamily.Variant.FENCE, (s, r) ->{
                this.fencePost(s + "_post", r);
                this.fenceSide(s + "_side", r);
                this.fenceInventory(s + "_inventory", r);
            })
            .put(BlockFamily.Variant.FENCE_GATE, (s, r) ->{
                this.fenceGate(s, r);
                this.fenceGateOpen(s + "_open", r);
                this.fenceGateWall(s + "_wall", r);
                this.fenceGateWallOpen(s + "_wall_open", r);
            })
            .put(BlockFamily.Variant.SIGN, this::sign)
            .put(BlockFamily.Variant.WALL_SIGN, this::sign) //this might change in future mc versions
            .put(BlockFamily.Variant.SLAB, (s,r) -> {
                this.slab(s, r, r, r);
                this.slabTop(s + "_top", r, r, r);
            })
            .put(BlockFamily.Variant.STAIRS, (s,r) -> {
                this.stairs(s, r, r, r);
                this.stairsInner(s + "_inner", r, r, r);
                this.stairsOuter(s + "_outer", r, r, r);

            })
            .put(BlockFamily.Variant.PRESSURE_PLATE, this::pressurePlate)
            .put(BlockFamily.Variant.TRAPDOOR,  (s,r) -> {
                this.trapdoorOrientableBottom(s + "_bottom", r);
                this.trapdoorOrientableTop(s + "_top", r);
                this.trapdoorOrientableOpen(s + "_open", r);
            })
            .put(BlockFamily.Variant.WALL, (s,r) -> {
                this.wallPost(s + "_post", r);
                this.wallInventory(s + "_inventory", r);
                this.wallSide(s + "_side", r);
                this.wallSideTall(s + "_side_tall", r);
            })
            .build();
*/

}
