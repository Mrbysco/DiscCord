package com.mrbysco.disccord.network.payload;

import com.mrbysco.disccord.DiscCordMod;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.UUID;

public record PlayRecordPayload(BlockPos pos, String url, UUID uuid, int entityId) implements CustomPacketPayload {

	public static final StreamCodec<FriendlyByteBuf, PlayRecordPayload> CODEC = CustomPacketPayload.codec(
			PlayRecordPayload::write,
			PlayRecordPayload::new);
	public static final Type<PlayRecordPayload> ID = new Type<>(DiscCordMod.modLoc("play_record"));

	public PlayRecordPayload(BlockPos pos, String url) {
		this(pos, url, Util.NIL_UUID, -1);
	}

	public PlayRecordPayload(FriendlyByteBuf buf) {
		this(buf.readBlockPos(), buf.readUtf(), buf.readUUID(), buf.readInt());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeUtf(url);
		buf.writeUUID(uuid);
		buf.writeInt(entityId);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
