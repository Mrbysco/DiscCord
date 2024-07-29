package com.mrbysco.disccord.network.payload;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.config.DiscCordConfig;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.net.URL;
import java.util.function.Supplier;

public record SetRecordUrlMessage(String url) {

	public void encode(FriendlyByteBuf buf) {
		buf.writeUtf(url);
	}

	public static SetRecordUrlMessage decode(final FriendlyByteBuf packetBuffer) {
		return new SetRecordUrlMessage(packetBuffer.readUtf());
	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer()) {
				Player player = context.get().getSender();
				ItemStack currentItem = player.getItemInHand(player.getUsedItemHand());

				if (!currentItem.is(ModRegistry.CUSTOM_RECORD.get())) {
					return;
				}

				try {
					new URL(url()).toURI();
				} catch (Exception e) {
					player.sendSystemMessage(Component.translatable("disccord.song_url.invalid"));
					return;
				}

				if (url().length() >= 400) {
					player.sendSystemMessage(Component.translatable("disccord.song_url.long"));
					return;
				}

				for (String url : DiscCordConfig.SERVER.whitelistedUrls.get()
				) {
					if (url().startsWith(url)) {
						player.playNotifySound(SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.BLOCKS, 1.0f, 1.0f);
						CompoundTag tag = currentItem.getOrCreateTag();
						tag.putString(DiscCordMod.URL_NBT, url());
						currentItem.setTag(tag);
						return;
					}
				}
				// Probably need to format console message differently (too many Link Sources lead to crappy/long message)
				player.sendSystemMessage(Component.translatable("disccord.song_url.websites").append(",").append(DiscCordConfig.SERVER.whitelistedWebsites.get().toString()));
			}
		});
		ctx.setPacketHandled(true);
	}
}
