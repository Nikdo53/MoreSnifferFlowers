package net.abraxator.moresnifferflowers.client.color.block;

import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.client.MSFColorHandler;
import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public record VivicusBlockTint() implements BlockTintSource{
    public static final VivicusBlockTint INSTANCE = new VivicusBlockTint();

    @Override
    public int color(BlockState state) {
        return colorInWorld(state, null, BlockPos.ZERO);
    }

    @Override
    public int colorInWorld(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        if (pos == null) pos = new BlockPos(0,0,0);

        var colorable = ((ColorableVivicusBlock) state.getBlock());
        int dyedValue = Dye.colorForDye(colorable, state.getValue(colorable.getColorProperty()));
        DyeColor color = colorable.getDyeFromBlock(state).color();
        float[] colorHSB = MSFColorHandler.getColorHSB(dyedValue);

        if (Colorable.isModdedDye(color)) {
            colorHSB[1] = colorHSB[1] / 1.5f;
            colorHSB[2] = colorHSB[2] * 1.6f;

            if (colorHSB[2] > 1) colorHSB[2] = 1f;
        }

        if(state.is(MSFBlocks.VIVICUS_LEAVES.get()) || state.is(MSFBlocks.VIVICUS_LEAVES_SPROUT.get())) {
            float hue = colorHSB[0] + ((1+ Mth.sin((float)pos.getX() + (float)pos.getY() + (float)pos.getZ())) / 15);

            if (colorHSB[1] < 0.3 && colorHSB[2] < 0.8){
                colorHSB[2] = colorHSB[2] - ((1+Mth.sin((float)pos.getX() + (float)pos.getY() + (float)pos.getZ())) / 15);
            }

            if (colorHSB[1] < 0.3){
                colorHSB[1] = colorHSB[1] + ((1+Mth.sin((float)pos.getX() + (float)pos.getY() + (float)pos.getZ())) / 12);
            }


            return Color.HSBtoRGB(hue, colorHSB[1], colorHSB[2]);
        }

        return Color.HSBtoRGB(colorHSB[0], colorHSB[1], colorHSB[2]);
    }
}
