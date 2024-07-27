package com.mrbysco.disccord.network.payload;

import com.mrbysco.disccord.DiscCordMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record SetRecordUrlPayload(String url) implements CustomPacketPayload {

	public static final StreamCodec<FriendlyByteBuf, SetRecordUrlPayload> CODEC = CustomPacketPayload.codec(
			SetRecordUrlPayload::write,
			SetRecordUrlPayload::new);
	public static final Type<SetRecordUrlPayload> ID = new Type<>(DiscCordMod.modLoc("set_record_url"));


	public SetRecordUrlPayload(FriendlyByteBuf buf) {
		this(buf.readUtf());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeUtf(url);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
