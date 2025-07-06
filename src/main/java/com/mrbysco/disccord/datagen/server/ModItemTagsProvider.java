package com.mrbysco.disccord.datagen.server;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {

	public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, DiscCordMod.MOD_ID);
	}

	@Override
	public void addTags(@NotNull HolderLookup.Provider lookupProvider) {
		this.tag(ItemTags.CREEPER_DROP_MUSIC_DISCS).add(ModRegistry.CUSTOM_RECORD.get());
	}
}
