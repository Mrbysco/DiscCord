package com.mrbysco.disccord.platform;

import com.mrbysco.disccord.network.payload.OpenMusicDiscScreenPayload;
import com.mrbysco.disccord.network.payload.PlayRecordPayload;
import com.mrbysco.disccord.network.payload.SetRecordUrlPayload;
import com.mrbysco.disccord.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.nio.file.Path;

public class FabricPlatformHelper implements IPlatformHelper {

	@Override
	public Path getConfigFolder() {
		return FabricLoader.getInstance().getConfigDir();
	}

	@Override
	public void playRecord(ServerPlayer serverPlayer, BlockPos pos, String url) {
		ServerPlayNetworking.send(serverPlayer, new PlayRecordPayload(pos, url));
	}

	@Override
	public void sendURLPacket(String url) {
		ClientPlayNetworking.send(new SetRecordUrlPayload(url));
	}

	@Override
	public void openMusicDiscScreen(ServerPlayer serverPlayer, ItemStack stack) {
		ServerPlayNetworking.send(serverPlayer, new OpenMusicDiscScreenPayload(stack));
	}
}
