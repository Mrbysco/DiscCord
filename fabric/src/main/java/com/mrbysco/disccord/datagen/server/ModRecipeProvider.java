package com.mrbysco.disccord.datagen.server;

import com.mrbysco.disccord.registry.ModRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

	public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
		return new Provider(provider, recipeOutput);
	}

	@Override
	public String getName() {
		return "DiscCord recipes";
	}

	public static class Provider extends RecipeProvider {
		public Provider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
			super(provider, recipeOutput);
		}

		@Override
		public void buildRecipes() {
			shaped(RecipeCategory.TOOLS, ModRegistry.CUSTOM_RECORD.get())
					.pattern("PPP")
					.pattern("PEP")
					.pattern("RDR")
					.define('P', Items.PAPER)
					.define('E', ConventionalItemTags.ENDER_PEARLS)
					.define('R', ConventionalItemTags.REDSTONE_DUSTS)
					.define('D', ConventionalItemTags.MUSIC_DISCS)
					.unlockedBy("has_paper", has(Items.PAPER))
					.unlockedBy("has_ender_pearl", has(ConventionalItemTags.ENDER_PEARLS))
					.unlockedBy("has_redstone", has(ConventionalItemTags.REDSTONE_DUSTS))
					.unlockedBy("has_disc", has(ConventionalItemTags.MUSIC_DISCS))
					.save(output);
		}
	}
}
