package com.mrbysco.disccord.network.payload;

import com.mrbysco.disccord.DiscCordMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;

public record OpenMusicDiscScreenPayload(ItemStack disc) implements CustomPacketPayload {

	public static final StreamCodec<RegistryFriendlyByteBuf, OpenMusicDiscScreenPayload> CODEC = CustomPacketPayload.codec(
			OpenMusicDiscScreenPayload::write,
			OpenMusicDiscScreenPayload::new);
	public static final Type<OpenMusicDiscScreenPayload> ID = new Type<>(DiscCordMod.modLoc("open_disc_screen"));


	public OpenMusicDiscScreenPayload(RegistryFriendlyByteBuf buf) {
		this(ItemStack.STREAM_CODEC.decode(buf));
	}

	public void write(RegistryFriendlyByteBuf buf) {
		ItemStack.STREAM_CODEC.encode(buf, disc);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
