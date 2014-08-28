package de.kumpelblase2.remoteentities.services;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.pathfinding.BlockNode;
import de.kumpelblase2.remoteentities.api.pathfinding.Path;
import org.bukkit.Chunk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public interface WorldService
{
	public void injectPath(LivingEntity inEntity, Path inPath, double inSpeed);
	public Vector addEntityWidth(RemoteEntity inEntity, BlockNode inNode);
	public void updateEntityTracking(RemoteEntity inEntity, Chunk inChunk);
}