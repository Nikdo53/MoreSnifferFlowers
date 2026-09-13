package net.abraxator.moresnifferflowers.client.color.block;

import net.abraxator.moresnifferflowers.client.MSFColorHandler;
import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;

public class PatternflowerTint {

    public record Leaves() implements BlockTintSource {
        public static final Leaves INSTANCE = new Leaves();

        @Override
        public int color(BlockState state) {
            int color = state.getValue(MSFStateProperties.BLOCK_PATTERN).getColor();
            if (state.getValue(MSFStateProperties.EMPTY)) color = 0xFFFFFFFF;

            float[] colorHSB = MSFColorHandler.getColorHSB(color);
            return Color.HSBtoRGB(colorHSB[0], Math.max(colorHSB[1] / 1.7F, 0), Math.max(colorHSB[2], 0));
        }
    }

    public record Flower() implements BlockTintSource{
        public static final Flower INSTANCE = new Flower();

        @Override
        public int color(BlockState state) {
            int color = state.getValue(MSFStateProperties.BLOCK_PATTERN).getColor();
            if (state.getValue(MSFStateProperties.EMPTY)) color = 0xFFFFFFFF;

            float[] colorHSB = MSFColorHandler.getColorHSB(color);
            return Color.HSBtoRGB(colorHSB[0], Math.min(colorHSB[1] * 1.1F, 1), Math.min(colorHSB[2] * 1.2F, 1));
        }
    }
}
