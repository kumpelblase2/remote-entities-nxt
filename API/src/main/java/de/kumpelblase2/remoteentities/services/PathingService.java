package de.kumpelblase2.remoteentities.services;

import de.kumpelblase2.remoteentities.api.pathfinding.Path;
import org.bukkit.entity.LivingEntity;

public interface PathingService
{
	public void injectPath(LivingEntity inEntity, Path inPath, double inSpeed);
}