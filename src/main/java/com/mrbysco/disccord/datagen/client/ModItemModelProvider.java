package com.mrbysco.disccord.datagen.client;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

public class ModItemModelProvider extends ModelProvider {
	public ModItemModelProvider(PackOutput packOutput) {
		super(packOutput, DiscCordMod.MOD_ID);
	}

	@Override
	protected void registerModels(@NotNull BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
		itemModels.generateFlatItem(ModRegistry.CUSTOM_RECORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
	}
}
