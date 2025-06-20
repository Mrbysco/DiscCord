package com.mrbysco.disccord.item;

import com.mrbysco.disccord.network.payload.OpenMusicDiscScreenPayload;
import com.mrbysco.disccord.registry.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class DiscCordItem extends Item {
	public DiscCordItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stackInHand = player.getItemInHand(hand);
		if (player instanceof ServerPlayer serverPlayer) {
			serverPlayer.connection.send(new OpenMusicDiscScreenPayload(stackInHand));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
		String currentURL = stack.getOrDefault(ModDataComponents.MUSIC_URL, "");
		if (!currentURL.isEmpty() && flag.isAdvanced()) {
			Component urlComponent = Component.literal(currentURL).withStyle(ChatFormatting.BLUE);
			tooltipAdder.accept(Component.translatable("item.disccord.custom_record.tooltip", urlComponent).withStyle(ChatFormatting.GRAY));
		}
	}
}
