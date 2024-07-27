package com.mrbysco.disccord.network;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.network.handler.ClientPayloadHandler;
import com.mrbysco.disccord.network.handler.ServerPayloadHandler;
import com.mrbysco.disccord.network.payload.OpenMusicDiscScreenPayload;
import com.mrbysco.disccord.network.payload.PlayRecordPayload;
import com.mrbysco.disccord.network.payload.SetRecordUrlPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketHandler {

	public static void setupPackets(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(DiscCordMod.MOD_ID);

		registrar.playToClient(OpenMusicDiscScreenPayload.ID, OpenMusicDiscScreenPayload.CODEC, ClientPayloadHandler.getInstance()::handleDiscScreen);
		registrar.playToClient(PlayRecordPayload.ID, PlayRecordPayload.CODEC, ClientPayloadHandler.getInstance()::handleRecordPlay);
		registrar.playToServer(SetRecordUrlPayload.ID, SetRecordUrlPayload.CODEC, ServerPayloadHandler.getInstance()::handleRecordUrl);
	}
}
