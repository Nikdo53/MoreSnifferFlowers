package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.MSFDataComponents;
import net.abraxator.moresnifferflowers.init.MSFStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ColorableVivicusBlock extends Colorable {
    default EnumProperty<DyeColor> getColorProperty() {
        return MSFStateProperties.COLOR;
    }
    
    default void addDye(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if(!blockState.hasProperty(getColorProperty())) {
            return;
        }
        
        var stack = player.getMainHandItem();
        Dye dye = Dye.getDyeFromDyespria(stack);
        RandomSource randomSource = level.getRandom();

        if(blockState.getValue(getColorProperty()).equals(dye.color()) || dye.isEmpty()) {
            return;
        }

        level.setBlockAndUpdate(blockPos, blockState.setValue(getColorProperty(), dye.color()));
        ItemStack itemStack = Dye.stackFromDye(new Dye(dye.color(), dye.amount() - randomSource.nextIntBetweenInclusive(0, 1)));
        Dye.setDyeToDyeHolderStack(stack, itemStack, itemStack.getCount());
        
        if(!level.isClientSide()) {
            particles(randomSource, ((ServerLevel) level), dye, blockPos);
        }
    }

    default int getColorId(BlockPlaceContext context){
       return context.getItemInHand().getOrDefault(MSFDataComponents.COLOR_ID, 0);
    }

    default @Nullable BlockState stateForPlacementHelper(BlockState state, BlockPlaceContext context) {
        if (state != null) {
            return state.setValue(MSFStateProperties.COLOR, DyeColor.byId(getColorId(context)));
        }
        return null;
    }

    default @NotNull ItemStack cloneItemStackHelper(BlockState state, ItemStack stack) {
        int colorId = state.getValue(MSFStateProperties.COLOR).getId();
        int color = colorValues().get(DyeColor.byId(colorId));

        stack.set(MSFDataComponents.COLOR, color);
        stack.set(MSFDataComponents.COLOR_ID, colorId);

        return stack;
    }

    @Override
    default Map<DyeColor, Integer> colorValues() {
        Map<DyeColor, Integer> map = Colorable.super.colorValues();

        map.put(DyeColor.WHITE, 0xFFFFFFFF);
        map.put(DyeColor.LIGHT_GRAY, 0xFFd2cad8);
        map.put(DyeColor.GRAY, 0xFFb0a4be);
        map.put(DyeColor.BLACK, 0xFF837e9b);
        map.put(DyeColor.BROWN, 0xFFd6a27c);
        map.put(DyeColor.RED, 0xFFffaca6);
        map.put(DyeColor.ORANGE, 0xFFffd180);
        map.put(DyeColor.YELLOW, 0xFFfff07a);
        map.put(DyeColor.LIME, 0xFFddff97);
        map.put(DyeColor.GREEN, 0xFFa2ffb2);
        map.put(DyeColor.CYAN, 0xFF9bffda);
        map.put(DyeColor.LIGHT_BLUE, 0xFFbefffa);
        map.put(DyeColor.BLUE, 0xFFa7cdff);
        map.put(DyeColor.PURPLE, 0xFFccb0ff);
        map.put(DyeColor.MAGENTA, 0xFFe9adff);
        map.put(DyeColor.PINK, 0xFFffd7f7);

        return map;
    }

}
