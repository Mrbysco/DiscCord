package com.mrbysco.disccord.datagen;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.datagen.client.ModItemModelProvider;
import com.mrbysco.disccord.datagen.client.ModLanguageProvider;
import com.mrbysco.disccord.datagen.client.ModSoundProvider;
import com.mrbysco.disccord.datagen.server.ModItemTagsProvider;
import com.mrbysco.disccord.datagen.server.ModRecipeProvider;
import com.mrbysco.disccord.registry.ModJukeboxSongs;
import net.minecraft.core.Cloner;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.VanillaRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ModDataGenerator {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		if (event.includeServer()) {
			generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput, lookupProvider));
			BlockTagsProvider blockTags = new BlockTagsProvider(packOutput, lookupProvider, DiscCordMod.MOD_ID, helper) {
				@Override
				protected void addTags(HolderLookup.Provider provider) {

				}
			};
			generator.addProvider(event.includeServer(), blockTags);
			generator.addProvider(event.includeServer(), new ModItemTagsProvider(packOutput, lookupProvider, blockTags, helper));
			generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(
					packOutput, CompletableFuture.supplyAsync(ModDataGenerator::getProvider), Set.of(DiscCordMod.MOD_ID)));
		}
		if (event.includeClient()) {
			generator.addProvider(event.includeClient(), new ModLanguageProvider(packOutput));
			generator.addProvider(event.includeClient(), new ModSoundProvider(packOutput, helper));
			generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, helper));
		}
	}

	private static RegistrySetBuilder.PatchedRegistries getProvider() {
		final RegistrySetBuilder registryBuilder = new RegistrySetBuilder();
		registryBuilder.add(Registries.JUKEBOX_SONG, ModJukeboxSongs::bootstrap);
		// We need the BIOME registry to be present, so we can use a biome tag, doesn't matter that it's empty
		registryBuilder.add(Registries.BIOME, $ -> {
		});
		RegistryAccess.Frozen regAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
		Cloner.Factory cloner$factory = new Cloner.Factory();
		net.neoforged.neoforge.registries.DataPackRegistriesHooks.getDataPackRegistriesWithDimensions().forEach(data -> data.runWithArguments(cloner$factory::addCodec));
		return registryBuilder.buildPatch(regAccess, VanillaRegistries.createLookup(), cloner$factory);
	}
}
