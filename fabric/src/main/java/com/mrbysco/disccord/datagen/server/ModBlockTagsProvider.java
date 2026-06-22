package com.mrbysco.disccord.datagen.server;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup.Provider;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
	public ModBlockTagsProvider(FabricPackOutput output, CompletableFuture<Provider> completableFuture) {
		super(output, completableFuture);
	}

	@Override
	protected void addTags(Provider provider) {
	}
}