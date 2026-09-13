package net.abraxator.moresnifferflowers.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.abraxator.moresnifferflowers.events.ForgeEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingMenu.class)
public abstract class CraftingMenuMixin extends AbstractCraftingMenu {
    public CraftingMenuMixin(MenuType<?> menuType, int containerId, int width, int height) {
        super(menuType, containerId, width, height);
    }

    @Inject(method = "slotChangedCraftingGrid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isItemEnabled(Lnet/minecraft/world/flag/FeatureFlagSet;)Z", shift =  At.Shift.AFTER))
    private static void slotChangedCraftingGrid(
            AbstractContainerMenu menu, ServerLevel level,
            Player player, CraftingContainer container,
            ResultContainer resultSlots, @Nullable RecipeHolder<CraftingRecipe> recipeHint, CallbackInfo ci, @Local(name = "recipeResult") LocalRef<ItemStack> outputRef) {

        ItemStack output = outputRef.get();
        ForgeEvents.onItemCrafted(new PlayerEvent.ItemCraftedEvent(player, output, container));

        outputRef.set(output);
    }

}
