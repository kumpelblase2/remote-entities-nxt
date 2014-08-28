package de.kumpelblase2.remoteentities.helper.ruby;

import java.lang.reflect.Constructor;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import de.kumpelblase2.remoteentities.api.events.RemoteEntityCreateEvent;
import de.kumpelblase2.remoteentities.helper.NMSUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

public class EntityHelper
{
	public static RemoteEntity createEntity(EntityManager inManager, RemoteEntityType inType, int inID)
	{
		try
		{
			Constructor<? extends RemoteEntity> constructor = inType.getRemoteClass().getConstructor(int.class, EntityManager.class);
			RemoteEntity entity = constructor.newInstance(inID, inManager);
			RemoteEntityCreateEvent event = new RemoteEntityCreateEvent(entity);
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled())
				return null;

			return entity;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static RemoteEntity createNamedEntity(EntityManager inManager, RemoteEntityType inType, int inID, String inName)
	{
		try
		{
			Constructor<? extends RemoteEntity> constructor = inType.getRemoteClass().getConstructor(int.class, String.class, EntityManager.class);
			RemoteEntity entity = constructor.newInstance(inID, inName, inManager);
			RemoteEntityCreateEvent event = new RemoteEntityCreateEvent(entity);
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled())
				return null;

			return entity;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static RemoteEntityType getTypeForEntity(LivingEntity inEntity)
	{
		return RemoteEntityType.getByEntityClass(NMSUtil.getNMSClassFromEntity(inEntity));
	}
}