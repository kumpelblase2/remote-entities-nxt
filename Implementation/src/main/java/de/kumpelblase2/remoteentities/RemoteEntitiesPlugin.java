package de.kumpelblase2.remoteentities;

import java.io.*;
import java.net.URL;
import java.util.*;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.exceptions.PluginNotEnabledException;
import de.kumpelblase2.remoteentities.helper.ReflectionUtil;
import javassist.*;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class RemoteEntitiesPlugin extends JavaPlugin implements RemoteEntitiesAPI
{
	private final Map<String, EntityManager> m_managers = new HashMap<String, EntityManager>();
	private static RemoteEntitiesPlugin s_instance;

	@Override
	public void onEnable()
	{
		s_instance = this;
		RemoteEntityType.update();
		this.getServer().getPluginManager().registerEvents(new DisableListener(), this);
	}

	@Override
	public void onDisable()
	{
		for(EntityManager manager : m_managers.values())
		{
			manager.despawnAll(DespawnReason.PLUGIN_DISABLE);
			if(manager instanceof RemoteEntityManager)
				((RemoteEntityManager)manager).unregisterEntityLoader();
		}
		s_instance = null;
	}

	/**
	 * Creates a manager for your plugin
	 *
	 * @param inPlugin	plugin using that manager
	 * @return			instance of a manager
	 */
	public EntityManager createManager(Plugin inPlugin) throws PluginNotEnabledException
	{
		return createManager(inPlugin, false);
	}

	/**
	 * Creates a manager for your plugin
	 * You can also specify whether despawned entities should be removed or not
	 *
	 * @param inPlugin				plugin using that manager
	 * @param inRemoveDespawned		automatically removed despawned entities
	 * @return						instance of a manager
	 */
	public EntityManager createManager(Plugin inPlugin, boolean inRemoveDespawned)
	{
		EntityManager manager = new EntityManager(inPlugin, inRemoveDespawned);
		registerCustomManager(manager, inPlugin.getName());
		return manager;
	}

	/**
	 * Adds custom created manager to internal map
	 *
	 * @param inManager custom manager
	 * @param inName	name of the plugin using it
	 */
	public void registerCustomManager(EntityManager inManager, String inName)
	{
		this.m_managers.put(inName, inManager);
	}

	/**
	 * Gets the manager of a specific plugin
	 *
	 * @param inName	name of the plugin
	 * @return			EntityManager of that plugin
	 */
	public EntityManager getManagerOfPlugin(String inName)
	{
		return this.m_managers.get(inName);
	}

	/**
	 * Checks if a specific plugin has registered a manager
	 *
	 * @param inName	name of the plugin
	 * @return			true if the given plugin has a manager, false if not
	 */
	public boolean hasManagerForPlugin(String inName)
	{
		return this.m_managers.containsKey(inName);
	}

	/**
	 * Checks if the given entity is a RemoteEntity created by any manager.
	 *
	 * @param inEntity	entity to check
	 * @return			true if it is a RemoteEntity, false if not
	 */
	public boolean isRemoteEntity(LivingEntity inEntity)
	{
		for(EntityManager manager : this.m_managers.values())
		{
			if(manager.isRemoteEntity(inEntity))
				return true;
		}
		return false;
	}

	/**
	 * Gets the RemoteEntity from a given entity which can be created by any manager.
	 *
	 * @param inEntity	entity
	 * @return			RemoteEntity
	 */
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
		return this;
	}

	@Override
	public boolean onCommand(CommandSender inSender, Command inCommand, String inLabel, String[] inArgs)
	{
		if(inCommand.getName().equals("remoteentities"))
		{
			if(inArgs.length == 0)
				inSender.sendMessage("Please provide arguments.");
			else
			{
				if(inArgs[0].equalsIgnoreCase("rebuild"))
				{
					if(inSender instanceof ConsoleCommandSender)
					{
						//TODO
					}
					else
						inSender.sendMessage("Only the console can do that.");
				}
			}
		}
		return true;
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
				if(manager instanceof RemoteEntityManager)
					((RemoteEntityManager)manager).unregisterEntityLoader();
			}
		}
	}
}