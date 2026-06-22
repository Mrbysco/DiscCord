package com.mrbysco.disccord.platform;

import com.mrbysco.disccord.network.payload.OpenMusicDiscScreenPayload;
import com.mrbysco.disccord.network.payload.PlayRecordPayload;
import com.mrbysco.disccord.network.payload.SetRecordUrlPayload;
import com.mrbysco.disccord.platform.services.IPlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;

import java.nio.file.Path;

public class NeoForgePlatformHelper implements IPlatformHelper {

	@Override
	public Path getConfigFolder() {
		return FMLPaths.CONFIGDIR.get();
	}

	@Override
	public void playRecord(ServerPlayer serverPlayer, BlockPos pos, String url) {
		PacketDistributor.sendToPlayer(serverPlayer, new PlayRecordPayload(pos, url));
	}

	@Override
	public void sendURLPacket(String url) {
		ClientPacketDistributor.sendToServer(new SetRecordUrlPayload(url));
	}

	@Override
	public void openMusicDiscScreen(ServerPlayer serverPlayer, ItemStack stack) {
		PacketDistributor.sendToPlayer(serverPlayer, new OpenMusicDiscScreenPayload(stack));
	}
}
