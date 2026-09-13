package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.entities.SaltProjectile;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class SaltySpiceItem extends BlockItem implements ProjectileItem{
    public SaltySpiceItem(Block block, Properties properties) {
        super(block, properties.useItemDescriptionPrefix());
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        return throwItem(level, player, hand, new SaltProjectile(level, player, itemInHand), itemInHand);
    }


}
