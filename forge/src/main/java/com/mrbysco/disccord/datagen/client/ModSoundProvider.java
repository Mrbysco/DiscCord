package com.mrbysco.disccord.datagen.client;

import com.mrbysco.disccord.Reference;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ModSoundProvider extends SoundDefinitionsProvider {

	public ModSoundProvider(PackOutput packOutput) {
		super(packOutput, Reference.MOD_ID);
	}

	@Override
	public void registerSounds() {
		this.add(ModRegistry.PLACEHOLDER_SOUND.asHolder(), definition()
				.with(sound(modLoc("placeholder_sound")).stream(true)));
	}

	public Identifier modLoc(String path) {
		return Reference.modLoc(path);
	}
}
