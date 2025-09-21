package com.mrbysco.disccord.client.audio;

import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.world.entity.Entity;

/**
 * Custom Sound Instance for playing custom sounds bound to entities
 */
public class EntityBoundFileSound extends FileSound implements TickableSoundInstance {
	private final Entity entity;
	private boolean stopped;

	public EntityBoundFileSound(String fileUrl, Entity entity) {
		super(fileUrl, entity.position());
		this.entity = entity;
	}

	@Override
	public boolean isStopped() {
		return this.stopped;
	}

	protected final void stop() {
		this.stopped = true;
	}

	@Override
	public boolean canPlaySound() {
		return !this.entity.isSilent();
	}

	@Override
	public void tick() {
		if (this.entity.isRemoved()) {
			this.stop();
		} else {
			this.x = (double)((float)this.entity.getX());
			this.y = (double)((float)this.entity.getY());
			this.z = (double)((float)this.entity.getZ());
		}
	}
}
