package com.mrbysco.disccord.datagen.client;

import com.mrbysco.disccord.registry.ModRegistry;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

public class ModItemModelProvider extends FabricModelProvider {
	public ModItemModelProvider(FabricPackOutput packOutput) {
		super(packOutput);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModels) {
		itemModels.generateFlatItem(ModRegistry.CUSTOM_RECORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
	}
}
