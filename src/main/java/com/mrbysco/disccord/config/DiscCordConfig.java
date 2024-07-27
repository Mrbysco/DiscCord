package com.mrbysco.disccord.config;

import com.mrbysco.disccord.DiscCordMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class DiscCordConfig {

	public static class Server {
		public final ModConfigSpec.ConfigValue<List<? extends String>> whitelistedWebsites;
		public final ModConfigSpec.ConfigValue<List<? extends String>> whitelistedUrls;

		Server(ModConfigSpec.Builder builder) {
			builder.comment("Server settings")
					.push("server");

			whitelistedWebsites = builder
					.comment("A list of website names to show when a player tries to use an unwhitelisted URL")
					.defineList("whitelistedWebsites", List.of("Youtube", "Discord", "GDrive", "Dropbox"), () -> "", String.class::isInstance);

			whitelistedUrls = builder
					.comment("List of whitelisted URLs to allow for playing music")
					.defineList("whitelistedUrls", List.of(
							"https://youtu.be", "https://www.youtube.com", "https://youtube.com",
							"https://cdn.discordapp.com", "https://drive.google.com/uc",
							"https://www.dropbox.com/scl", "https://dropbox.com/scl"), () -> "", String.class::isInstance);

			builder.pop();
		}
	}

	public static final ModConfigSpec serverSpec;
	public static final Server SERVER;

	static {
		final Pair<Server, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Server::new);
		serverSpec = specPair.getRight();
		SERVER = specPair.getLeft();
	}

	public static class Client {
		public final ModConfigSpec.BooleanValue downloadFFmpeg;
		public final ModConfigSpec.BooleanValue downloadYoutubeDL;

		Client(ModConfigSpec.Builder builder) {
			builder.comment("Client settings")
					.push("client");

			downloadFFmpeg = builder
					.comment("Download FFmpeg executable if it's not found in the config folder. USE WITH CAUTION!\n" +
							"(This option downloads a zip file from a third party websites:\n" +
							"- https://evermeet.cx/ffmpeg/ffmpeg-6.1.zip (MAC OS)\n" +
							"- https://www.gyan.dev/ffmpeg/builds/ffmpeg-release-essentials.zip (Windows)\n" +
							")")
					.define("downloadFFmpeg", false);

			downloadYoutubeDL = builder
					.comment("Download YoutubeDL executable if it's not found in the config folder. USE WITH CAUTION!\n" +
							"(This option downloads the executable from the https://github.com/yt-dlp/yt-dlp/releases/latest/ github)")
					.define("downloadYoutubeDL", false);

			builder.pop();
		}
	}

	public static final ModConfigSpec clientSpec;
	public static final Client CLIENT;

	static {
		final Pair<Client, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Client::new);
		clientSpec = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent.Loading configEvent) {
		DiscCordMod.LOGGER.debug("Loaded DiscCord's config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
		DiscCordMod.LOGGER.warn("DiscCord's config just got changed on the file system!");
	}
}
