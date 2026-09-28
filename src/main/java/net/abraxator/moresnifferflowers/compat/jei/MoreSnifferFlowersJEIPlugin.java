package net.abraxator.moresnifferflowers.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.gui.screen.RebrewingStandScreen;
import net.abraxator.moresnifferflowers.compat.MSFRecipeSyncer;
import net.abraxator.moresnifferflowers.compat.jei.corruption.CorruptionCategory;
import net.abraxator.moresnifferflowers.compat.jei.corruption.CorruptionRecipe;
import net.abraxator.moresnifferflowers.compat.jei.cropressing.CropressingRecipeCategory;
import net.abraxator.moresnifferflowers.compat.jei.rebrewing.JeiRebrewingRecipe;
import net.abraxator.moresnifferflowers.compat.jei.rebrewing.RebrewingCategory;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.init.MSFRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

@JeiPlugin
public class MoreSnifferFlowersJEIPlugin implements IModPlugin {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("jei", MoreSnifferFlowers.MOD_ID);

    @Override
    public Identifier getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(CropressingRecipeCategory.CROPRESSING, MSFItems.CROPRESSOR.get().getDefaultInstance());
        registration.addCraftingStation(RebrewingCategory.REBREWING, MSFItems.REBREWING_STAND.get().getDefaultInstance());
        registration.addCraftingStation(CorruptionCategory.CORRUPTING, MSFItems.CORRUPTED_SLIME_BALL.get().getDefaultInstance());
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(RebrewingStandScreen.class, 123, 17, 9, 28, RebrewingCategory.REBREWING);
    }
    
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CropressingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new RebrewingCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CorruptionCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        MSFRecipeSyncer syncer = MSFRecipeSyncer.INSTANCE;
        registration.addRecipes(CropressingRecipeCategory.CROPRESSING, syncer.getRecipeMapForType(Minecraft.getInstance().level, MSFRecipes.Types.CROPRESSING.get()));
        registration.addRecipes(RebrewingCategory.REBREWING, JeiRebrewingRecipe.createRecipes());
        registration.addRecipes(CorruptionCategory.CORRUPTING, CorruptionRecipe.createRecipes());
    }
}
