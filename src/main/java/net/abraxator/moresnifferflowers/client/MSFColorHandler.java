package net.abraxator.moresnifferflowers.client;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.components.BlockPattern;
import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.*;
import net.abraxator.moresnifferflowers.items.DyespriaItem;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, value = Dist.CLIENT)
public class MSFColorHandler {
    @SubscribeEvent
    public static void onRegisterBlockColorHandlers(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register((state, level, pos, tintIndex) -> {
            Colorable colorable = ((Colorable) state.getBlock());
            Dye dye = colorable.getDyeFromBlock(state);
            int color = Dye.colorForDye(colorable, dye.color());
            if(!dye.isEmpty()) {
                if (tintIndex == 0) {
                    float[] colorHSB = getColorHSB(color);

                    return Color.HSBtoRGB(colorHSB[0], Math.max(colorHSB[1] / 1.7F, 0), Math.max(colorHSB[2], 0));
                }
                if (tintIndex == 1) {
                    return color;
                }
            }
            return -1;
        }, MSFBlocks.CAULORFLOWER.get());
        event.register((state, level, pos, tintIndex) -> {
            int color = state.getValue(MSFStateProperties.BLOCK_PATTERN).getColor();
            if (state.getValue(MSFStateProperties.EMPTY)) color = 0xFFFFFF;
            if (tintIndex == 0) {
                float[] colorHSB = getColorHSB(color);
                return Color.HSBtoRGB(colorHSB[0], Math.max(colorHSB[1] / 1.7F, 0), Math.max(colorHSB[2], 0));
            }
            if (tintIndex == 1) {
                float[] colorHSB = getColorHSB(color);
                return Color.HSBtoRGB(colorHSB[0], Math.min(colorHSB[1] * 1.1F, 1), Math.min(colorHSB[2] * 1.2F, 1));
            }

            return -1;
        }, MSFBlocks.PATTERNFLOWER.get());

        event.register((state, level, pos, tintIndex) -> {
                    var colorable = ((ColorableVivicusBlock) state.getBlock());
                    if(tintIndex == 0) {
                        int dyedValue = Dye.colorForDye(colorable, state.getValue(colorable.getColorProperty()));
                        DyeColor color = colorable.getDyeFromBlock(state).color();
                        float[] colorHSB = getColorHSB(dyedValue);

                        if (Colorable.isModdedDye(color)) {
                            colorHSB[1] = colorHSB[1] / 1.5f;
                            colorHSB[2] = colorHSB[2] * 1.6f;

                            if (colorHSB[2] > 1) colorHSB[2] = 1f;
                        }

                        if(state.is(MSFBlocks.VIVICUS_LEAVES.get()) || state.is(MSFBlocks.VIVICUS_LEAVES_SPROUT.get())) {
                            if (pos == null) pos = new BlockPos(0,0,0);
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

                    return -1;
                }, MSFBlocks.VIVICUS_LOG.get(), MSFBlocks.VIVICUS_WOOD.get(), MSFBlocks.STRIPPED_VIVICUS_LOG.get(), MSFBlocks.STRIPPED_VIVICUS_LOG.get(),
                MSFBlocks.STRIPPED_VIVICUS_WOOD.get(), MSFBlocks.VIVICUS_PLANKS.get(), MSFBlocks.VIVICUS_STAIRS.get(),
                MSFBlocks.VIVICUS_SLAB.get(), MSFBlocks.VIVICUS_FENCE.get(), MSFBlocks.VIVICUS_FENCE_GATE.get(),
                MSFBlocks.VIVICUS_DOOR.get(), MSFBlocks.VIVICUS_TRAPDOOR.get(), MSFBlocks.VIVICUS_PRESSURE_PLATE.get(),
                MSFBlocks.VIVICUS_BUTTON.get(), MSFBlocks.VIVICUS_LEAVES.get(), MSFBlocks.VIVICUS_SAPLING.get(),
                MSFBlocks.VIVICUS_LEAVES_SPROUT.get(), MSFBlocks.VIVICUS_SIGN.get(), MSFBlocks.VIVICUS_HANGING_SIGN.get(),
                MSFBlocks.VIVICUS_SAPLING.get());
    }

    @SubscribeEvent
    public static void onRegisterItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            Dye dye = Dye.getDyeFromDyespria(stack);
            if(tintIndex != 0 || dye.isEmpty()) {
                return -1;
            } else {
                return Dye.colorForDye(((DyespriaItem) stack.getItem()), dye.color());
            }
        }, MSFItems.DYESPRIA.get());

