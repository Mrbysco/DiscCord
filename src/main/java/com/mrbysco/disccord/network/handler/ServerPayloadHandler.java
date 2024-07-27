package com.mrbysco.disccord.network.handler;

import com.mrbysco.disccord.config.DiscCordConfig;
import com.mrbysco.disccord.network.payload.SetRecordUrlPayload;
import com.mrbysco.disccord.registry.ModDataComponents;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.net.URL;

public class ServerPayloadHandler {
	public static final ServerPayloadHandler INSTANCE = new ServerPayloadHandler();

	@SuppressWarnings("SameReturnValue")
	public static ServerPayloadHandler getInstance() {
		return INSTANCE;
	}

	public void handleRecordUrl(final SetRecordUrlPayload data, final IPayloadContext context) {
		context.enqueueWork(() -> {
					if (context.player() != null) {
						Player player = context.player();
						ItemStack currentItem = player.getItemInHand(player.getUsedItemHand());

						if (!currentItem.is(ModRegistry.CUSTOM_RECORD.get())) {
							return;
						}

						try {
							new URL(data.url()).toURI();
						} catch (Exception e) {
							player.sendSystemMessage(Component.translatable("disccord.song_url.invalid"));
							return;
						}

						if (data.url().length() >= 400) {
							player.sendSystemMessage(Component.translatable("disccord.song_url.long"));
							return;
						}

						for (String url : DiscCordConfig.SERVER.whitelistedUrls.get()
						) {
							if (data.url().startsWith(url)) {
								player.playNotifySound(SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.BLOCKS, 1.0f, 1.0f);
								currentItem.set(ModDataComponents.MUSIC_URL.get(), data.url());
								return;
							}
						}
						// Probably need to format console message differently (too many Link Sources lead to crappy/long message)
						player.sendSystemMessage(Component.translatable("disccord.song_url.websites").append(",").append(DiscCordConfig.SERVER.whitelistedWebsites.get().toString()));
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("disccord.networking.set_record_url.failed", e.getMessage()));
					return null;
				});
	}
}
