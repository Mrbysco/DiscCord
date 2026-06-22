package com.mrbysco.disccord.datagen.server;

import com.mrbysco.disccord.registry.ModRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends FabricTagsProvider.ItemTagsProvider {

	public ModItemTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture,
	                           FabricTagsProvider.BlockTagsProvider blockTags) {
		super(output, completableFuture, blockTags);
	}

	@Override
	public void addTags(@NotNull HolderLookup.Provider lookupProvider) {
		this.builder(ItemTags.CREEPER_DROP_MUSIC_DISCS).add(ModRegistry.CUSTOM_RECORD.getResourceKey());
	}
}
