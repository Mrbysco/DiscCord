package com.mrbysco.disccord.registry;

import com.mojang.serialization.Codec;
import com.mrbysco.disccord.Reference;
import com.mrbysco.disccord.registration.RegistrationProvider;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;

import java.util.function.Supplier;

public class ModDataComponents {
	public static final RegistrationProvider<DataComponentType<?>> DATA_COMPONENT_TYPES = RegistrationProvider.get(Registries.DATA_COMPONENT_TYPE, Reference.MOD_ID);

	public static final Supplier<DataComponentType<String>> MUSIC_URL = DATA_COMPONENT_TYPES.register("music_url", () ->
			DataComponentType.<String>builder()
					.persistent(Codec.STRING)
					.networkSynchronized(ByteBufCodecs.STRING_UTF8)
					.build());


	public static void load() {
		// Load class
	}
}
