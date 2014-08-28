package de.kumpelblase2.remoteentities;

import java.util.*;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.persistence.EntityData;
import de.kumpelblase2.remoteentities.persistence.IEntitySerializer;
import de.kumpelblase2.remoteentities.services.RemoteEntityConversionService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public abstract class BaseEntityManager implements EntityManager
{
	private final Plugin m_plugin;
	private final Map<Integer, RemoteEntity> m_entities;
	private boolean m_saveOnDisable = false;
	protected boolean m_removeDespawned = false;
	protected IEntitySerializer m_serializer;
	protected final RemoteEntityConversionService m_conversionService;

	protected BaseEntityManager(Plugin inPlugin)
	{
		this.m_plugin = inPlugin;
		this.m_entities = new HashMap<Integer, RemoteEntity>();
		this.m_conversionService = Bukkit.getServicesManager().getRegistration(RemoteEntityConversionService.class).getProvider();
	}

	public Plugin getPlugin()
	{
		return this.m_plugin;
	}

	protected abstract void setup();

	@Override
	public boolean isRemoteEntity(LivingEntity inEntity)
	{
		if(inEntity.hasMetadata("remoteentity"))
		{
			for(MetadataValue value : inEntity.getMetadata("remoteentity"))
			{
				if(value.getOwningPlugin() == this.m_plugin)
				{
					if(!(value.value() instanceof RemoteEntity))
						continue;

					RemoteEntity e = (RemoteEntity)value.value();
					if(e.getManager() == this)
						return true;
				}
			}
			return false;
		}
		else
		{
			// WAS NMSUtil.getRemoteEntityFromLiving
			RemoteEntity entity = this.m_conversionService.getRemoteEntityFromLiving(inEntity);
			return entity != null && entity.getManager() == this;
		}
	}

	@Override
	public RemoteEntity getRemoteEntityByID(int inID)
	{
		return this.m_entities.get(inID);
	}

	@Override
	public Collection<RemoteEntity> getRemoteEntitiesByName(String inName)
	{
		List<RemoteEntity> entities = new ArrayList<RemoteEntity>();

		for(RemoteEntity entity : this.getAllEntities())
		{
			if(entity != null)
			{
				if(entity.getName().equals(inName))
					entities.add(entity);
			}
		}

		return entities;
	}

	@Override
	public void addRemoteEntity(int inID, RemoteEntity inEntity)
	{
		this.m_entities.put(inID, inEntity);
	}

	@Override
	public void despawnAll()
	{
		this.despawnAll(DespawnReason.CUSTOM);
	}

	@Override
	public void despawnAll(DespawnReason inReason)
	{
		if(this.m_entities.size() == 0)
			return;

		if(inReason == DespawnReason.PLUGIN_DISABLE)
			this.despawnAll(inReason, this.shouldSaveOnDisable());

		this.despawnAll(inReason, false);
	}

	@Override
	public void despawnAll(DespawnReason inReason, boolean inSave)
	{
		if(inSave)
			this.saveEntities();

		for(RemoteEntity entity : this.m_entities.values())
		{
			entity.despawn(inReason);
		}
		this.m_entities.clear();
	}

	@Override
	public Collection<RemoteEntity> getAllEntities()
	{
		return this.m_entities.values();
	}

	@Override
	public int getEntityCount()
	{
		return this.m_entities.size();
	}

	@Override
	public int saveEntities()
	{
		if(this.m_serializer == null)
			return 0;

		EntityData[] data = new EntityData[this.m_entities.size()];
		int pos = 0;
		for(RemoteEntity entity : this.m_entities.values())
		{
			data[pos] = this.m_serializer.prepare(entity);
			pos++;
		}
		this.m_serializer.save(data);
		return data.length;
	}

	@Override
	public int loadEntities()
	{
		if(this.m_serializer == null)
			return 0;

		EntityData[] data = this.m_serializer.loadData();
		for(EntityData entity : data)
		{
			this.m_serializer.create(entity);
		}

		return data.length;
	}

	@Override
	public IEntitySerializer getSerializer()
	{
		return this.m_serializer;
	}

	@Override
	public void setEntitySerializer(IEntitySerializer inSerializer)
	{
		this.m_serializer = inSerializer;
	}

	@Override
	public void setRemovingDespawned(boolean inState)
	{
		this.m_removeDespawned = inState;
	}

	@Override
	public boolean shouldRemoveDespawnedEntities()
	{
		return this.m_removeDespawned;
	}

	@Override
	public void setSaveOnDisable(boolean inSave)
	{
		this.m_saveOnDisable = inSave;
	}

	@Override
	public boolean shouldSaveOnDisable()
	{
		return this.m_saveOnDisable;
	}

	@Override
	public Collection<RemoteEntity> getEntitiesByType(RemoteEntityType inType)
	{
		return this.getEntitiesByType(inType, false);
	}

	@Override
	public Collection<RemoteEntity> getEntitiesByType(RemoteEntityType inType, boolean inSpawnedOnly)
	{
		List<RemoteEntity> entities = new ArrayList<RemoteEntity>();
		for(RemoteEntity entity : this.m_entities.values())
		{
			if(entity.getType() == inType)
			{
				if(inSpawnedOnly && !entity.isSpawned())
					continue;

				entities.add(entity);
			}
		}

		return entities;
	}

	@Override
	public int getNextFreeID()
	{
		return this.getNextFreeID(0);
	}

	@Override
	public int getNextFreeID(int inStart)
	{
		Set<Integer> ids = this.m_entities.keySet();
		while(ids.contains(inStart))
		{
			inStart++;
		}

		return inStart;
	}

	@Override
	public void removeEntity(int inID)
	{
		this.removeEntity(inID, true);
	}

	@Override
	public CreateEntityContext prepareEntity(RemoteEntityType inType)
	{
		return new CreateEntityContext(this).withType(inType);
	}

	@Override
	public RemoteEntity createEntity(RemoteEntityType inType, Location inLocation)
	{
		return this.createEntity(inType, inLocation, true);
	}

	@Override
	public RemoteEntity createNamedEntity(RemoteEntityType inType, Location inLocation, String inName)
	{
		return this.createNamedEntity(inType, inLocation, inName, true);
	}

	@Override
	public void removeEntity(int inID, boolean inDespawn)
	{
		if(this.m_entities.containsKey(inID) && inDespawn)
			this.m_entities.get(inID).despawn(DespawnReason.CUSTOM);

		this.m_entities.remove(inID);
	}

	@Override
	public RemoteEntity getRemoteEntityFromEntity(LivingEntity inEntity)
	{
		if(!this.isRemoteEntity(inEntity))
			return null;

		for (RemoteEntity remoteEntity : this.getAllEntities())
		{
			if (remoteEntity.getBukkitEntity() == inEntity)
				return remoteEntity;
		}

		return this.m_conversionService.getRemoteEntityFromLiving(inEntity);
	}
}