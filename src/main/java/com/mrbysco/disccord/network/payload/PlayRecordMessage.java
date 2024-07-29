package com.mrbysco.disccord.network.payload;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PlayRecordMessage(BlockPos pos, String url) {

	public void encode(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeUtf(url);
	}

	public static PlayRecordMessage decode(final FriendlyByteBuf packetBuffer) {
		return new PlayRecordMessage(packetBuffer.readBlockPos(), packetBuffer.readUtf());
	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isClient()) {
				com.mrbysco.disccord.client.ClientHandler.playRecord(pos(), url());
			}
		});
		ctx.setPacketHandled(true);
	}
}
