package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.entities.CorruptedProjectile;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class CorruptedSlimeBallItem extends Item implements ProjectileItem {
    public CorruptedSlimeBallItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        return throwItem(level, player, hand, new CorruptedProjectile(level, player, player.getItemInHand(hand)), this.getDefaultInstance());
    }
}
