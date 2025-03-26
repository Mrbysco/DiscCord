package com.mrbysco.disccord.client;

import com.mrbysco.disccord.client.audio.AudioHandlerClient;
import com.mrbysco.disccord.client.audio.FileSound;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientHandler {
	public static final Map<Vec3, FileSound> playingSounds = new HashMap<>();
	public static final Map<UUID, FileSound> playingSoundsByUUID = new HashMap<>();

	public static void playRecord(Vec3 centerPos, String fileUrl, @NotNull UUID uuid) {
		Minecraft mc = Minecraft.getInstance();

		FileSound currentSound = !uuid.equals(Util.NIL_UUID) ?
				ClientHandler.playingSoundsByUUID.get(uuid) :
				ClientHandler.playingSounds.get(centerPos);
		if (currentSound != null) {
			mc.getSoundManager().stop(currentSound);

			if (!uuid.equals(Util.NIL_UUID))
				ClientHandler.playingSoundsByUUID.remove(uuid);
			else
				ClientHandler.playingSounds.remove(centerPos);
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

				if (!uuid.equals(Util.NIL_UUID))
					ClientHandler.playingSoundsByUUID.put(uuid, fileSound);
				else
					ClientHandler.playingSounds.put(centerPos, fileSound);

				mc.getSoundManager().play(fileSound);

				return null;
			});
			return;
		}

		FileSound fileSound = new FileSound();
		fileSound.position = centerPos;
		fileSound.fileUrl = fileUrl;

		if (!uuid.equals(Util.NIL_UUID))
			ClientHandler.playingSoundsByUUID.put(uuid, fileSound);
		else
			ClientHandler.playingSounds.put(centerPos, fileSound);

		mc.getSoundManager().play(fileSound);
	}
}
