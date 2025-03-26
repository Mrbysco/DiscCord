package com.mrbysco.disccord.network;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.network.payload.OpenMusicDiscScreenMessage;
import com.mrbysco.disccord.network.payload.PlayRecordMessage;
import com.mrbysco.disccord.network.payload.SetRecordUrlMessage;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(DiscCordMod.MOD_ID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);
	private static int id = 0;

	public static void init() {
		CHANNEL.registerMessage(id++, OpenMusicDiscScreenMessage.class, OpenMusicDiscScreenMessage::encode, OpenMusicDiscScreenMessage::decode, OpenMusicDiscScreenMessage::handle);
		CHANNEL.registerMessage(id++, PlayRecordMessage.class, PlayRecordMessage::encode, PlayRecordMessage::decode, PlayRecordMessage::handle);
		CHANNEL.registerMessage(id++, SetRecordUrlMessage.class, SetRecordUrlMessage::encode, SetRecordUrlMessage::decode, SetRecordUrlMessage::handle);
	}

	public static <M> void sendToAllNear(ResourceKey<Level> dimension, Vec3 position, int range, M message) {
		CHANNEL.send(PacketDistributor.NEAR.with(() ->
				new PacketDistributor.TargetPoint(position.x(), position.y(), position.z(), range, dimension)), message);
	}
}