        event.register((stack, tintIndex) -> tintIndex > 0 ? -1 : stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getColor(),
                MSFItems.EXTRACTED_BOTTLE.get(), MSFItems.REBREWED_POTION.get(), MSFItems.REBREWED_SPLASH_POTION.get(), MSFItems.REBREWED_LINGERING_POTION.get());

        event.register(((stack, tintIndex) ->{
           BlockPattern pattern = BlockPattern.fromPatternspria(stack);
           if(tintIndex != 0 || pattern == BlockPattern.EMPTY) return -1;
           return alphaFixer(stack.getOrDefault(MSFDataComponents.COLOR.get(), pattern.getColor()));
        }), MSFItems.PATTERNSPRIA.get());

        event.register(((stack, tintIndex) ->{
            if (tintIndex != 0) return -1;
            return alphaFixer(stack.getOrDefault(MSFDataComponents.COLOR.get(), 0xffffffff));
        }), MSFItems.ROOTED_SOUP.get());


        event.register(((stack, tintIndex) -> {
                    if (tintIndex != 0) return -1;

                    if (stack.has(MSFDataComponents.COLOR)) {
                        int color = stack.getOrDefault(MSFDataComponents.COLOR, 0xffffffff);
                        int colorId = stack.getOrDefault(MSFDataComponents.COLOR_ID, 0);

                        if (Colorable.isModdedDye(DyeColor.byId(colorId))) {
                            float[] colorHSB = getColorHSB(color);

                            colorHSB[1] = colorHSB[1] / 1.5f;
                            colorHSB[2] = colorHSB[2] * 1.6f;

                            if (colorHSB[2] > 1) colorHSB[2] = 1f;

                            return alphaFixer(Color.HSBtoRGB(colorHSB[0], colorHSB[1], colorHSB[2]));
                        }

                        return alphaFixer(color);
                    }
                    return -1;
                }
        ), MSFBlocks.VIVICUS_LOG.get(),  MSFBlocks.VIVICUS_WOOD.get(), MSFBlocks.STRIPPED_VIVICUS_LOG.get(),  MSFBlocks.STRIPPED_VIVICUS_WOOD.get(), MSFBlocks.VIVICUS_PLANKS.get(),
                MSFBlocks.VIVICUS_STAIRS.get(), MSFBlocks.VIVICUS_SLAB.get(), MSFBlocks.VIVICUS_FENCE.get(), MSFBlocks.VIVICUS_FENCE_GATE.get(), MSFBlocks.VIVICUS_DOOR.get(),
                MSFBlocks.VIVICUS_TRAPDOOR.get(), MSFBlocks.VIVICUS_PRESSURE_PLATE.get(), MSFBlocks.VIVICUS_BUTTON.get(), MSFBlocks.VIVICUS_LEAVES.get(),
                MSFBlocks.VIVICUS_LEAVES_SPROUT.get(), MSFItems.VIVICUS_SIGN.get(), MSFItems.VIVICUS_HANGING_SIGN.get(), MSFItems.VIVICUS_BOAT.get(), MSFItems.VIVICUS_CHEST_BOAT.get());

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
        int r = FastColor.ARGB32.red(argb);
        int g = FastColor.ARGB32.green(argb);
        int b = FastColor.ARGB32.blue(argb);
        int a = FastColor.ARGB32.alpha(argb);
        return new int[] {r,g,b,a};
    }

    public static int alphaFixer(int color) {
        if (FastColor.ARGB32.alpha(color) < 1){
           int[] rgb = hexToRGBLarge(color);
           return FastColor.ARGB32.color(rgb[0], rgb[1], rgb[2]);
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
