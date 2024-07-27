package com.mrbysco.disccord.registry;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.item.DiscCordItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRegistry {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DiscCordMod.MOD_ID);
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, DiscCordMod.MOD_ID);

	public static final DeferredItem<DiscCordItem> CUSTOM_RECORD = ITEMS.register("custom_record", () -> new DiscCordItem(new Item.Properties()
			.stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(ModJukeboxSongs.PLACEHOLDER_SONG)));
	public static final DeferredHolder<SoundEvent, SoundEvent> PLACEHOLDER_SOUND = SOUND_EVENTS.register("placeholder_sound", () ->
			SoundEvent.createVariableRangeEvent(DiscCordMod.modLoc("placeholder_sound")));
}
