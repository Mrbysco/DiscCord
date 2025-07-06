package com.mrbysco.disccord.network.payload;

import com.mrbysco.disccord.DiscCordMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record SetRecordUrlPayload(String url) implements CustomPacketPayload {

	public static final StreamCodec<RegistryFriendlyByteBuf, SetRecordUrlPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			payload -> payload.url,
			SetRecordUrlPayload::new
	);
	public static final Type<SetRecordUrlPayload> ID = new Type<>(DiscCordMod.modLoc("set_record_url"));

	@NotNull
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
