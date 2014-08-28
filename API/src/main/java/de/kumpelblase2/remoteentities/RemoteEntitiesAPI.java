package de.kumpelblase2.remoteentities;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public interface RemoteEntitiesAPI
{
	public EntityManager createManager(Plugin inPlugin);

	public EntityManager createManager(Plugin inPlugin, boolean inRemoveDespawned);

	public void registerCustomManager(EntityManager inManager, String inName);

	public EntityManager getManagerOfPlugin(String inName);

	public boolean hasManagerForPlugin(String inName);

	public boolean isRemoteEntity(LivingEntity inEntity);

	public RemoteEntity getRemoteEntityFromEntity(LivingEntity inEntity);

	public JavaPlugin getPlugin();

	public void shutdown();
}