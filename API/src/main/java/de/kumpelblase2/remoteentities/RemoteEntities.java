package de.kumpelblase2.remoteentities;

import java.io.File;
import java.util.logging.Logger;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.exceptions.PluginNotEnabledException;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

public class RemoteEntities
{
	private static RemoteEntitiesAPI s_api;

	private RemoteEntities()
	{
	}

	public static EntityManager createManager(Plugin inPlugin)
	{
		if(s_api == null)
			throw new PluginNotEnabledException();

		return s_api.createManager(inPlugin);
	}

	public static EntityManager createManager(Plugin inPlugin, boolean inRemoveDespawned)
	{
		if(s_api == null)
			throw new PluginNotEnabledException();

		return s_api.createManager(inPlugin, inRemoveDespawned);
	}

	public static void registerCustomManager(EntityManager inManager, String inName)
	{
		if(s_api == null)
			throw new PluginNotEnabledException();

		s_api.registerCustomManager(inManager, inName);
	}

	public static EntityManager getManagerOfPlugin(String inName)
	{
		if(s_api == null)
			throw new PluginNotEnabledException();

		return s_api.getManagerOfPlugin(inName);
	}

	public static boolean hasManagerForPlugin(String inName)
	{
		if(s_api == null)
			throw new PluginNotEnabledException();

		return s_api.hasManagerForPlugin(inName);
	}

	public static boolean isRemoteEntity(LivingEntity inEntity)
	{
		if(s_api == null)
			throw new PluginNotEnabledException();

		return s_api.isRemoteEntity(inEntity);
	}

	public static RemoteEntity getRemoteEntityFromEntity(LivingEntity inEntity)
	{
		if(s_api == null)
			throw new PluginNotEnabledException();

		return s_api.getRemoteEntityFromEntity(inEntity);
	}

	public static Logger getLogger()
	{
		if(s_api == null)
			throw new PluginNotEnabledException();

		return s_api.getPlugin().getLogger();
	}

	public static File getDataFolder()
	{
		if(s_api == null)
			throw new PluginNotEnabledException();

		return s_api.getPlugin().getDataFolder();
	}

	public static RemoteEntitiesAPI getImplementation()
	{
		return s_api;
	}
}