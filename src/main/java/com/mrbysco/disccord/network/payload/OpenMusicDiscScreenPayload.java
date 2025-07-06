package com.mrbysco.disccord.network.payload;

import com.mrbysco.disccord.DiscCordMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record OpenMusicDiscScreenPayload(ItemStack disc) implements CustomPacketPayload {

	public static final StreamCodec<RegistryFriendlyByteBuf, OpenMusicDiscScreenPayload> CODEC = StreamCodec.composite(
			ItemStack.STREAM_CODEC,
			payload -> payload.disc,
			OpenMusicDiscScreenPayload::new
	);
	public static final Type<OpenMusicDiscScreenPayload> ID = new Type<>(DiscCordMod.modLoc("open_disc_screen"));

	@NotNull
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
