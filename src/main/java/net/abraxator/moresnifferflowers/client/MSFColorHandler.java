package net.abraxator.moresnifferflowers.client;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.color.block.CaulorflowerTint;
import net.abraxator.moresnifferflowers.client.color.block.PatternflowerTint;
import net.abraxator.moresnifferflowers.client.color.block.VivicusBlockTint;
import net.abraxator.moresnifferflowers.client.color.item.DyespriaTint;
import net.abraxator.moresnifferflowers.client.color.item.PatternspriaTint;
import net.abraxator.moresnifferflowers.client.color.item.RootedSoupTint;
import net.abraxator.moresnifferflowers.client.color.item.VivicusItemTint;
import net.abraxator.moresnifferflowers.init.MSFBlocks;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

@EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, value = Dist.CLIENT)
public class MSFColorHandler {
    @SubscribeEvent
    public static void onRegisterBlockColorHandlers(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(List.of(CaulorflowerTint.Leaves.INSTANCE, CaulorflowerTint.Flower.INSTANCE), MSFBlocks.CAULORFLOWER.get());
        event.register(List.of(PatternflowerTint.Leaves.INSTANCE, PatternflowerTint.Flower.INSTANCE), MSFBlocks.PATTERNFLOWER.get());
        event.register(List.of(VivicusBlockTint.INSTANCE), MSFBlocks.VIVICUS_LOG.get(), MSFBlocks.VIVICUS_WOOD.get(), MSFBlocks.STRIPPED_VIVICUS_LOG.get(), MSFBlocks.STRIPPED_VIVICUS_LOG.get(),
                MSFBlocks.STRIPPED_VIVICUS_WOOD.get(), MSFBlocks.VIVICUS_PLANKS.get(), MSFBlocks.VIVICUS_STAIRS.get(),
                MSFBlocks.VIVICUS_SLAB.get(), MSFBlocks.VIVICUS_FENCE.get(), MSFBlocks.VIVICUS_FENCE_GATE.get(),
                MSFBlocks.VIVICUS_DOOR.get(), MSFBlocks.VIVICUS_TRAPDOOR.get(), MSFBlocks.VIVICUS_PRESSURE_PLATE.get(),
                MSFBlocks.VIVICUS_BUTTON.get(), MSFBlocks.VIVICUS_LEAVES.get(), MSFBlocks.VIVICUS_SAPLING.get(),
                MSFBlocks.VIVICUS_LEAVES_SPROUT.get(), MSFBlocks.VIVICUS_SIGN.get(), MSFBlocks.VIVICUS_HANGING_SIGN.get(),
                MSFBlocks.VIVICUS_SAPLING.get());
    }

    @SubscribeEvent
    public static void onRegisterItemColorHandlers(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(MoreSnifferFlowers.loc("dyespria"), DyespriaTint.CODEC);
        event.register(MoreSnifferFlowers.loc("patternspria"), PatternspriaTint.CODEC);
        event.register(MoreSnifferFlowers.loc("rooted_soup"), RootedSoupTint.CODEC);
        event.register(MoreSnifferFlowers.loc("vivicus"), VivicusItemTint.CODEC);
    }

    public static float @NotNull [] getColorHSB(int originalColor) {
        int startRed = (originalColor >> 16) & 0xFF;
        int startGreen = (originalColor >> 8) & 0xFF;
        int startBlue = originalColor & 0xFF;
        return Color.RGBtoHSB(startRed, startGreen, startBlue, null);
    }

    public static int[] hexToRGBLarge(int hex) {
        return new int[] {(hex >> 16) & 0xFF, (hex >> 8) & 0xFF, hex & 0xFF};
    }

    public static float[] hexToRGB(int hex) {
        int r = (hex >> 16) & 0xFF;
        int g = (hex >> 8) & 0xFF;
        int b = hex & 0xFF;
        return new float[] {r / 255f, g/ 255f, b/ 255f};
    }

    public static int[] argbToArray(int argb) {
        int r = ARGB.red(argb);
        int g = ARGB.green(argb);
        int b = ARGB.blue(argb);
        int a = ARGB.alpha(argb);
        return new int[] {r,g,b,a};
    }

    public static int alphaFixer(int color) {
        if (ARGB.alpha(color) < 1){
           int[] rgb = hexToRGBLarge(color);
           return ARGB.color(rgb[0], rgb[1], rgb[2]);
        }
        return color;
    }


    public static int RGBtoInt(Vec3 color) {
        int r = (int) color.x;
        int g = (int) color.y;
        int b = (int) color.z;

        int rgb = r;
        rgb = (rgb << 8) + g;
        rgb = (rgb << 8) + b;

        return rgb;
    }

    public static int barColorHelper(int input, int maxInput){
        int lowColor = 0x8c1111;
        int highColor = 0x179529;

        return barColorHelper(input, maxInput, lowColor, highColor);
    }


    public static int barColorHelper(int input, int maxInput, int lowColor, int highColor){
        int lowRed = (lowColor >> 16) & 0xFF;
        int lowGreen = (lowColor >> 8) & 0xFF;
        int lowBlue = lowColor & 0xFF;

        int highRed = (highColor >> 16) & 0xFF;
        int highGreen = (highColor >> 8) & 0xFF;
        int highBlue = highColor & 0xFF;

        float[] lowHSB =  Color.RGBtoHSB(lowRed, lowGreen, lowBlue, null);
        float[] highHSB =  Color.RGBtoHSB(highRed, highGreen, highBlue, null);


        float finalHue = ((lowHSB[0] * (Math.abs(input - maxInput))) + (highHSB[0] * input)) / maxInput;
        float finalSat = ((lowHSB[1] * (Math.abs(input - maxInput))) + (highHSB[1] * input)) / maxInput;
        float finalValue = ((lowHSB[2] * (Math.abs(input - maxInput))) + (highHSB[2] * input)) / maxInput;

        return Mth.hsvToRgb(finalHue, 1.0F, 1.0F);
    }

    public static int getTransformedLeavesColor(int original, BlockAndTintGetter level, BlockPos blockPos) {

        // distant horizons provides a level that doesn't actually have a level???
        BlockState blockState;
        try {
            blockState = level.getBlockState(blockPos);
        } catch (Exception e) {
           return original;
        }

        if (MSFStateProperties.hasCustomLeavesProperties(blockState) && !blockState.getValue(MSFStateProperties.NOT_CORRUPTED)) {
            float[] colorHSB = MSFColorHandler.getColorHSB(original);
            original = (Color.HSBtoRGB(-colorHSB[0] / 1.5F, colorHSB[1] - 0.25F, colorHSB[2] - 0.23F));
        }

        return original;
    }

}
