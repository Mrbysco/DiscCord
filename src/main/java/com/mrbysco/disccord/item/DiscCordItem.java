package com.mrbysco.disccord.item;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.network.PacketHandler;
import com.mrbysco.disccord.network.payload.OpenMusicDiscScreenMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class DiscCordItem extends RecordItem {
	public DiscCordItem(Properties properties, int comparatorOutput, Supplier<SoundEvent> soundEvent, int lengthInSeconds) {
		super(comparatorOutput, soundEvent, properties.tab(CreativeModeTab.TAB_TOOLS), lengthInSeconds);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
		ItemStack heldStack = player.getItemInHand(interactionHand);
		if (player instanceof ServerPlayer serverPlayer) {
			PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new OpenMusicDiscScreenMessage(heldStack));
		}
		return InteractionResultHolder.success(heldStack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
		//Get the current URL from the disc
		CompoundTag tag = stack.getTag();
		if (tag == null) {
			tag = new CompoundTag();
		}

		if (tag.contains(DiscCordMod.URL_NBT)) {
			String currentUrl = tag.getString(DiscCordMod.URL_NBT);
			if (!currentUrl.isEmpty() && tooltipFlag.isAdvanced()) {
				Component urlComponent = Component.literal(currentUrl).withStyle(ChatFormatting.BLUE);
				components.add(Component.translatable("item.disccord.custom_record.tooltip", urlComponent).withStyle(ChatFormatting.GRAY));
			}
		}
		super.appendHoverText(stack, level, components, tooltipFlag);
	}

	@Override
	public int getLengthInTicks() {
		return 0;
	}
}
