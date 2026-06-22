package com.mrbysco.disccord;

import com.mojang.logging.LogUtils;
import com.mrbysco.disccord.registry.ModDataComponents;
import com.mrbysco.disccord.registry.ModRegistry;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;

public class Reference {
	public static final String MOD_ID = "disccord";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static Identifier modLoc(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public static void initRegistries() {
		ModDataComponents.load();
		ModRegistry.load();
	}
}