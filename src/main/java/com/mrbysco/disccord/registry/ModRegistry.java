package com.mrbysco.disccord.registry;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.item.DiscCordItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRegistry {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DiscCordMod.MOD_ID);
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DiscCordMod.MOD_ID);

	public static final RegistryObject<DiscCordItem> CUSTOM_RECORD = ITEMS.register("custom_record", () -> new DiscCordItem(
			new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 0, ModRegistry.PLACEHOLDER_SOUND, 1)
	);

	public static final RegistryObject<SoundEvent> PLACEHOLDER_SOUND = SOUND_EVENTS.register("placeholder_sound", () ->
			SoundEvent.createVariableRangeEvent(DiscCordMod.modLoc("placeholder_sound")));
}
