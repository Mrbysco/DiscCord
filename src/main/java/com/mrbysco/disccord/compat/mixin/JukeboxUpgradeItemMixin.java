package com.mrbysco.disccord.compat.mixin;

import com.mrbysco.disccord.network.payload.PlayRecordPayload;
import com.mrbysco.disccord.registry.ModDataComponents;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.init.ModCoreDataComponents;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.jukebox.JukeboxUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.jukebox.JukeboxUpgradeWrapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin(JukeboxUpgradeWrapper.class)
public abstract class JukeboxUpgradeItemMixin extends UpgradeWrapperBase<JukeboxUpgradeWrapper, JukeboxUpgradeItem> {

	protected JukeboxUpgradeItemMixin(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
		super(storageWrapper, upgrade, upgradeSaveHandler);
	}

	@Shadow
	protected abstract void setIsPlaying(boolean playing);

	@Shadow
	public abstract ItemStack getDisc();

	@Shadow protected abstract void setDiscSlotActive(int discSlotActive);

	@Shadow @Final private LinkedList<Integer> playlist;

	@Shadow @Final private LinkedList<Integer> history;

	@Shadow @Nullable private Entity entityPlaying;

	@Shadow @Nullable private BlockPos posPlaying;

	@Shadow @Nullable private Level levelPlaying;

	@Shadow public abstract Optional<Holder<JukeboxSong>> getJukeboxSongHolder(Level level);

	@Inject(at = @At("HEAD"), method = "stop(Lnet/minecraft/world/entity/LivingEntity;)V")
	public void disccord$stop(LivingEntity entity, CallbackInfo ci) {
		if (entity.level() instanceof ServerLevel serverLevel) {
			this.storageWrapper.getContentsUuid().ifPresent((storageUuid) -> {
				PacketDistributor.sendToPlayersNear(serverLevel, (ServerPlayer) null,
						entity.getX(), entity.getY(), entity.getZ(), 128.0,
						new PlayRecordPayload(entity.blockPosition(), "", storageUuid, entity.getId()));
				this.setIsPlaying(false);
			});
			this.setIsPlaying(false);
			this.upgrade.remove(ModCoreDataComponents.DISC_FINISH_TIME);
			this.setDiscSlotActive(-1);
			this.playlist.clear();
			this.history.clear();
		}
	}

	@Inject(at = @At("HEAD"), method = "playDisc()V", remap = false)
	public void disccord$playDisc(CallbackInfo ci) {
		if (getDisc().is(ModRegistry.CUSTOM_RECORD.get())) {
			Level level = this.entityPlaying != null ? this.entityPlaying.level() : this.levelPlaying;
			if (level instanceof ServerLevel serverLevel) {
				if (this.posPlaying != null || this.entityPlaying != null) {
					if (this.getDisc().isEmpty()) {
						return;
					}

					this.storageWrapper.getContentsUuid().ifPresent((storageUuid) -> this.getJukeboxSongHolder(level).ifPresent((song) -> {
						String musicUrl = getDisc().getOrDefault(ModDataComponents.MUSIC_URL.get(), "");
						if (this.entityPlaying != null) {
							PacketDistributor.sendToPlayersNear(serverLevel, (ServerPlayer) null,
									this.entityPlaying.getX(), this.entityPlaying.getY(), this.entityPlaying.getZ(), 128.0,
									new PlayRecordPayload(this.entityPlaying.blockPosition(), musicUrl, storageUuid, entityPlaying.getId()));
						} else {
							PacketDistributor.sendToPlayersNear(serverLevel, (ServerPlayer) null,
									this.posPlaying.getX(), this.posPlaying.getY(), this.posPlaying.getZ(), 128.0,
									new PlayRecordPayload(this.posPlaying, musicUrl, storageUuid, entityPlaying.getId()));
						}

						this.upgrade.set(ModCoreDataComponents.DISC_FINISH_TIME, level.getGameTime() + (long)((JukeboxSong)song.value()).lengthInTicks());
					}));
					this.setIsPlaying(true);
					return;
				}
			}
			ci.cancel();
		}
	}
}
