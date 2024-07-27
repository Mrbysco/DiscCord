package com.mrbysco.disccord.mixin.client;

import com.mrbysco.disccord.client.audio.StreamHelper;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(SoundBufferLibrary.class)
public class SoundBufferLibraryMixin {
	@Inject(at = @At("HEAD"), method = "getStream", cancellable = true)
	public void disccord$getStream(ResourceLocation resourceLocation, boolean isWrapper,
	                               CallbackInfoReturnable<CompletableFuture<AudioStream>> cir) {
		var completableAudioStream = StreamHelper.getStream(resourceLocation, isWrapper);
		if (completableAudioStream != null) {
			cir.setReturnValue(completableAudioStream);
			cir.cancel();
		}
	}
}