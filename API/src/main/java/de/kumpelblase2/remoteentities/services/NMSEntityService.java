package de.kumpelblase2.remoteentities.services;

import org.bukkit.entity.LivingEntity;

public interface NMSEntityService
{
	public boolean checkAffected(LivingEntity inEntity);
	public void resetAffection(LivingEntity inEntity);
}