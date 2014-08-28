package de.kumpelblase2.remoteentities;

import de.kumpelblase2.remoteentities.api.DespawnReason;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class RemoteEntitiesMain extends JavaPlugin
{
	private RemoteEntitiesAPI m_entitiesAPI;

	@Override
	public void onDisable()
	{
		this.m_entitiesAPI.shutdown();
		if(RemoteEntities.getImplementation() == this) // Don't set to null if someone else registered
			RemoteEntities.setImplementation(null);

		super.onDisable();
	}

	@Override
	public void onEnable()
	{
		this.m_entitiesAPI = new RemoteEntitiesAPIImpl(this);
		RemoteEntityType.update();
		this.getServer().getPluginManager().registerEvents(new DisableListener(), this);
		if(RemoteEntities.getImplementation() == null) // Only register if there's no implementation yet.
			RemoteEntities.setImplementation(this.m_entitiesAPI);

		super.onEnable();
	}

	class DisableListener implements Listener
	{
		@EventHandler
		public void onPluginDisable(PluginDisableEvent event)
		{
			EntityManager manager = RemoteEntities.getManagerOfPlugin(event.getPlugin().getName());
			if(manager != null)
			{
				manager.despawnAll(DespawnReason.PLUGIN_DISABLE);
				if(manager instanceof BaseEntityManager)
					((BaseEntityManager)manager).teardown();
			}
		}
	}
}