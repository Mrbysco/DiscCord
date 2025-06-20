//package com.mrbysco.disccord.compat;
//
//import net.neoforged.fml.ModList;
//import net.neoforged.fml.loading.LoadingModList;
//import net.neoforged.fml.loading.moddiscovery.ModInfo;
//import org.objectweb.asm.tree.ClassNode;
//import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
//import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
//
//import java.util.List;
//import java.util.Set;
//
//public class SophisticatedCompatPlugin implements IMixinConfigPlugin {
//	@Override
//	public void onLoad(String mixinPackage) {
//		if (isModLoaded("sophisticatedcore")) {
//			System.out.println("[DiscCord] SophisticatedCore is loaded, applying compat mixins");
//		}
//	}
//
//	@Override
//	public String getRefMapperConfig() {
//		return null;
//	}
//
//	@Override
//	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
//		List<String> classList = List.of(
//				"com.mrbysco.disccord.compat.mixin.JukeboxUpgradeItemMixin"
//		);
//		if (classList.contains(mixinClassName)) {
//			return isModLoaded("sophisticatedcore");
//		}
//		return true;
//	}
//
//	@Override
//	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
//		// noop
//	}
//
//	@Override
//	public List<String> getMixins() {
//		return null;
//	}
//
//	@Override
//	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
//		// noop
//	}
//
//	@Override
//	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
//		// noop
//	}
//
//	private static boolean isModLoaded(String modId) {
//		if (ModList.get() == null) {
//			return LoadingModList.get().getMods().stream().map(ModInfo::getModId).anyMatch(modId::equals);
//		}
//		return ModList.get().isLoaded(modId);
//	}
//}
