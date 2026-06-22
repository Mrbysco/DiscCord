package com.mrbysco.disccord;

import com.mrbysco.disccord.config.DiscCordConfig;
import com.mrbysco.disccord.network.PacketHandler;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(Reference.MOD_ID)
public class DiscCordMod {

	public DiscCordMod(IEventBus eventBus, ModContainer container, Dist dist) {
		container.registerConfig(ModConfig.Type.SERVER, DiscCordConfig.serverSpec);

		Reference.initRegistries();

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
}