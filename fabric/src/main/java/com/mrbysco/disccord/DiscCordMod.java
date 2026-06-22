package com.mrbysco.disccord;

import com.mrbysco.disccord.config.DiscCordConfig;
import com.mrbysco.disccord.network.payload.OpenMusicDiscScreenPayload;
import com.mrbysco.disccord.network.payload.PlayRecordPayload;
import com.mrbysco.disccord.network.payload.SetRecordUrlPayload;
import com.mrbysco.disccord.registry.ModDataComponents;
import com.mrbysco.disccord.registry.ModRegistry;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.config.ModConfig;

import java.net.URL;

public class DiscCordMod implements ModInitializer {
	@Override
	public void onInitialize() {
		ConfigRegistry.INSTANCE.register(Reference.MOD_ID, ModConfig.Type.SERVER, DiscCordConfig.serverSpec);

		Reference.initRegistries();

		PayloadTypeRegistry.clientboundPlay().register(OpenMusicDiscScreenPayload.ID, OpenMusicDiscScreenPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(PlayRecordPayload.ID, PlayRecordPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(SetRecordUrlPayload.ID, SetRecordUrlPayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(SetRecordUrlPayload.ID, (payload, context) -> {
			final ServerPlayer player = context.player();

			ItemStack currentItem = player.getItemInHand(player.getUsedItemHand());

			if (!currentItem.is(ModRegistry.CUSTOM_RECORD.get())) {
				return;
			}

			try {
				new URL(payload.url()).toURI();
			} catch (Exception e) {
				player.sendSystemMessage(Component.translatable("disccord.song_url.invalid"));
				return;
			}

			if (payload.url().length() >= 400) {
				player.sendSystemMessage(Component.translatable("disccord.song_url.long"));
				return;
			}

			for (String url : DiscCordConfig.SERVER.whitelistedUrls.get()
			) {
				if (payload.url().startsWith(url)) {
					player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.BLOCKS, 1.0f, 1.0f, false);
					currentItem.set(ModDataComponents.MUSIC_URL.get(), payload.url());
					return;
				}
			}
			// Probably need to format console message differently (too many Link Sources lead to crappy/long message)
			player.sendSystemMessage(Component.translatable("disccord.song_url.websites").append(",").append(DiscCordConfig.SERVER.whitelistedWebsites.get().toString()));
		});

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(content -> {
			content.accept(ModRegistry.CUSTOM_RECORD.get());
		});
	}
}
