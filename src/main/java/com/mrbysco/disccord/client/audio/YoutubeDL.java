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
		if (checkYoutubeDLPath("yt-dlp")) {
			youtubedlPath = "yt-dlp";
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
	 * Checks if the 'yt-dlp' executable is in the path
	 * @param fileName The name of the 'yt-dlp' executable
	 * @return Whether the 'yt-dlp' executable is present
	 */
	static boolean checkYoutubeDLPath(String fileName) {
		// Check if running the executable with the '--version' argument works
		ProcessBuilder builder = new ProcessBuilder(fileName, "--version");
		Process process;
		try {
			process = builder.start();
		} catch (IOException ex) {
			return false;
		}
		int exitCode;
		while (true) {
			try {
				exitCode = process.waitFor();
				break;
			} catch (InterruptedException ignored) {
			}
		}
		return exitCode == 0;
	}

	/**
	 * Executes a command using the 'yt-dlp' executable
	 * @param arguments The arguments to pass to the 'yt-dlp' executable
	 * @throws IOException If an I/O error occurs
	 * @throws InterruptedException If the process is interrupted
	 */
	static String executeYoutubeDLCommand(String arguments) throws IOException, InterruptedException {
		if (youtubedlPath == null) {
			checkForExecutable();
		}
		if (checkYoutubeDLPath(youtubedlPath)) {
			String cmd = youtubedlPath + " " + arguments;
//			DiscCordMod.LOGGER.error("Executing '{}'", cmd);
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
			return output;
		} else {
			DiscCordMod.LOGGER.error("'{}' isn't executable", youtubedlPath);
			throw new IOException("Unable to execute " + youtubedlPath);
		}
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
