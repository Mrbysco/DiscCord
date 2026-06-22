package com.mrbysco.disccord.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.nio.file.Path;

public interface IPlatformHelper {

	Path getConfigFolder();

	void playRecord(ServerPlayer serverPlayer, BlockPos pos, String url);

	void sendURLPacket(String url);

	void openMusicDiscScreen(ServerPlayer serverPlayer, ItemStack stack);
}
