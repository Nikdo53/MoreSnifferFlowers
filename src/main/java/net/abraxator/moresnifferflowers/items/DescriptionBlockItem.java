package net.abraxator.moresnifferflowers.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Consumer;

public class DescriptionBlockItem extends BlockItem {
    final Component[] components;
    public DescriptionBlockItem(Block block, Properties properties, Component... tooltipComponents) {
        super(block, properties.useItemDescriptionPrefix());
        components = tooltipComponents;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        for (Component component : components) {
            builder.accept(component);
        }
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
    }
}
