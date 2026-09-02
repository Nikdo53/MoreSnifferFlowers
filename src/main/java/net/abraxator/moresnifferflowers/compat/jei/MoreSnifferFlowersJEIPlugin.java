package net.abraxator.moresnifferflowers.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.gui.screen.RebrewingStandScreen;
import net.abraxator.moresnifferflowers.compat.jei.corruption.CorruptionCategory;
import net.abraxator.moresnifferflowers.compat.jei.corruption.CorruptionRecipe;
import net.abraxator.moresnifferflowers.compat.jei.cropressing.CropressingRecipeCategory;
import net.abraxator.moresnifferflowers.compat.jei.rebrewing.JeiRebrewingRecipe;
import net.abraxator.moresnifferflowers.compat.jei.rebrewing.RebrewingCategory;
import net.abraxator.moresnifferflowers.init.MSFItems;
import net.abraxator.moresnifferflowers.init.MSFRecipes;
import net.abraxator.moresnifferflowers.recipes.CropressingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class MoreSnifferFlowersJEIPlugin implements IModPlugin {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("jei", MoreSnifferFlowers.MOD_ID);

    @Override
    public Identifier getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(MSFItems.CROPRESSOR.get().getDefaultInstance(), CropressingRecipeCategory.CROPRESSING);
        registration.addRecipeCatalyst(MSFItems.REBREWING_STAND.get().getDefaultInstance(), RebrewingCategory.REBREWING);
        registration.addRecipeCatalyst(MSFItems.CORRUPTED_SLIME_BALL.get().getDefaultInstance(), CorruptionCategory.CORRUPTING);
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
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        List<CropressingRecipe> list = new ArrayList<>();
        recipeManager.getAllRecipesFor(MSFRecipes.Types.CROPRESSING.get()).forEach(o -> list.add(o.value()));
        registration.addRecipes(CropressingRecipeCategory.CROPRESSING, list);
        registration.addRecipes(RebrewingCategory.REBREWING, JeiRebrewingRecipe.createRecipes());
        registration.addRecipes(CorruptionCategory.CORRUPTING, CorruptionRecipe.createRecipes());
    }
}
