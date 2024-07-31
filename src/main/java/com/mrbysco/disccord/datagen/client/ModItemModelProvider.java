package com.mrbysco.disccord.datagen.client;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
	public ModItemModelProvider(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, DiscCordMod.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		generatedItem(ModRegistry.CUSTOM_RECORD.getId());
	}

	private void generatedItem(ResourceLocation location) {
		singleTexture(location.getPath(), new ResourceLocation("item/generated"),
				"layer0", DiscCordMod.modLoc("item/" + location.getPath()));
	}
}
