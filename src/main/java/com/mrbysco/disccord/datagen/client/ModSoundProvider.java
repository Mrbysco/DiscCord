package com.mrbysco.disccord.datagen.client;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class ModSoundProvider extends SoundDefinitionsProvider {

	public ModSoundProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, DiscCordMod.MOD_ID, existingFileHelper);
	}

	@Override
	public void registerSounds() {
		this.add(ModRegistry.PLACEHOLDER_SOUND, definition()
				.with(sound(modLoc("placeholder_sound")).stream(true)));
	}

	public ResourceLocation modLoc(String path) {
		return DiscCordMod.modLoc(path);
	}
}
