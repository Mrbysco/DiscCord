package com.mrbysco.disccord.datagen.client;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
	public ModItemModelProvider(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, DiscCordMod.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		generatedItem(ModRegistry.CUSTOM_RECORD.getId());
	}

	private void generatedItem(ResourceLocation location) {
		singleTexture(location.getPath(), ResourceLocation.withDefaultNamespace("item/generated"),
				"layer0", DiscCordMod.modLoc("item/" + location.getPath()));
	}
}
