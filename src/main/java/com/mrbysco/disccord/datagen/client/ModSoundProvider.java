package com.mrbysco.disccord.datagen.client;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ModSoundProvider extends SoundDefinitionsProvider {

	public ModSoundProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
		super(packOutput, DiscCordMod.MOD_ID, existingFileHelper);
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
