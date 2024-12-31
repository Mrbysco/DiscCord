package com.mrbysco.disccord.compat.mixin;

import com.mrbysco.disccord.network.payload.PlayRecordPayload;
import com.mrbysco.disccord.registry.ModDataComponents;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.jukebox.JukeboxUpgradeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mixin(JukeboxUpgradeItem.Wrapper.class)
public abstract class JukeboxUpgradeItemMixin extends UpgradeWrapperBase<JukeboxUpgradeItem.Wrapper, JukeboxUpgradeItem> {

	protected JukeboxUpgradeItemMixin(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
		super(storageWrapper, upgrade, upgradeSaveHandler);
	}

	@Shadow
	protected abstract void setIsPlaying(boolean playing);

	@Shadow
	public abstract ItemStack getDisc();

	@Shadow
	protected abstract void play(Level level, BiConsumer<ServerLevel, UUID> play);

	@Inject(at = @At("HEAD"), method = "stop(Lnet/minecraft/world/entity/LivingEntity;)V")
	public void disccord$stop(LivingEntity entity, CallbackInfo ci) {
		if (entity.level() instanceof ServerLevel serverLevel) {
			this.storageWrapper.getContentsUuid().ifPresent((storageUuid) -> {
				PacketDistributor.sendToPlayersNear(serverLevel, (ServerPlayer) null,
						entity.getX(), entity.getY(), entity.getZ(), 128.0,
						new PlayRecordPayload(entity.blockPosition(), "", storageUuid));
				this.setIsPlaying(false);
			});
		}
	}

	@Inject(at = @At("HEAD"), method = "play(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V", cancellable = true)
	public void disccord$play(Level level, BlockPos pos, CallbackInfo ci) {
		if (getDisc().is(ModRegistry.CUSTOM_RECORD.asItem())) {
			this.play(level, (serverLevel, storageUuid) -> {
				String musicUrl = getDisc().getOrDefault(ModDataComponents.MUSIC_URL.get(), "");
				PacketDistributor.sendToPlayersNear(serverLevel, (ServerPlayer) null, pos.getX(), pos.getY(), pos.getZ(), 128.0,
						new PlayRecordPayload(pos, musicUrl, storageUuid));
			});
			ci.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "play(Lnet/minecraft/world/entity/Entity;)V", cancellable = true)
	public void disccord$play2(Entity entity, CallbackInfo ci) {
		if (getDisc().is(ModRegistry.CUSTOM_RECORD.asItem())) {
			this.play(entity.level(), (serverLevel, storageUuid) -> {
				String musicUrl = getDisc().getOrDefault(ModDataComponents.MUSIC_URL.get(), "");
				PacketDistributor.sendToPlayersNear(serverLevel, (ServerPlayer) null, entity.getX(), entity.getY(), entity.getZ(), 128.0,
						new PlayRecordPayload(entity.blockPosition(), musicUrl, storageUuid));
			});
			ci.cancel();
		}
	}
}
