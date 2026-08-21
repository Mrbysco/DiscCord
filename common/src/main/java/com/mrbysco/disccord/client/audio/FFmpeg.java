package com.mrbysco.disccord.client.audio;

import com.mrbysco.disccord.Reference;
import com.mrbysco.disccord.config.DiscCordConfig;
import com.mrbysco.disccord.platform.Services;
import com.mrbysco.disccord.util.ArchiveUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * This class is responsible for checking if the 'ffmpeg' executable is in the path, if not it will download it if the user has enabled the option
 */
public class FFmpeg {
	static String ffmpegPath = null;

	/**
	 * Checks if the 'ffmpeg' executable is in the path, if not it will download it if the user has enabled the option
	 */
	static void checkForExecutable() {
		Optional<String> pathExecutable = PathTools.traversePath("ffmpeg");
		if (!pathExecutable.isEmpty()) {
			ffmpegPath = pathExecutable.get().toString();
			return;
		}

		File FFmpegDirectory = Services.PLATFORM.getConfigFolder().resolve("disccord/ffmpeg/").toAbsolutePath().toFile();

		if (!FFmpegDirectory.exists() && !FFmpegDirectory.mkdirs()) {
			Reference.LOGGER.error("Failed to create the 'disccord/ffmpeg' directory");
			return;
		}

		String fileName = SystemUtils.IS_OS_WINDOWS ? "ffmpeg.exe" : "ffmpeg";
		File ffmpegFile = FFmpegDirectory.toPath().resolve(fileName).toFile();
		Minecraft mc = Minecraft.getInstance();
		if (!ffmpegFile.exists()) {
			if (DiscCordConfig.CLIENT.downloadFFmpeg.get()) {
				String archiveFileName;
				String downloadUrl;

				if (SystemUtils.IS_OS_MAC) {
					archiveFileName = "ffmpeg.zip";
					downloadUrl = "https://evermeet.cx/ffmpeg/ffmpeg-6.1.zip";
				} else if (SystemUtils.IS_OS_WINDOWS) {
					archiveFileName = "ffmpeg.zip";
					downloadUrl = "https://github.com/BtbN/FFmpeg-Builds/releases/download/latest/ffmpeg-master-latest-win64-gpl.zip";
				} else if (SystemUtils.IS_OS_LINUX) {
					archiveFileName = "ffmpeg.tar.xz";
					downloadUrl = "https://github.com/BtbN/FFmpeg-Builds/releases/download/latest/ffmpeg-master-latest-linux64-gpl.tar.xz";
				} else {
					Reference.LOGGER.error("Unsupported operating system for automatic ffmpeg download");
					return;
				}

				File archiveFile = FFmpegDirectory.toPath().resolve(archiveFileName).toFile();

				if (!archiveFile.exists()) {
					try (InputStream inputStream = new URL(downloadUrl).openStream()) {
						Files.copy(inputStream, archiveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
						Reference.LOGGER.error("Failed to download ffmpeg", e);
						return;
					}
				}

				if (!archiveFile.exists()) {
					return;
				}

				try {
					ArchiveUtils.extract(archiveFile.toPath(), FFmpegDirectory.toPath());

					Optional<Path> foundFFmpeg = Files.walk(FFmpegDirectory.toPath())
							.filter(Files::isRegularFile)
							.filter(p -> p.getFileName().toString().equals(fileName))
							.findFirst();

					if (foundFFmpeg.isPresent()) {
						ffmpegPath = foundFFmpeg.get().toAbsolutePath().toString();
						foundFFmpeg.get().toFile().setExecutable(true);
					} else {
						Reference.LOGGER.error("Could not find ffmpeg binary in extracted archive");
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					Reference.LOGGER.error("Extraction interrupted", e);
				} catch (IOException e) {
					Reference.LOGGER.error("Failed to extract ffmpeg", e);
				}

				if (!archiveFile.delete())
					Reference.LOGGER.error("Failed to delete the {} file", archiveFile.getName());
			} else {
				if (mc.player != null) {
					mc.player.sendSystemMessage(Component.translatable("disccord.ffmpeg.missing").withStyle(ChatFormatting.RED));
				}
				for (String message : getMissingMessage()) {
					Reference.LOGGER.error(message);
				}
			}
		} else {
			if (SystemUtils.IS_OS_WINDOWS || ffmpegFile.canExecute()) {
				ffmpegPath = ffmpegFile.getAbsolutePath();
			} else {
				if (mc.player != null) {
					mc.player.sendSystemMessage(Component.translatable("disccord.executable.permission", ffmpegFile.getName()).withStyle(ChatFormatting.RED));
				}
				Reference.LOGGER.error("The '{}' executable isn't executeable, please make sure to add the executeable permission with chmod +x", ffmpegFile.getName());
			}
		}
	}

	/**
	 * Executes a command using the 'ffmpeg' executable
	 *
	 * @param arguments The arguments to pass to the 'ffmpeg' executable
	 * @throws IOException          If an I/O error occurs
	 * @throws InterruptedException If the process is interrupted
	 */
	static void executeFFmpegCommand(String... arguments) throws IOException, InterruptedException {
		if (ffmpegPath == null || !new File(ffmpegPath).canExecute()) {
			checkForExecutable();
		}

		List<String> cmdList = new ArrayList<>();
		Process resultProcess;
		if (SystemUtils.IS_OS_LINUX) {
			cmdList.add("/bin/sh");
			cmdList.add("-c");
			cmdList.add("\"" + ffmpegPath + "\" " + String.join(" ", arguments));
		} else {
			cmdList.add(ffmpegPath);
			Collections.addAll(cmdList, arguments);
		}
		Reference.LOGGER.debug("Executing '{}'", String.join(" ", cmdList));
		resultProcess = Runtime.getRuntime().exec(cmdList.toArray(new String[0]));
		resultProcess.getOutputStream().close();

		int result = resultProcess.waitFor();
		if (result != 0) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(resultProcess.getErrorStream()))) {
				reader.lines().forEach(line -> Reference.LOGGER.error("FFmpeg: {}", line));
			}
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
