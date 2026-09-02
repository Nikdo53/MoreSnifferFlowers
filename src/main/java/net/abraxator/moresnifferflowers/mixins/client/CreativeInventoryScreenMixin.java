package net.abraxator.moresnifferflowers.mixins.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.abraxator.moresnifferflowers.MoreSnifferFlowersClient;
import net.abraxator.moresnifferflowers.init.MSFCreativeTabs;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu> {
    public CreativeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    //ignore this
    @WrapOperation(method = "selectTab", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;size()I"))
    public int skipExtraSlots(NonNullList<Slot> instance, Operation<Integer> original){
        return original.call(instance) - 2;
    }

    @ModifyVariable(method = "renderTabButton", at = @At(value = "LOAD", ordinal = 0))
    protected Identifier[] renderTabButton(Identifier[] vanillaLoc, @Local(argsOnly = true) CreativeModeTab creativeModeTab,
                                   @Local(ordinal = 0) boolean isSelected, @Local(ordinal = 1) boolean isTop) {
        if (MoreSnifferFlowersClient.isBoringLoaded()) return vanillaLoc;
        if (creativeModeTab != MSFCreativeTabs.MORESNIFFERFLOWERS_TAB.get()) return vanillaLoc;

        Identifier[] aresourcelocation;
        if (isTop) {
            aresourcelocation = isSelected ? MSFCreativeTabs.SELECTED_TOP_TABS : MSFCreativeTabs.UNSELECTED_TOP_TABS;
        } else {
            aresourcelocation = isSelected ? MSFCreativeTabs.SELECTED_BOTTOM_TABS : MSFCreativeTabs.UNSELECTED_BOTTOM_TABS;
        }

        return aresourcelocation;
    }



}
