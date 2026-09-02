package net.abraxator.moresnifferflowers.mixins.client;

import com.google.common.collect.ImmutableList;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSourceList;
import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SpriteSourceList.class)
public abstract class SpriteResourceLoaderMixin {

    @Shadow @Final private List<SpriteSource> sources;

    @Inject(method = "load*",
            at = @At("RETURN"))
    private static void moresnifferflowers$load(ResourceManager resourceManager, Identifier location, CallbackInfoReturnable<SpriteSourceList> cir) {
        if (location.getPath().equals("armor_trims")) {
            SpriteSourceList ret = cir.getReturnValue();
            for (SpriteSource source : ((SpriteResourceLoaderMixin) ((Object) ret)).getSources()) {
                if (source instanceof PalettedPermutationsAccessor permutations && permutations.getPaletteKey().getPath().equals("trims/color_palettes/trim_palette")) {
                    Identifier trimLocation = MoreSnifferFlowers.loc("trims/models/armor/aroma");
                    Identifier leggingsTrimLocation = MoreSnifferFlowers.loc("trims/models/armor/aroma").withSuffix("_leggings");
                    permutations.setTextures(ImmutableList.<Identifier>builder().addAll(permutations.getTextures()).add(trimLocation, leggingsTrimLocation).build());
                }
                if (source instanceof PalettedPermutationsAccessor permutations && permutations.getPaletteKey().getPath().equals("trims/color_palettes/trim_palette")) {
                    Identifier trimLocation = MoreSnifferFlowers.loc("trims/models/armor/tater");
                    Identifier leggingsTrimLocation = MoreSnifferFlowers.loc("trims/models/armor/tater").withSuffix("_leggings");
                    permutations.setTextures(ImmutableList.<Identifier>builder().addAll(permutations.getTextures()).add(trimLocation, leggingsTrimLocation).build());
                }
                if (source instanceof PalettedPermutationsAccessor permutations && permutations.getPaletteKey().getPath().equals("trims/color_palettes/trim_palette")) {
                    Identifier trimLocation = MoreSnifferFlowers.loc("trims/models/armor/nether_wart");
                    Identifier leggingsTrimLocation = MoreSnifferFlowers.loc("trims/models/armor/nether_wart").withSuffix("_leggings");
                    permutations.setTextures(ImmutableList.<Identifier>builder().addAll(permutations.getTextures()).add(trimLocation, leggingsTrimLocation).build());
                }
                if (source instanceof PalettedPermutationsAccessor permutations && permutations.getPaletteKey().getPath().equals("trims/color_palettes/trim_palette")) {
                    Identifier trimLocation = MoreSnifferFlowers.loc("trims/models/armor/carotene");
                    Identifier leggingsTrimLocation = MoreSnifferFlowers.loc("trims/models/armor/carotene").withSuffix("_leggings");
                    permutations.setTextures(ImmutableList.<Identifier>builder().addAll(permutations.getTextures()).add(trimLocation, leggingsTrimLocation).build());
                }
                if (source instanceof PalettedPermutationsAccessor permutations && permutations.getPaletteKey().getPath().equals("trims/color_palettes/trim_palette")) {
                    Identifier trimLocation = MoreSnifferFlowers.loc("trims/models/armor/grain");
                    Identifier leggingsTrimLocation = MoreSnifferFlowers.loc("trims/models/armor/grain").withSuffix("_leggings");
                    permutations.setTextures(ImmutableList.<Identifier>builder().addAll(permutations.getTextures()).add(trimLocation, leggingsTrimLocation).build());
                }
                if (source instanceof PalettedPermutationsAccessor permutations && permutations.getPaletteKey().getPath().equals("trims/color_palettes/trim_palette")) {
                    Identifier trimLocation = MoreSnifferFlowers.loc("trims/models/armor/beat");
                    Identifier leggingsTrimLocation = MoreSnifferFlowers.loc("trims/models/armor/beat").withSuffix("_leggings");
                    permutations.setTextures(ImmutableList.<Identifier>builder().addAll(permutations.getTextures()).add(trimLocation, leggingsTrimLocation).build());
                }
            }
        }
    }

    @Accessor("sources")
    abstract List<SpriteSource> getSources();

    @Mixin(PalettedPermutations.class)
    private interface PalettedPermutationsAccessor {

        @Accessor
        List<Identifier> getTextures();

        @Accessor("textures")
        @Mutable
        void setTextures(List<Identifier> value);

        @Accessor
        Identifier getPaletteKey();
    }
}
