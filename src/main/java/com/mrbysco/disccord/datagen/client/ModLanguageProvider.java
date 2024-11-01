package com.mrbysco.disccord.datagen.client;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

	public ModLanguageProvider(PackOutput packOutput) {
		super(packOutput, DiscCordMod.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		addItem(ModRegistry.CUSTOM_RECORD, "Custom Record");
		add("item.disccord.custom_record.tooltip", "Current URL: %s");
		add("item.disccord.custom_record.desc", "Custom Song");

		add("disccord.song_url.invalid", "Song URL is invalid!");
		add("disccord.song_url.long", "Song URL is too long!");
		add("disccord.song_url.websites", "Song URL must be a %s URL!");
		add("disccord.song.downloading", "Downloading music, please wait a moment...");
		add("disccord.song.downloading_failed", "Downloading music failed");
		add("disccord.song.transcoding_failed", "Transcoding music failed");
		add("disccord.song.succeed", "Downloading complete!");
		add("disccord.song.failed", "Failed to download music!");
		add("disccord.ffmpeg.missing", "Could not find FFmpeg executable, check the log for more information!");
		add("disccord.youtubedl.missing", "Could not find YoutubeDL executable, check the log for more information!");
		add("disccord.executable.permission", "The '%s' executable isn't executeable, please make sure to add the executeable permission with chmod +x");

		add("disccord.networking.set_record_url.failed", "Failed to set record URL: %s");
		add("disccord.networking.open_disc_screen.failed", "Failed to open disc screen: %s");
		add("disccord.networking.play_record.failed", "Failed to play record: %s");
	}
}
