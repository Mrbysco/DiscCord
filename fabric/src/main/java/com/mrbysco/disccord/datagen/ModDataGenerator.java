package com.mrbysco.disccord.datagen;

import com.mrbysco.disccord.datagen.client.ModItemModelProvider;
import com.mrbysco.disccord.datagen.client.ModLanguageProvider;
import com.mrbysco.disccord.datagen.client.ModSoundProvider;
import com.mrbysco.disccord.datagen.server.ModBlockTagsProvider;
import com.mrbysco.disccord.datagen.server.ModDatapackProvider;
import com.mrbysco.disccord.datagen.server.ModItemTagsProvider;
import com.mrbysco.disccord.datagen.server.ModRecipeProvider;
import com.mrbysco.disccord.registry.ModJukeboxSongs;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class ModDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		var pack = generator.createPack();

		pack.addProvider(ModItemModelProvider::new);
		pack.addProvider(ModLanguageProvider::new);
		pack.addProvider(ModSoundProvider::new);

		pack.addProvider(ModDatapackProvider::new);
		ModBlockTagsProvider blockTags = pack.addProvider(ModBlockTagsProvider::new);
		pack.addProvider((output, wrapperLookup) -> new ModItemTagsProvider(output, wrapperLookup, blockTags));
		pack.addProvider(ModRecipeProvider::new);

	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.JUKEBOX_SONG, ModJukeboxSongs::bootstrap);
	}
}