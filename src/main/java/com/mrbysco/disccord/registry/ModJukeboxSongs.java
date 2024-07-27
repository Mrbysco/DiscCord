package com.mrbysco.disccord.registry;

import com.mrbysco.disccord.DiscCordMod;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;

public class ModJukeboxSongs {
	public static final ResourceKey<JukeboxSong> PLACEHOLDER_SONG = create();

	private static ResourceKey<JukeboxSong> create() {
		return ResourceKey.create(Registries.JUKEBOX_SONG, DiscCordMod.modLoc("placeholder_song"));
	}

	public static void bootstrap(BootstrapContext<JukeboxSong> context) {
		registerPlaceholder(context, ModRegistry.PLACEHOLDER_SOUND.getDelegate());
	}

	private static void registerPlaceholder(
			BootstrapContext<JukeboxSong> context,
			Holder<SoundEvent> soundEvent
	) {
		context.register(
				ModJukeboxSongs.PLACEHOLDER_SONG,
				new JukeboxSong(soundEvent,
						Component.translatable(Util.makeDescriptionId("jukebox_song", ModJukeboxSongs.PLACEHOLDER_SONG.location())),
						(float) 1, 0)
		);
	}
}
