package com.mrbysco.disccord.client.audio;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.util.Hashing;
import net.neoforged.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class AudioHandlerClient {
	/**
	 * Check for the audio file related to the given URL
	 * @param urlName The URL to check for
	 * @return Whether the audio file exists
	 */
	public boolean checkForAudioFile(String urlName) {
		String hashedName = Hashing.Sha256(getMinecraftified(urlName));

		File audio = new File(FMLPaths.CONFIGDIR.get().resolve("disccord/client_downloads/" + hashedName + ".ogg").toString());

		return audio.exists();
	}

	/**
	 * Download the audio file from the given URL and convert it to OGG format
	 * @param urlName The URL to download the audio from
	 * @return A CompletableFuture that will be completed when the download is finished
	 */
	public CompletableFuture<Boolean> downloadVideoAsOgg(String urlName) {
		return CompletableFuture.supplyAsync(() -> {
			String hashedName = Hashing.Sha256(getMinecraftified(urlName));
			String audioIn = FMLPaths.CONFIGDIR.get().resolve("disccord/client_downloads/" + hashedName).toString();
			File audioOut = new File(FMLPaths.CONFIGDIR.get().resolve("disccord/client_downloads/" + hashedName + ".ogg").toString());

			String inPath;
			try {
				inPath = YoutubeDL.executeYoutubeDLCommand(String.format("-S res:144 -o \"%s\" %s --print after_move:filepath", audioIn, urlName));
			} catch (IOException | InterruptedException e) {
				throw new RuntimeException(e);
			}

			try {
				FFmpeg.executeFFmpegCommand(String.format("-i \"%s\" -c:a libvorbis -ac 1 -b:a 64k -vn -y -nostdin -nostats -loglevel 0 \"%s\"", inPath, audioOut.getAbsolutePath()));
			} catch (IOException | InterruptedException e) {
				throw new RuntimeException(e);
			}

			return true;
		});


	}

	/**
	 * Get an InputStream for the audio file related to the given URL
	 * @param urlName The URL to get the audio file for
	 * @return An InputStream for the audio file
	 */
	public InputStream getAudioInputStream(String urlName) {
		String hashedName = Hashing.Sha256(getMinecraftified(urlName));
		DiscCordMod.LOGGER.debug("Getting audio stream for {} with SHA256 {}", urlName, hashedName);
		File audio = new File(FMLPaths.CONFIGDIR.get().resolve("disccord/client_downloads/" + hashedName + ".ogg").toString());

		InputStream fileStream;
		try {
			fileStream = new FileInputStream(audio);
		} catch (FileNotFoundException e) {
			DiscCordMod.LOGGER.error("Failed to load audio stream", e);
			return null;
		}

		return fileStream;
	}

	/**
	 * Get the minecraftified version of the URL (Replacing all resource location invalid characters with underscores)
	 * @param url The URL to minecraftify
	 * @return The minecraftified URL
	 */
	private String getMinecraftified(String url) {
		return url.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9/._-]", "_");
	}
}
