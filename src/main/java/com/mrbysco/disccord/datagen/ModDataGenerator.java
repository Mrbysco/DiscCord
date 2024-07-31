package com.mrbysco.disccord.datagen;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.datagen.client.ModItemModelProvider;
import com.mrbysco.disccord.datagen.client.ModLanguageProvider;
import com.mrbysco.disccord.datagen.client.ModSoundProvider;
import com.mrbysco.disccord.datagen.server.ModItemTagsProvider;
import com.mrbysco.disccord.datagen.server.ModRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenerator {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(event.includeServer(), new ModRecipeProvider(generator));
			BlockTagsProvider blockTags = new BlockTagsProvider(generator, DiscCordMod.MOD_ID, helper) {
				@Override
				protected void addTags() {

				}
			};
			generator.addProvider(event.includeServer(), blockTags);
			generator.addProvider(event.includeServer(), new ModItemTagsProvider(generator, blockTags, helper));
		}
		if (event.includeClient()) {
			generator.addProvider(event.includeClient(), new ModLanguageProvider(generator));
			generator.addProvider(event.includeClient(), new ModSoundProvider(generator, helper));
			generator.addProvider(event.includeClient(), new ModItemModelProvider(generator, helper));
		}
	}
}
