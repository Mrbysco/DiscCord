package com.mrbysco.disccord.client;

import com.mrbysco.disccord.client.audio.AudioHandlerClient;
import com.mrbysco.disccord.client.audio.FileSound;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;

public class ClientHandler {
	public static final HashMap<Vec3, FileSound> playingSounds = new HashMap<>();

	public static void playRecord(Vec3 centerPos, String fileUrl) {
		Minecraft mc = Minecraft.getInstance();

		FileSound currentSound = ClientHandler.playingSounds.get(centerPos);

		if (currentSound != null) {
			mc.getSoundManager().stop(currentSound);
		}

		if (fileUrl.isEmpty()) {
			return;
		}

		AudioHandlerClient audioHandler = new AudioHandlerClient();

		if (!audioHandler.checkForAudioFile(fileUrl)) {
			mc.player.sendSystemMessage(Component.translatable("disccord.song.downloading"));

			audioHandler.downloadVideoAsOgg(fileUrl).thenApply((in) -> {
				mc.player.sendSystemMessage(Component.translatable("disccord.song.succeed"));

				FileSound fileSound = new FileSound();
				fileSound.position = centerPos;
				fileSound.fileUrl = fileUrl;

				ClientHandler.playingSounds.put(centerPos, fileSound);

				mc.getSoundManager().play(fileSound);

				return null;
			});
			return;
		}

		FileSound fileSound = new FileSound();
		fileSound.position = centerPos;
		fileSound.fileUrl = fileUrl;

		ClientHandler.playingSounds.put(centerPos, fileSound);

		mc.getSoundManager().play(fileSound);
	}
}
