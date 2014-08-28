package de.kumpelblase2.remoteentities;

import java.util.Collection;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.persistence.IEntitySerializer;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

public interface EntityManager
{
	public Plugin getPlugin();
	public RemoteEntity createEntity(RemoteEntityType inType, Location inLocation);
	public RemoteEntity createEntity(RemoteEntityType inType, Location inLocation, boolean inSetupGoals);
	public RemoteEntity createNamedEntity(RemoteEntityType inType, Location inLocation, String inName);
	public RemoteEntity createNamedEntity(RemoteEntityType inType, Location inLocation, String inName, boolean inSetupGoals);
	public CreateEntityContext prepareEntity(RemoteEntityType inType);
	public void removeEntity(int inID);
	public void removeEntity(int inID, boolean inDespawn);
	public boolean isRemoteEntity(LivingEntity inEntity);
	public RemoteEntity getRemoteEntityFromEntity(LivingEntity inEntity);
	public RemoteEntity getRemoteEntityByID(int inID);
	public Collection<RemoteEntity> getRemoteEntitiesByName(String inName);
	public void addRemoteEntity(int inID, RemoteEntity inEntity);
	public RemoteEntity createRemoteEntityFromExisting(LivingEntity inEntity);
	public RemoteEntity createRemoteEntityFromExisting(LivingEntity inEntity, boolean inDeleteOld);
	public void despawnAll();
	public void despawnAll(DespawnReason inReason);
	public void despawnAll(DespawnReason inReason, boolean inSave);
	public Collection<RemoteEntity> getAllEntities();
	public int getEntityCount();
	public boolean shouldRemoveDespawnedEntities();
	public void setRemovingDespawned(boolean inState);
	public void setEntitySerializer(IEntitySerializer inSerializer);
	public IEntitySerializer getSerializer();
	public int saveEntities();
	public int loadEntities();
	public void setSaveOnDisable(boolean inSave);
	public boolean shouldSaveOnDisable();
	public int getNextFreeID();
	public int getNextFreeID(int inStart);
	public Collection<RemoteEntity> getEntitiesByType(RemoteEntityType inType);
	public Collection<RemoteEntity> getEntitiesByType(RemoteEntityType inType, boolean inSpawnedOnly);
}