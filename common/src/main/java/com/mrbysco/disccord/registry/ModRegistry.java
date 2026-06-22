package com.mrbysco.disccord.registry;

import com.mrbysco.disccord.Reference;
import com.mrbysco.disccord.item.DiscCordItem;
import dev.chococraft.registration.RegistrationProvider;
import dev.chococraft.registration.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ModRegistry {
	public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(BuiltInRegistries.ITEM, Reference.MOD_ID);
	public static final RegistrationProvider<SoundEvent> SOUND_EVENTS = RegistrationProvider.get(BuiltInRegistries.SOUND_EVENT, Reference.MOD_ID);

	public static final RegistryObject<Item, DiscCordItem> CUSTOM_RECORD = ITEMS.register("custom_record", () -> new DiscCordItem(
			itemBuilder("custom_record").stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(ModJukeboxSongs.PLACEHOLDER_SONG)));
	public static final RegistryObject<SoundEvent, SoundEvent> PLACEHOLDER_SOUND = SOUND_EVENTS.register("placeholder_sound", () ->
			SoundEvent.createVariableRangeEvent(Reference.modLoc("placeholder_sound")));


	private static Item.Properties itemBuilder(String name) {
		return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Reference.modLoc(name)));
	}

	public static void load() {
		// Load class
	}
}
