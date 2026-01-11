package com.mrbysco.disccord.network.payload;

import com.mrbysco.disccord.DiscCordMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.Util;

import java.util.UUID;

public record PlayRecordPayload(BlockPos pos, String url, UUID uuid, int entityId) implements CustomPacketPayload {

	public static final StreamCodec<RegistryFriendlyByteBuf, PlayRecordPayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			payload -> payload.pos,
			ByteBufCodecs.STRING_UTF8,
			payload -> payload.url,
			UUIDUtil.STREAM_CODEC,
			payload -> payload.uuid,
			ByteBufCodecs.INT,
			payload -> payload.entityId,
			PlayRecordPayload::new
	);
	public static final Type<PlayRecordPayload> ID = new Type<>(DiscCordMod.modLoc("play_record"));

	public PlayRecordPayload(BlockPos pos, String url) {
		this(pos, url, Util.NIL_UUID, -1);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
