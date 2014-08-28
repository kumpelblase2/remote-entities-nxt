package de.kumpelblase2.remoteentities.entities;

import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.EntitySound;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import org.bukkit.entity.Horse;

public class RemoteHorse extends RemoteBaseEntity<Horse>
{
	public RemoteHorse(int inID, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Horse, inManager);
	}

	public RemoteHorse(int inID, RemoteHorseEntity inEntity, EntityManager inManager)
	{
		this(inID, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "EntityHorse";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.LAND, "mob.horse.land");
	}
}