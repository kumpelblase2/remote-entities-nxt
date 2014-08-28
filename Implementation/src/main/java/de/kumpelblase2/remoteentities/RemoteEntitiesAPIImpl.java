package de.kumpelblase2.remoteentities;

import java.util.HashMap;
import java.util.Map;
import de.kumpelblase2.remoteentities.api.DespawnReason;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

class RemoteEntitiesAPIImpl implements RemoteEntitiesAPI
{
	private final Map<String, EntityManager> m_managers = new HashMap<String, EntityManager>();
	private final JavaPlugin m_plugin;

	public RemoteEntitiesAPIImpl(JavaPlugin inPlugin)
	{
		this.m_plugin = inPlugin;
	}

	@Override
	public EntityManager createManager(Plugin inPlugin)
	{
		return createManager(inPlugin, false);
	}

	@Override
	public EntityManager createManager(Plugin inPlugin, boolean inRemoveDespawned)
	{
		return null;
	}

	@Override
	public void registerCustomManager(EntityManager inManager, String inName)
	{
		this.m_managers.put(inName, inManager);
	}

	@Override
	public EntityManager getManagerOfPlugin(String inName)
	{
		return this.m_managers.get(inName);
	}

	@Override
	public boolean hasManagerForPlugin(String inName)
	{
		return this.m_managers.containsKey(inName);
	}

	@Override
	public boolean isRemoteEntity(LivingEntity inEntity)
	{
		for(EntityManager manager : this.m_managers.values())
		{
			if(manager.isRemoteEntity(inEntity))
				return true;
		}
		return false;
	}

	@Override
	public RemoteEntity getRemoteEntityFromEntity(LivingEntity inEntity)
	{
		for(EntityManager manager : this.m_managers.values())
		{
			RemoteEntity entity = manager.getRemoteEntityFromEntity(inEntity);
			if(entity != null)
				return entity;
		}
		return null;
	}

	@Override
	public JavaPlugin getPlugin()
	{
		return this.m_plugin;
	}

	@Override
	public void shutdown()
	{
		for(EntityManager manager : m_managers.values())
		{
			manager.despawnAll(DespawnReason.PLUGIN_DISABLE);
			if(manager instanceof BaseEntityManager)
				((BaseEntityManager)manager).teardown();
		}
	}
}