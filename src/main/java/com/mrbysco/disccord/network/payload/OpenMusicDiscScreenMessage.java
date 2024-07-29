package com.mrbysco.disccord.network.payload;

import com.mrbysco.disccord.DiscCordMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record OpenMusicDiscScreenMessage(ItemStack disc) {

	public void encode(FriendlyByteBuf buf) {
		buf.writeItem(disc);
	}

	public static OpenMusicDiscScreenMessage decode(final FriendlyByteBuf packetBuffer) {
		return new OpenMusicDiscScreenMessage(packetBuffer.readItem());
	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isClient()) {
				//Get the current URL from the disc
				CompoundTag tag = disc.getTag();
				if (tag == null) {
					tag = new CompoundTag();
				}

				String currentUrl = tag.getString(DiscCordMod.URL_NBT);
				com.mrbysco.disccord.client.screen.MusicDiscScreen.openScreen(Component.literal("DiscCord"), currentUrl);
			}
		});
		ctx.setPacketHandled(true);
	}
}
