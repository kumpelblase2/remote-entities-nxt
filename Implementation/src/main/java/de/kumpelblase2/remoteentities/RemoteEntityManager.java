package de.kumpelblase2.remoteentities;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.events.RemoteEntityCreateEvent;
import de.kumpelblase2.remoteentities.exceptions.NoNameException;
import de.kumpelblase2.remoteentities.helper.NMSUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

public class RemoteEntityManager extends BaseEntityManager
{
	private final ChunkEntityLoader m_chunkEntityLoader;

	protected RemoteEntityManager(Plugin inPlugin)
	{
		super(inPlugin);
		this.m_chunkEntityLoader = new ChunkEntityLoader(this);
	}

	@Override
	protected void setup()
	{
		Bukkit.getPluginManager().registerEvents(this.m_chunkEntityLoader, RemoteEntities.getImplementation().getPlugin());
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				Iterator<RemoteEntity> it = RemoteEntityManager.this.getAllEntities().iterator();
				while(it.hasNext())
				{
					RemoteEntity entity = it.next();
					if(entity.getBukkitEntity() == null)
					{
						if(m_removeDespawned)
							it.remove();
					}
					else
					{
						entity.getHandle().C();
						if(entity.getHandle().dead)
						{
							if(entity.despawn(DespawnReason.DEATH))
								it.remove();
						}
					}
				}
			}
		}, 1L, 1L);
	}

	@Override
	public RemoteEntity createEntity(RemoteEntityType inType, Location inLocation, boolean inSetupGoals)
	{
		if(inType.isNamed())
			throw new NoNameException("Tried to spawn a named entity without name");

		Integer id = this.getNextFreeID();
		RemoteEntity entity = this.createEntity(inType, id);
		if(entity == null)
			return null;

		if(inLocation != null)
			this.m_chunkEntityLoader.queueSpawn(entity, inLocation, inSetupGoals);

		return entity;
	}

	RemoteEntity createEntity(RemoteEntityType inType, int inID)
	{
		try
		{
			Constructor<? extends RemoteEntity> constructor = inType.getRemoteClass().getConstructor(int.class, EntityManager.class);
			RemoteEntity entity = constructor.newInstance(inID, this);
			RemoteEntityCreateEvent event = new RemoteEntityCreateEvent(entity);
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled())
				return null;

			this.addRemoteEntity(inID, entity);
			return entity;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public RemoteEntity createNamedEntity(RemoteEntityType inType, Location inLocation, String inName, boolean inSetupGoals)
	{
		if(!inType.isNamed())
		{
			RemoteEntity entity = this.createEntity(inType, inLocation, inSetupGoals);
			if(entity == null)
				return null;

			entity.setName(inName);
			return entity;
		}

		Integer id = this.getNextFreeID();
		RemoteEntity entity = this.createNamedEntity(inType, id, inName);
		if(entity == null)
			return null;

		if(inLocation != null)
			this.m_chunkEntityLoader.queueSpawn(entity, inLocation, inSetupGoals);

		return entity;
	}

	RemoteEntity createNamedEntity(RemoteEntityType inType, int inID, String inName)
	{
		try
		{
			Constructor<? extends RemoteEntity> constructor = inType.getRemoteClass().getConstructor(int.class, String.class, EntityManager.class);
			RemoteEntity entity = constructor.newInstance(inID, inName, this);
			RemoteEntityCreateEvent event = new RemoteEntityCreateEvent(entity);
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled())
				return null;

			this.addRemoteEntity(inID, entity);
			return entity;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public RemoteEntity createRemoteEntityFromExisting(LivingEntity inEntity)
	{
		return this.createRemoteEntityFromExisting(inEntity, true);
	}

	@Override
	public RemoteEntity createRemoteEntityFromExisting(LivingEntity inEntity, boolean inDeleteOld)
	{
		RemoteEntityType type = RemoteEntityType.getByEntityClass(NMSUtil.getNMSClassFromEntity(inEntity));
		if(type == null)
			return null;

		Location originalSpot = inEntity.getLocation();
		String name = (inEntity instanceof HumanEntity) ? ((HumanEntity)inEntity).getName() : null;
		if(inDeleteOld)
			inEntity.remove();

		try
		{
			if(name == null)
				return this.createEntity(type, originalSpot, true);
			else
				return this.createNamedEntity(type, originalSpot, name, true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	void unregisterEntityLoader()
	{
		this.m_chunkEntityLoader.unregister();
	}
}