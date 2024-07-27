package com.mrbysco.disccord.datagen.server;

import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

	public ModRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	protected void buildRecipes(RecipeOutput output) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistry.CUSTOM_RECORD.get())
				.pattern("PPP")
				.pattern("PEP")
				.pattern("RDR")
				.define('P', Items.PAPER)
				.define('E', Tags.Items.ENDER_PEARLS)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('D', Tags.Items.MUSIC_DISCS)
				.unlockedBy("has_paper", has(Items.PAPER))
				.unlockedBy("has_ender_pearl", has(Tags.Items.ENDER_PEARLS))
				.unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
				.unlockedBy("has_disc", has(Tags.Items.MUSIC_DISCS))
				.save(output);
	}
}
