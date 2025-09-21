package com.mrbysco.disccord.client.audio;

import com.mrbysco.disccord.DiscCordMod;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * Custom Sound Instance for playing custom sounds
 */
public class FileSound implements SoundInstance {
	private String fileUrl;
	protected double x;
	protected double y;
	protected double z;

	public FileSound(String fileUrl, Vec3 position) {
		this.fileUrl = fileUrl;
		this.x = position.x;
		this.y = position.y;
		this.z = position.z;
	}

	@Override
	public ResourceLocation getLocation() {
		return DiscCordMod.modLoc("customsound/" + fileUrl
				.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9/._-]", "_"));
	}

	@Nullable
	@Override
	public WeighedSoundEvents resolve(SoundManager soundManager) {
		return new WeighedSoundEvents(getLocation(), null);
	}

	@Override
	public Sound getSound() {
		return new Sound(getLocation().toString(), ConstantFloat.of((float) getVolume()), ConstantFloat.of((float) getPitch()), 1, Sound.Type.SOUND_EVENT, true, false, 64);
	}

	@Override
	public SoundSource getSource() {
		return SoundSource.RECORDS;
	}

	@Override
	public boolean isLooping() {
		return false;
	}

	@Override
	public boolean isRelative() {
		return false;
	}

	@Override
	public int getDelay() {
		return 0;
	}

	@Override
	public float getVolume() {
		return 1;
	}

	@Override
	public float getPitch() {
		return 1;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public double getZ() {
		return z;
	}

	@Override
	public Attenuation getAttenuation() {
		return Attenuation.LINEAR;
	}
}
