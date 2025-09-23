package com.mrbysco.disccord.network.payload;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public record PlayRecordMessage(BlockPos pos, String url, UUID uuid, int entityId) {

	public PlayRecordMessage(BlockPos pos, String url) {
		this(pos, url, Util.NIL_UUID, -1);
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeUtf(url);
		buf.writeUUID(uuid);
		buf.writeInt(entityId);
	}

	public static PlayRecordMessage decode(final FriendlyByteBuf packetBuffer) {
		return new PlayRecordMessage(packetBuffer.readBlockPos(), packetBuffer.readUtf(), packetBuffer.readUUID(), packetBuffer.readInt());
	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isClient()) {
				com.mrbysco.disccord.client.ClientHandler.playRecord(pos().getCenter(), url(), uuid(), entityId());
			}
		});
		ctx.setPacketHandled(true);
	}
}
