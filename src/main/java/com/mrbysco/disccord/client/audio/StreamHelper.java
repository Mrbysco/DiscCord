package com.mrbysco.disccord.client.audio;

import com.mojang.blaze3d.audio.OggAudioStream;
import net.minecraft.Util;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.LoopingAudioStream;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class StreamHelper {
	/**
	 * Get the audio stream from the given resource location
	 *
	 * @param resourceLocation The resource location containing the minecraftified URL of the audio file
	 * @param isWrapper        Whether the audio stream should be wrapped in a looping audio stream
	 * @return A completable future containing the audio stream
	 */
	public static CompletableFuture<AudioStream> getStream(ResourceLocation resourceLocation, boolean isWrapper) {
		if (!resourceLocation.getNamespace().equals("disccord"))
			return null;

		if (resourceLocation.getPath().contains("placeholder_sound.ogg"))
			return null;

		String[] splitNamespace = resourceLocation.getPath().split("/");
		splitNamespace = Arrays.copyOfRange(splitNamespace, 2, splitNamespace.length);
		String fileUrl = String.join("/", splitNamespace);
		fileUrl = fileUrl.substring(0, fileUrl.length() - 4);

		String finalFileUrl = fileUrl;
		return CompletableFuture.supplyAsync(() -> {
			try {
				AudioHandlerClient audioHandler = new AudioHandlerClient();
				InputStream inputStream = audioHandler.getAudioInputStream(finalFileUrl);
				if (inputStream == null) {
					return null;
				}

				return isWrapper ? new LoopingAudioStream(OggAudioStream::new, inputStream) : new OggAudioStream(inputStream);
			} catch (IOException ioexception) {
				throw new CompletionException(ioexception);
			}
		}, Util.backgroundExecutor());
	}
}
