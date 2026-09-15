package net.abraxator.moresnifferflowers.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class SaltemoneSeedsItem extends PlaceOnWaterBlockItem {
    public SaltemoneSeedsItem(Block block, Properties properties) {
        super(block, properties.useItemDescriptionPrefix());
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        builder.accept(Component.translatableWithFallback("tooltip.saltemone_seeds", "Plant on water!").withStyle(ChatFormatting.GOLD));
    }

}
