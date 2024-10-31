package com.mrbysco.disccord.datagen.client;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.Nullable;

public class ModLanguageProvider extends LanguageProvider {

	public ModLanguageProvider(PackOutput packOutput) {
		super(packOutput, DiscCordMod.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		addItem(ModRegistry.CUSTOM_RECORD, "Custom Record");
		add("item.disccord.custom_record.tooltip", "Current URL: %s");
		add("jukebox_song.disccord.placeholder_song", "Custom Song");

		addConfig("title", "DiscCord Config", null);
		addConfig("server", "Server", "Server Settings");
		addConfig("whitelistedWebsites", "Whitelisted Websites", "A list of website names to show when a player tries to use an unwhitelisted URL");
		addConfig("whitelistedUrls", "Whitelisted URLs", "List of whitelisted URLs to allow for playing music");
		addConfig("client", "Client", "Client Settings");
		addConfig("downloadFFmpeg", "Download FFmpeg", "Download FFmpeg executable if it's not found in the config folder. USE WITH CAUTION!\n" +
				"(This option downloads a zip file from a third party websites:\n" +
				"- https://evermeet.cx/ffmpeg/ffmpeg-6.1.zip (MAC OS)\n" +
				"- https://www.gyan.dev/ffmpeg/builds/ffmpeg-release-essentials.zip (Windows)\n" +
				")");
		addConfig("downloadYoutubeDL", "Download YoutubeDL", "Download YoutubeDL executable if it's not found in the config folder. USE WITH CAUTION!\n" +
				"(This option downloads the executable from the https://github.com/yt-dlp/yt-dlp/releases/latest/ github)");

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

	/**
	 * Add the translation for a config entry
	 *
	 * @param path        The path of the config entry
	 * @param name        The name of the config entry
	 * @param description The description of the config entry (optional in case of targeting "title" or similar entries that have no tooltip)
	 */
	private void addConfig(String path, String name, @Nullable String description) {
		this.add("disccord.configuration." + path, name);
		if (description != null && !description.isEmpty())
			this.add("disccord.configuration." + path + ".tooltip", description);
	}
}
