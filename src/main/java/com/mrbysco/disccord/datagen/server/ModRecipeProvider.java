package com.mrbysco.disccord.datagen.server;

import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {

	public ModRecipeProvider(PackOutput packOutput) {
		super(packOutput);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistry.CUSTOM_RECORD.get())
				.pattern("PPP")
				.pattern("PEP")
				.pattern("RDR")
				.define('P', Items.PAPER)
				.define('E', Tags.Items.ENDER_PEARLS)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('D', ItemTags.MUSIC_DISCS)
				.unlockedBy("has_paper", has(Items.PAPER))
				.unlockedBy("has_ender_pearl", has(Tags.Items.ENDER_PEARLS))
				.unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
				.unlockedBy("has_disc", has(ItemTags.MUSIC_DISCS))
				.save(consumer);
	}
}
