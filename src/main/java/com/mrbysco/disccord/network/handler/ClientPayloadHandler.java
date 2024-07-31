package com.mrbysco.disccord.network.handler;

import com.mrbysco.disccord.network.payload.OpenMusicDiscScreenPayload;
import com.mrbysco.disccord.network.payload.PlayRecordPayload;
import com.mrbysco.disccord.registry.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
	private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

	@SuppressWarnings("SameReturnValue")
	public static ClientPayloadHandler getInstance() {
		return INSTANCE;
	}

	public void handleDiscScreen(final OpenMusicDiscScreenPayload data, final IPayloadContext context) {
		context.enqueueWork(() -> {
					if (FMLEnvironment.dist.isClient()) {
						ItemStack disc = data.disc();

						//Get the current URL from the disc
						String currentUrl = disc.getOrDefault(ModDataComponents.MUSIC_URL, "URL");
						com.mrbysco.disccord.client.screen.MusicDiscScreen.openScreen(Component.literal("DiscCord"), currentUrl);
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("disccord.networking.open_disc_screen.failed", e.getMessage()));
					return null;
				});
	}

	public void handleRecordPlay(final PlayRecordPayload data, final IPayloadContext context) {
		context.enqueueWork(() -> {
					if (FMLEnvironment.dist.isClient()) {
						Vec3 centerPos = data.pos().getCenter();
						String fileUrl = data.url();
						com.mrbysco.disccord.client.ClientHandler.playRecord(centerPos, fileUrl);
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("disccord.networking.play_record.failed", e.getMessage()));
					return null;
				});
	}
}
