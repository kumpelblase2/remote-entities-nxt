package de.kumpelblase2.remoteentities;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.InputStreamReader;
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
	private final ScriptEngine m_jruby;
	private EntityManagerFactory m_managerFactory;

	public RemoteEntitiesAPIImpl(JavaPlugin inPlugin)
	{
		this.m_plugin = inPlugin;
		this.m_jruby = JRubyScriptEngineManager.getEngine();
		this.setupEngine();
	}

	@Override
	public EntityManager createManager(Plugin inPlugin)
	{
		return this.createManager(inPlugin, false);
	}

	@Override
	public EntityManager createManager(Plugin inPlugin, boolean inRemoveDespawned)
	{
		EntityManager manager = this.m_managerFactory.createManager();
		manager.setRemovingDespawned(inRemoveDespawned);
		if(manager instanceof BaseEntityManager)
			((BaseEntityManager)manager).setup(inPlugin);

		return manager;
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

	protected void setupEngine()
	{
		//TODO
		this.m_jruby.put("MC_VERSION", null);
		this.m_jruby.put("PLUGIN", this.m_plugin);
		try
		{
			this.m_jruby.eval(new InputStreamReader(RemoteEntitiesAPIImpl.class.getResourceAsStream("ruby/main.rb")));
			this.m_managerFactory = (EntityManagerFactory)this.m_jruby.eval("RemoteEntities::RemoteEntityManagerFactory.new");
		}
		catch(ScriptException e)
		{
			e.printStackTrace();
		}
	}
}