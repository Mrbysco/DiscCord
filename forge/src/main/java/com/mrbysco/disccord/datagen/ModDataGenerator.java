package com.mrbysco.disccord.datagen;

import com.mrbysco.disccord.datagen.client.ModItemModelProvider;
import com.mrbysco.disccord.datagen.client.ModLanguageProvider;
import com.mrbysco.disccord.datagen.client.ModSoundProvider;
import com.mrbysco.disccord.datagen.server.ModItemTagsProvider;
import com.mrbysco.disccord.datagen.server.ModRecipeProvider;
import com.mrbysco.disccord.registry.ModJukeboxSongs;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class ModDataGenerator {
	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.JUKEBOX_SONG, ModJukeboxSongs::bootstrap);

	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		event.createDatapackRegistryObjects(BUILDER);

		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(true, new ModRecipeProvider.Runner(packOutput, lookupProvider));
		generator.addProvider(true, new ModItemTagsProvider(packOutput, lookupProvider));

		generator.addProvider(true, new ModLanguageProvider(packOutput));
		generator.addProvider(true, new ModSoundProvider(packOutput));
		generator.addProvider(true, new ModItemModelProvider(packOutput));
	}
}
