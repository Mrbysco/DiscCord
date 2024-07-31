package com.mrbysco.disccord.datagen.server;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider {

	public ModItemTagsProvider(DataGenerator generator, BlockTagsProvider blockTagsProvider,
	                           ExistingFileHelper existingFileHelper) {
		super(generator, blockTagsProvider, DiscCordMod.MOD_ID, existingFileHelper);
	}

	@Override
	public void addTags() {
		this.tag(ItemTags.CREEPER_DROP_MUSIC_DISCS).add(ModRegistry.CUSTOM_RECORD.get());
	}
}
