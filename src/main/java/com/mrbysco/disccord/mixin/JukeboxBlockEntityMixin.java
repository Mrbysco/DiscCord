package com.mrbysco.disccord.mixin;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.network.PacketHandler;
import com.mrbysco.disccord.network.payload.PlayRecordMessage;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JukeboxBlockEntity.class)
public class JukeboxBlockEntityMixin {
	@Inject(at = @At("TAIL"), method = "clearContent")
	public void disccord$clearContent(CallbackInfo ci) {
		JukeboxBlockEntity jukebox = (JukeboxBlockEntity) (Object) this;

		if (!jukebox.getLevel().isClientSide) {
			jukebox.getLevel().players().forEach(player -> {
				PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new PlayRecordMessage(jukebox.getBlockPos(), ""));
			});
		}
	}

	@Inject(method = "playRecord()V", at = @At("HEAD"))
	public void disccord$playRecord(CallbackInfo ci) {
		JukeboxBlockEntity jukebox = (JukeboxBlockEntity) (Object) this;
		Level level = jukebox.getLevel();
		ItemStack stack = jukebox.getRecord();
		if (stack.is(ModRegistry.CUSTOM_RECORD.get()) && level instanceof ServerLevel) {
			CompoundTag tag = stack.getTag();
			if (tag == null) {
				tag = new CompoundTag();
			}

			String musicUrl = tag.getString(DiscCordMod.URL_NBT);
			if (!musicUrl.isEmpty()) {
				level.players().forEach(player -> {
					PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
							new PlayRecordMessage(jukebox.getBlockPos(), musicUrl));
				});
			}
		}
	}
}
