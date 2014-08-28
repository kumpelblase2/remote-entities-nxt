package de.kumpelblase2.remoteentities.services;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import org.bukkit.entity.LivingEntity;

public interface RemoteEntityConversionService
{
	public RemoteEntity getRemoteEntityFromLiving(LivingEntity inLivingEntity);
}