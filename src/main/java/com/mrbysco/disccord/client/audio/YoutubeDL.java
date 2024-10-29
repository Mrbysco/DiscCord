package com.mrbysco.disccord.client.audio;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.config.DiscCordConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.loading.FMLPaths;
import org.apache.commons.lang3.SystemUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

/**
 * This class is responsible for checking if the 'yt-dlp' executable is in the path, if not it will download it if the user has enabled the option
 */
public class YoutubeDL {
	static String youtubedlPath = null;

	/**
	 * Checks if the 'yt-dlp' executable is in the path, if not it will download it if the user has enabled the option
	 * @throws IOException If an I/O error occurs
	 */
	static void checkForExecutable() throws IOException {
		String fileName = switch (Util.getPlatform()) {
			case LINUX -> "yt-dlp_linux";
			case OSX -> "yt-dlp_macos";
			default -> "yt-dlp.exe";
		};
		Optional<String> pathExecutable = PathTools.traversePath("yt-dlp");
		if (!pathExecutable.isEmpty()) {
			youtubedlPath = pathExecutable.get().toString();
			return;
		}
		File YoutubeDLDirectory = FMLPaths.CONFIGDIR.get().resolve("disccord/youtubedl/").toAbsolutePath().toFile();

		if (!YoutubeDLDirectory.exists() && !YoutubeDLDirectory.mkdirs()) {
			DiscCordMod.LOGGER.error("Failed to create the 'disccord/youtubedl' directory");
			return;
		}

		File ffmpegFile = YoutubeDLDirectory.toPath().resolve(fileName).toFile();

		Minecraft mc = Minecraft.getInstance();
		if (!ffmpegFile.exists()) {
			if (DiscCordConfig.CLIENT.downloadYoutubeDL.get()) {
				InputStream inputStream = null;

				if (SystemUtils.IS_OS_LINUX) {
					inputStream = new URL("https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp_linux").openStream();
				} else if (SystemUtils.IS_OS_MAC) {
					inputStream = new URL("https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp_macos").openStream();
				} else if (SystemUtils.IS_OS_WINDOWS) {
					inputStream = new URL("https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp.exe").openStream();
				}

				if (inputStream != null) {
					Path outPath = YoutubeDLDirectory.toPath().resolve(fileName);
					Files.copy(inputStream, outPath, StandardCopyOption.REPLACE_EXISTING);
					outPath.toFile().setExecutable(true);
				} else {
					DiscCordMod.LOGGER.error("Failed to download the yt-dlp executable");
				}
			} else {
				if (mc.player != null) {
					mc.player.sendSystemMessage(Component.translatable("disccord.youtubedl.missing").withStyle(ChatFormatting.RED));
				}
				for (String message : getMissingMessage()) {
					DiscCordMod.LOGGER.error(message);
				}
			}
		} else {
			if (SystemUtils.IS_OS_WINDOWS || ffmpegFile.canExecute()) {
				youtubedlPath = ffmpegFile.getAbsolutePath();
			} else {
				if (mc.player != null) {
					mc.player.sendSystemMessage(Component.translatable("disccord.executable.permission", ffmpegFile.getName()).withStyle(ChatFormatting.RED));
				}
				DiscCordMod.LOGGER.error("The '{}' executable isn't executeable, please make sure to add the executeable permission with chmod +x", ffmpegFile.getName());
			}
		}
	}

	/**
	 * Executes a command using the 'yt-dlp' executable
	 * @param arguments The arguments to pass to the 'yt-dlp' executable
	 * @throws IOException If an I/O error occurs
	 * @throws InterruptedException If the process is interrupted
	 */
	static String executeYoutubeDLCommand(String arguments) throws IOException, InterruptedException {
		if (youtubedlPath == null || ! new File(youtubedlPath).canExecute()) {
			checkForExecutable();
		}

		String cmd = youtubedlPath + " " + arguments;
		DiscCordMod.LOGGER.debug("Executing '{}'", cmd);
		Process resultProcess;
		if (SystemUtils.IS_OS_LINUX) {
			String[] cmds = { "/bin/sh", "-c", cmd };
			resultProcess = Runtime.getRuntime().exec(cmds);
		} else {
			resultProcess = Runtime.getRuntime().exec(cmd);
		}

		BufferedReader stdInput = new BufferedReader(new 
			InputStreamReader(resultProcess.getInputStream()));

		String output = "";
		String s = null;
		while ((s = stdInput.readLine()) != null) {
			output += s;
		}

		int result = resultProcess.waitFor();
		if (result != 0) {
			throw new IOException("Process exited with error code " + result);
		}
		return output;
	}

	/**
	 * @return The message displayed when the 'yt-dlp' executable is missing
	 */
	static String[] getMissingMessage() {
		return new String[]{
				"ERROR: 'yt-dlp' executable wasn't found in the 'config/disccord/youtubedl' folder!",
				"Please visit one of the following URLs to download it:",
				" - https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp.exe (Windows)",
				" - https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp_linux (Linux)",
				" - https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp_macos (Mac OS)",
				"",
				"Place the downloaded executable in the 'config/disccord/youtubedl' folder"
		};
	}
}
