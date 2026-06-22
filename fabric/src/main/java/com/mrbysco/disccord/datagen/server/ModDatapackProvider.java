package com.mrbysco.disccord.datagen.server;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;

import java.util.concurrent.CompletableFuture;

public class ModDatapackProvider extends FabricDynamicRegistryProvider {

	public ModDatapackProvider(FabricPackOutput output, CompletableFuture<Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(Provider registries, Entries entries) {
		entries.addAll(registries.lookupOrThrow(Registries.JUKEBOX_SONG));
	}

	@Override
	public String getName() {
		return "DiscCord datapack";
	}
}
