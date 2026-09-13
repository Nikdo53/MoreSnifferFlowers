package net.abraxator.moresnifferflowers.client.color.block;

import net.abraxator.moresnifferflowers.client.MSFColorHandler;
import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.components.Dye;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;

public class CaulorflowerTint {

    public record Leaves() implements BlockTintSource {
        public static final CaulorflowerTint.Leaves INSTANCE = new CaulorflowerTint.Leaves();

        @Override
        public int color(BlockState state) {
            Colorable colorable = ((Colorable) state.getBlock());
            Dye dye = colorable.getDyeFromBlock(state);
            int color = Dye.colorForDye(colorable, dye.color());
            if(!dye.isEmpty()) {
                float[] colorHSB = MSFColorHandler.getColorHSB(color);
                return Color.HSBtoRGB(colorHSB[0], Math.max(colorHSB[1] / 1.7F, 0), Math.max(colorHSB[2], 0));
            }
            return -1;
        }
    }

    public record Flower() implements BlockTintSource{
        public static final CaulorflowerTint.Flower INSTANCE = new CaulorflowerTint.Flower();

        @Override
        public int color(BlockState state) {
            Colorable colorable = ((Colorable) state.getBlock());
            Dye dye = colorable.getDyeFromBlock(state);
            int color = Dye.colorForDye(colorable, dye.color());
            if(!dye.isEmpty()) {
                return color;
            }
            return -1;
        }
    }
}
