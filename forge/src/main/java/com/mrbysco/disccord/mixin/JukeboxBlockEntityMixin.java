package com.mrbysco.disccord.mixin;

import com.mrbysco.disccord.platform.Services;
import com.mrbysco.disccord.registry.ModDataComponents;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JukeboxBlockEntity.class)
public class JukeboxBlockEntityMixin {
	@Shadow
	private ItemStack item;

	@Inject(at = @At("TAIL"), method = "popOutTheItem")
	public void disccord$popOutTheItem(CallbackInfo ci) {
		JukeboxBlockEntity jukebox = (JukeboxBlockEntity) (Object) this;

		if (jukebox.getLevel() instanceof ServerLevel serverLevel) {
			serverLevel.players().forEach(player -> {
				Services.PLATFORM.playRecord(player, jukebox.getBlockPos(), "");
			});
		}
	}

	@Inject(method = "itemChanged()V", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/JukeboxSongPlayer;play(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/Holder;)V",
			shift = At.Shift.AFTER,
			ordinal = 0))
	public void disccord$setTheItem(CallbackInfo ci) {
		JukeboxBlockEntity jukebox = (JukeboxBlockEntity) (Object) this;
		Level level = jukebox.getLevel();
		if (this.item.is(ModRegistry.CUSTOM_RECORD.get()) && level instanceof ServerLevel) {
			String musicUrl = this.item.getOrDefault(ModDataComponents.MUSIC_URL.get(), "");

			if (musicUrl != null && !musicUrl.isEmpty()) {
				level.players().forEach(player -> {
					Services.PLATFORM.playRecord(((ServerPlayer) player), jukebox.getBlockPos(), musicUrl);
				});
			}
		}
	}
}
