package net.abraxator.moresnifferflowers.client.gui.slot;

import net.abraxator.moresnifferflowers.init.MSFDataAttachments;
import net.abraxator.moresnifferflowers.init.MSFEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class HardenedMouthSlot extends Slot {
    private final Player player;
    private final int index;

    public HardenedMouthSlot(Player player, int index, int x, int y) {
        super(DummyContainer.INSTANCE, index, x, y); // Prevents saving to normal inventory
        this.player = player;
        this.index = index;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return hasHardenedMouthEffect();
    }

    @Override
    public boolean isActive() {
        return hasHardenedMouthEffect();
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return hasHardenedMouthEffect();
    }

    @Override
    public boolean isHighlightable() {
        return hasHardenedMouthEffect();
    }

    @Override
    public ItemStack getItem() {
        return player.getData(MSFDataAttachments.HARDENED_MOUTH_SLOTS).get(index);
    }

    @Override
    public void set(ItemStack stack) {
        if (!mayPlace(stack)) {
            return;
        }
        player.getData(MSFDataAttachments.HARDENED_MOUTH_SLOTS).set(index, stack);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        player.syncData(MSFDataAttachments.HARDENED_MOUTH_SLOTS);
    }

    @Override
    public ItemStack remove(int amount) {
        ItemStack itemStack = player.getData(MSFDataAttachments.HARDENED_MOUTH_SLOTS).get(index);
        return itemStack.split(amount);
    }

    public static boolean canStack(ItemStack a, ItemStack b) {
        return ItemStack.isSameItemSameComponents(a, b);
    }

    public static boolean moveToPlayerInventory(AbstractContainerMenu menu, ItemStack stackToMove) {
        for (int i = 8; i < 36; i++) {
            Slot target = menu.getSlot(i);
            if (!target.mayPlace(stackToMove)) continue;

            ItemStack targetStack = target.getItem();

            if (targetStack.isEmpty()) {
                target.set(stackToMove.copy());
                stackToMove.setCount(0);
                return true;
            } else if (canStack(stackToMove, targetStack)) {
                int transferable = Math.min(stackToMove.getCount(), targetStack.getMaxStackSize() - targetStack.getCount());
                if (transferable > 0) {
                    targetStack.grow(transferable);
                    stackToMove.shrink(transferable);
                    target.set(targetStack);
                    if (stackToMove.isEmpty()) return true;
                }
            }
        }
        return stackToMove.isEmpty();
    }

    public boolean hasHardenedMouthEffect() {
        return player.hasEffect(MSFEffects.HARDENED_MOUTH);
    }
}