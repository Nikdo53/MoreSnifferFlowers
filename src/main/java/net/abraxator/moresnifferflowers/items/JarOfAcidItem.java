package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.entities.JarOfAcidProjectile;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class JarOfAcidItem extends Item implements ProjectileItem {
    public JarOfAcidItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        return throwItem(level,player,hand, new JarOfAcidProjectile(player, level, itemInHand), itemInHand);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        builder.accept(Component.translatableWithFallback("tooltip.acid_jar", "Ungrows organic blocks").withStyle(ChatFormatting.GOLD));
    }
}
