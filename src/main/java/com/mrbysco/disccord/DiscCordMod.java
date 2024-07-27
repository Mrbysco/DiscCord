package com.mrbysco.disccord;

import com.mojang.logging.LogUtils;
import com.mrbysco.disccord.config.DiscCordConfig;
import com.mrbysco.disccord.network.PacketHandler;
import com.mrbysco.disccord.registry.ModDataComponents;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

@Mod(DiscCordMod.MOD_ID)
public class DiscCordMod {
	public static final String MOD_ID = "disccord";
	public static final Logger LOGGER = LogUtils.getLogger();

	public DiscCordMod(IEventBus eventBus, Dist dist, ModContainer container) {
		container.registerConfig(ModConfig.Type.SERVER, DiscCordConfig.serverSpec);
		eventBus.register(DiscCordConfig.class);

		ModDataComponents.DATA_COMPONENT_TYPES.register(eventBus);
		ModRegistry.ITEMS.register(eventBus);
		ModRegistry.SOUND_EVENTS.register(eventBus);

		eventBus.addListener(this::addTabContents);
		eventBus.addListener(PacketHandler::setupPackets);

		if (dist.isClient()) {
			container.registerConfig(ModConfig.Type.CLIENT, DiscCordConfig.clientSpec);
			container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		}
	}

	private void addTabContents(final BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			event.accept(ModRegistry.CUSTOM_RECORD.get());
		}
	}

	public static ResourceLocation modLoc(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
