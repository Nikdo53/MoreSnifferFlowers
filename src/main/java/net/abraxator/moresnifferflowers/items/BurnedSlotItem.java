package net.abraxator.moresnifferflowers.items;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class BurnedSlotItem extends Item {
    public BurnedSlotItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        //TODO: this slot isnt a slot
/*
        if (entity instanceof Player player && !player.hasEffect(MSFEffects.PANTS_ON_FIRE)) {
            player.inventoryMenu.getSlot(slot.getIndex()).set(ItemStack.EMPTY);
        }
*/
    }
}
