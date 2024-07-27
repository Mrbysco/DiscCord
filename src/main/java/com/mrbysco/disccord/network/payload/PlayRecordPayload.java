package com.mrbysco.disccord.network.payload;

import com.mrbysco.disccord.DiscCordMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record PlayRecordPayload(BlockPos pos, String url) implements CustomPacketPayload {

	public static final StreamCodec<FriendlyByteBuf, PlayRecordPayload> CODEC = CustomPacketPayload.codec(
			PlayRecordPayload::write,
			PlayRecordPayload::new);
	public static final Type<PlayRecordPayload> ID = new Type<>(DiscCordMod.modLoc("play_record"));


	public PlayRecordPayload(FriendlyByteBuf buf) {
		this(buf.readBlockPos(), buf.readUtf());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeUtf(url);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
