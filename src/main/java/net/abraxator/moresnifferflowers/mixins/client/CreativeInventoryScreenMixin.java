package net.abraxator.moresnifferflowers.mixins.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.abraxator.moresnifferflowers.MoreSnifferFlowersClient;
import net.abraxator.moresnifferflowers.init.MSFCreativeTabs;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
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
public abstract class CreativeInventoryScreenMixin extends AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu> {
    public CreativeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    //ignore this
    @WrapOperation(method = "selectTab", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;size()I"))
    public int skipExtraSlots(NonNullList<Slot> instance, Operation<Integer> original){
        return original.call(instance) - 2;
    }

    @ModifyVariable(method = "extractTabButton", at = @At(value = "LOAD", ordinal = 0), name = "sprites")
    protected Identifier[] renderTabButton(Identifier[] sprites, @Local(argsOnly = true, name = "tab") CreativeModeTab tab,
                                           @Local(name = "selected") boolean selected, @Local(name = "isTop") boolean isTop) {
        if (MoreSnifferFlowersClient.isBoringLoaded()) return sprites;
        if (tab != MSFCreativeTabs.MORESNIFFERFLOWERS_TAB.get()) return sprites;

        Identifier[] msfLoc;
        if (isTop) {
            msfLoc = selected ? MSFCreativeTabs.SELECTED_TOP_TABS : MSFCreativeTabs.UNSELECTED_TOP_TABS;
        } else {
            msfLoc = selected ? MSFCreativeTabs.SELECTED_BOTTOM_TABS : MSFCreativeTabs.UNSELECTED_BOTTOM_TABS;
        }

        return msfLoc;
    }



}
