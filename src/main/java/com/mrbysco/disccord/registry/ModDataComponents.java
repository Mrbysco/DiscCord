package com.mrbysco.disccord.registry;

import com.mojang.serialization.Codec;
import com.mrbysco.disccord.DiscCordMod;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponents {
	public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, DiscCordMod.MOD_ID);

	public static final Supplier<DataComponentType<String>> MUSIC_URL = DATA_COMPONENT_TYPES.register("music_url", () ->
			DataComponentType.<String>builder()
					.persistent(Codec.STRING)
					.networkSynchronized(ByteBufCodecs.STRING_UTF8)
					.build());

}
