package com.mrbysco.disccord.network.handler;

import com.mrbysco.disccord.client.ClientHandler;
import com.mrbysco.disccord.client.audio.AudioHandlerClient;
import com.mrbysco.disccord.client.audio.FileSound;
import com.mrbysco.disccord.client.screen.MusicDiscScreen;
import com.mrbysco.disccord.network.payload.OpenMusicDiscScreenPayload;
import com.mrbysco.disccord.network.payload.PlayRecordPayload;
import com.mrbysco.disccord.registry.ModDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
	private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

	@SuppressWarnings("SameReturnValue")
	public static ClientPayloadHandler getInstance() {
		return INSTANCE;
	}

	public void handleDiscScreen(final OpenMusicDiscScreenPayload data, final IPayloadContext context) {
		context.enqueueWork(() -> {
					Minecraft mc = Minecraft.getInstance();
					ItemStack disc = data.disc();

					//Get the current URL from the disc
					String currentUrl = disc.getOrDefault(ModDataComponents.MUSIC_URL, "URL");
					mc.setScreen(new MusicDiscScreen(Component.literal("DiscCord"), currentUrl));

				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("disccord.networking.open_disc_screen.failed", e.getMessage()));
					return null;
				});
	}

	public void handleRecordPlay(final PlayRecordPayload data, final IPayloadContext context) {
		context.enqueueWork(() -> {
					Minecraft mc = Minecraft.getInstance();
					Vec3 centerPos = data.pos().getCenter();
					String fileUrl = data.url();

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
				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("disccord.networking.play_record.failed", e.getMessage()));
					return null;
				});
	}
}
