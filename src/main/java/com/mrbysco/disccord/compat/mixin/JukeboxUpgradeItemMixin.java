package com.mrbysco.disccord.compat.mixin;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.network.PacketHandler;
import com.mrbysco.disccord.network.payload.PlayRecordMessage;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.jukebox.JukeboxUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.jukebox.JukeboxUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.function.Consumer;

@Mixin(JukeboxUpgradeWrapper.class)
public abstract class JukeboxUpgradeItemMixin extends UpgradeWrapperBase<JukeboxUpgradeWrapper, JukeboxUpgradeItem> {

	@Shadow protected abstract void setIsPlaying(boolean playing);

	@Shadow public abstract ItemStack getDisc();

	@Shadow @Nullable private Level levelPlaying;

	@Shadow @Nullable private Entity entityPlaying;

	@Shadow @Nullable private BlockPos posPlaying;

	@Shadow protected abstract void setDiscSlotActive(int discSlotActive);

	@Shadow @Final private LinkedList<Integer> playlist;

	@Shadow @Final private LinkedList<Integer> history;

	protected JukeboxUpgradeItemMixin(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
		super(storageWrapper, upgrade, upgradeSaveHandler);
	}

	@Inject(at = @At("HEAD"), method = "stop(Lnet/minecraft/world/entity/LivingEntity;)V", remap = false)
	public void disccord$stop(LivingEntity entity, CallbackInfo ci) {
		if (entity.level() instanceof ServerLevel serverLevel) {
			this.storageWrapper.getContentsUuid().ifPresent((storageUuid) -> {
				PacketHandler.sendToAllNear(serverLevel.dimension(),
						entity.position(), 128,
						new PlayRecordMessage(entity.blockPosition(), "", storageUuid));
				this.setIsPlaying(false);
				NBTHelper.removeTag(this.upgrade, "discFinishTime");
				NBTHelper.removeTag(this.upgrade, "discLength");
				this.setDiscSlotActive(-1);
				this.playlist.clear();
				this.history.clear();
			});
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

					this.storageWrapper.getContentsUuid().ifPresent((storageUuid) -> {
						String musicUrl = disccord$getUrl(getDisc());
						if (this.entityPlaying != null) {
							PacketHandler.sendToAllNear(serverLevel.dimension(), this.entityPlaying.position(), 128,
									new PlayRecordMessage(this.entityPlaying.blockPosition(), musicUrl, storageUuid));
						} else {
							PacketHandler.sendToAllNear(serverLevel.dimension(), this.posPlaying.getCenter(), 128,
									new PlayRecordMessage(this.posPlaying, musicUrl, storageUuid));
						}

						Item item = this.getDisc().getItem();
						if (item instanceof RecordItem recordItem) {
							int lengthInTicks = recordItem.getLengthInTicks();
							NBTHelper.setLong(this.upgrade, "discFinishTime", level.getGameTime() + (long)lengthInTicks);
							NBTHelper.setLong(this.upgrade, "discLength", (long)lengthInTicks);
						}

					});
					this.setIsPlaying(true);
					return;
				}
			}
			ci.cancel();
		}
	}

	private String disccord$getUrl(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		if (tag == null) {
			tag = new CompoundTag();
		}
		return tag.getString(DiscCordMod.URL_NBT); //Falls back to empty string if not found
	}
}
