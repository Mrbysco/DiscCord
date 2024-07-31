package com.mrbysco.disccord;

import com.mojang.logging.LogUtils;
import com.mrbysco.disccord.config.DiscCordConfig;
import com.mrbysco.disccord.network.PacketHandler;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(DiscCordMod.MOD_ID)
public class DiscCordMod {
	public static final String MOD_ID = "disccord";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final String URL_NBT = "disccord:url";

	public DiscCordMod() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, DiscCordConfig.serverSpec);
		eventBus.register(DiscCordConfig.class);

		ModRegistry.ITEMS.register(eventBus);
		ModRegistry.SOUND_EVENTS.register(eventBus);

		eventBus.addListener(this::setup);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, DiscCordConfig.clientSpec);
		});
	}

	private void setup(final FMLCommonSetupEvent event) {
		PacketHandler.init();
	}

	public static ResourceLocation modLoc(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
