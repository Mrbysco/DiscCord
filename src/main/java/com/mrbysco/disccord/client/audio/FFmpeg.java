package com.mrbysco.disccord.client.audio;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.config.DiscCordConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.loading.FMLPaths;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This class is responsible for checking if the 'ffmpeg' executable is in the path, if not it will download it if the user has enabled the option
 */
public class FFmpeg {
	static String ffmpegPath = null;

	/**
	 * Checks if the 'ffmpeg' executable is in the path, if not it will download it if the user has enabled the option
	 * @throws IOException If an I/O error occurs
	 */
	static void checkForExecutable() throws IOException {
		Optional<String> pathExecutable = PathTools.traversePath("ffmpeg");
		if (!pathExecutable.isEmpty()) {
			ffmpegPath = pathExecutable.get().toString();
			return;
		}

		File FFmpegDirectory = FMLPaths.CONFIGDIR.get().resolve("disccord/ffmpeg/").toAbsolutePath().toFile();

		if (!FFmpegDirectory.exists() && !FFmpegDirectory.mkdirs()) {
			DiscCordMod.LOGGER.error("Failed to create the 'disccord/ffmpeg' directory");
			return;
		}

		String fileName = SystemUtils.IS_OS_WINDOWS ? "ffmpeg.exe" : "ffmpeg";
		File ffmpegFile = FFmpegDirectory.toPath().resolve(fileName).toFile();
		Minecraft mc = Minecraft.getInstance();
		if (!ffmpegFile.exists()) {
			if (DiscCordConfig.CLIENT.downloadFFmpeg.get()) {
				File zipFile = FFmpegDirectory.toPath().resolve("ffmpeg.zip").toFile();

				InputStream inputStream = null;

				if (!FFmpegDirectory.toPath().resolve("ffmpeg.zip").toFile().exists()) {
					if (SystemUtils.IS_OS_MAC) {
						inputStream = new URL("https://evermeet.cx/ffmpeg/ffmpeg-6.1.zip").openStream();
					} else if (SystemUtils.IS_OS_WINDOWS) {
						inputStream = new URL("https://www.gyan.dev/ffmpeg/builds/ffmpeg-release-essentials.zip").openStream();
					} else if (SystemUtils.IS_OS_LINUX) {
						DiscCordMod.LOGGER.error("Automatic Linux ffmpeg install is not supported");
					}
				}

				if (inputStream != null) {
					Files.copy(inputStream, FFmpegDirectory.toPath().resolve("ffmpeg.zip"), StandardCopyOption.REPLACE_EXISTING);
				} else {
					DiscCordMod.LOGGER.error("Failed to download ffmpeg");
					return;
				}

				if (!zipFile.exists()) {
					return;
				}

				ZipInputStream zipInput = new ZipInputStream(new FileInputStream(zipFile));

				ZipEntry zipEntry = zipInput.getNextEntry();

				while (zipEntry != null) {
					if (zipEntry.getName().endsWith("ffmpeg.exe") || zipEntry.getName().endsWith("ffmpeg")) {
						Path outPath = FFmpegDirectory.toPath().resolve(fileName);
						Files.copy(zipInput, outPath, StandardCopyOption.REPLACE_EXISTING);
						ffmpegPath = outPath.toString();
						outPath.toFile().setExecutable(true);
					}
					zipEntry = zipInput.getNextEntry();
				}

				if (!zipFile.delete())
					DiscCordMod.LOGGER.error("Failed to delete the {} file", zipFile.getName());
			} else {
				if (mc.player != null) {
					mc.player.displayClientMessage(
							Component.translatable("disccord.ffmpeg.missing").withStyle(ChatFormatting.RED), false);
				}
				for (String message : getMissingMessage()) {
					DiscCordMod.LOGGER.error(message);
				}
			}
		} else {
			if (SystemUtils.IS_OS_WINDOWS || ffmpegFile.canExecute()) {
				ffmpegPath = ffmpegFile.getAbsolutePath();
			} else {
				if (mc.player != null) {
					mc.player.displayClientMessage(
							Component.translatable("disccord.executable.permission", ffmpegFile.getName()).withStyle(ChatFormatting.RED), false);
				}
				DiscCordMod.LOGGER.error("The '{}' executable isn't executeable, please make sure to add the executeable permission with chmod +x", ffmpegFile.getName());
			}
		}
	}

	/**
	 * Executes a command using the 'ffmpeg' executable
	 * @param arguments The arguments to pass to the 'ffmpeg' executable
	 * @throws IOException If an I/O error occurs
	 * @throws InterruptedException If the process is interrupted
	 */
	static void executeFFmpegCommand(String arguments) throws IOException, InterruptedException {
		if (ffmpegPath == null || ! new File(ffmpegPath).canExecute()) {
			checkForExecutable();
		}

		String cmd = ffmpegPath + " " + arguments;
		DiscCordMod.LOGGER.debug("Executing '{}'", cmd);
		Process resultProcess;
		if (SystemUtils.IS_OS_LINUX) {
			String[] cmds = {"/bin/sh", "-c", cmd};
			resultProcess = Runtime.getRuntime().exec(cmds);
		} else {
			resultProcess = Runtime.getRuntime().exec(cmd);
		}

		int result = resultProcess.waitFor();
		if (result != 0) {
			throw new IOException("Process exited with error code " + result);
		}
	}

	/**
	 * @return The message displayed when the 'ffmpeg' executable is missing
	 */
	static String[] getMissingMessage() {
		if (SystemUtils.IS_OS_WINDOWS) {
			return new String[]{
					"ERROR: 'ffmpeg.exe' wasn't found in the 'config/disccord/ffmpeg' folder!",
					"Please visit one of the following URLs to download it:",
					" - https://www.gyan.dev/ffmpeg/builds/",
					" - https://github.com/BtbN/FFmpeg-Builds/releases",
					"",
					"Locate the 'bin/ffmpeg.exe' file in the .zip and place it in the 'config/disccord/ffmpeg' folder"
			};
		} else if (SystemUtils.IS_OS_MAC) {
			return new String[]{
					"ERROR: 'ffmpeg' wasn't found in the 'config/disccord/ffmpeg' folder!",
					"Please visit one the following URL to download it:",
					" - https://ffmpeg.org/download.html#build-mac",
					"",
					"Locate the 'bin/ffmpeg' file in the .7x/.zip and place it in the 'config/disccord/ffmpeg' folder"
			};
		} else if (SystemUtils.IS_OS_LINUX) {
			return new String[]{
					"ERROR: ffmpeg is not installed on this system!",
					"Please install ffmpeg",
					" - Debian: apt install ffmpeg",
					" - Ubuntu: apt install ffmpeg",
					" - Fedora: dnf install ffmpeg",
					" - Arch: pacman -S ffmpeg",
					"",
					"Alternatively, place the 'ffmpeg' executable in the ",
					"'config/disccord/ffmpeg' folder of this repository"
			};
		}
		return new String[0];
	}
}
