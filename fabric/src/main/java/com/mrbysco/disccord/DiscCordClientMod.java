package com.mrbysco.disccord;

import com.mrbysco.disccord.config.DiscCordConfig;
import com.mrbysco.disccord.network.payload.OpenMusicDiscScreenPayload;
import com.mrbysco.disccord.network.payload.PlayRecordPayload;
import com.mrbysco.disccord.registry.ModDataComponents;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.config.ModConfig;

import java.util.UUID;

public class DiscCordClientMod implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ConfigRegistry.INSTANCE.register(Reference.MOD_ID, ModConfig.Type.CLIENT, DiscCordConfig.clientSpec);

		ClientPlayNetworking.registerGlobalReceiver(OpenMusicDiscScreenPayload.ID, (payload, context) -> {
			ItemStack disc = payload.disc();

			//Get the current URL from the disc
			String currentUrl = disc.getOrDefault(ModDataComponents.MUSIC_URL.get(), "URL");
			com.mrbysco.disccord.client.screen.MusicDiscScreen.openScreen(Component.literal("DiscCord"), currentUrl);
		});

		ClientPlayNetworking.registerGlobalReceiver(PlayRecordPayload.ID, (payload, context) -> {
			Vec3 centerPos = Vec3.atCenterOf(payload.pos());
			String fileUrl = payload.url();
			UUID uuid = payload.uuid();
			int entityId = payload.entityId();
			com.mrbysco.disccord.client.ClientHandler.playRecord(centerPos, fileUrl, uuid, entityId);
		});
	}
}
