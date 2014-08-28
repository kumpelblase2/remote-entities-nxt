package de.kumpelblase2.remoteentities.api;

import java.util.*;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.features.FeatureSet;
import de.kumpelblase2.remoteentities.api.thinking.Mind;
import de.kumpelblase2.remoteentities.persistence.ISingleEntitySerializer;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public abstract class BaseRemoteEntity implements RemoteEntity
{
	private final int m_id;
	private final Mind m_mind;
	private final EntityManager m_manager;
	private final RemoteEntityType m_type;
	private final FeatureSet m_featureSet;
	protected final Map<EntitySound, Object> m_sounds;
	private boolean m_stationary;
	private boolean m_pushable;
	private double m_speed;

	protected BaseRemoteEntity(int inId, EntityManager inManager, RemoteEntityType inType)
	{
		this.m_id = inId;
		this.m_manager = inManager;
		this.m_type = inType;
		this.m_mind = new Mind(this);
		this.m_featureSet = new FeatureSet(this);
		this.m_sounds = new HashMap<EntitySound, Object>();
	}

	@Override
	public boolean move(Location inLocation)
	{
		return this.move(inLocation, this.getSpeed());
	}

	@Override
	public boolean move(LivingEntity inEntity)
	{
		return this.move(inEntity, this.getSpeed());
	}

	@Override
	public void setYaw(float inYaw)
	{
		this.setYaw(inYaw, true);
	}

	@Override
	public void spawn(Location inLocation)
	{
		this.spawn(inLocation, false);
	}

	@Override
	public void setStationary(boolean inState)
	{
		this.m_stationary = inState;
	}

	@Override
	public int getID()
	{
		return this.m_id;
	}

	@Override
	public Mind getMind()
	{
		return this.m_mind;
	}

	@Override
	public EntityManager getManager()
	{
		return this.m_manager;
	}

	@Override
	public RemoteEntityType getType()
	{
		return this.m_type;
	}

	@Override
	public FeatureSet getFeatures()
	{
		return this.m_featureSet;
	}

	@Override
	public boolean isStationary()
	{
		return this.m_stationary;
	}

	@Override
	public boolean isPushable()
	{
		return this.m_pushable;
	}

	@Override
	public void setPushable(boolean inPushable)
	{
		this.m_pushable = inPushable;
	}

	@Override
	public double getSpeed()
	{
		return this.m_speed;
	}

	@Override
	public void setSpeed(double inSpeed)
	{
		this.m_speed = inSpeed;
	}

	@Override
	public boolean save()
	{
		if(this.getManager().getSerializer() instanceof ISingleEntitySerializer)
		{
			ISingleEntitySerializer serializer = (ISingleEntitySerializer)this.getManager().getSerializer();
			serializer.save(serializer.prepare(this));
			return true;
		}

		return false;
	}

	@Override
	public String getSound(EntitySound inType)
	{
		Object sound = this.m_sounds.get(inType);
		if(sound instanceof String)
			return (String)sound;
		else
		{
			Random generator = new Random();
			Object[] values = this.m_sounds.values().toArray();
			return (String)values[generator.nextInt(values.length)];
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getSound(EntitySound inType, String inKey)
	{
		Object sounds = this.m_sounds.get(inType);
		if(!(sounds instanceof Map))
			return null;

		return ((Map<String, String>)sounds).get(inKey);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getSounds(EntitySound inType)
	{
		Object sounds = this.m_sounds.get(inType);
		if(sounds instanceof String)
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put("default", (String)sounds);
			return map;
		}
		else
		{
			return (Map<String, String>)sounds;
		}
	}

	@Override
	public boolean hasSound(EntitySound inType)
	{
		return this.getSound(inType) != null;
	}

	@Override
	public boolean hasSound(EntitySound inType, String inKey)
	{
		return this.getSound(inType, inKey) != null;
	}

	@Override
	public void setSound(EntitySound inType, String inSound)
	{
		this.m_sounds.put(inType, inSound);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setSound(EntitySound inType, String inKey, String inSound)
	{
		Object sounds = this.m_sounds.get(inType);
		if(sounds instanceof String)
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put(inKey, inSound);
			this.m_sounds.put(inType, map);
		}
		else
			((Map<String, String>)sounds).put(inKey, inSound);
	}

	@Override
	public void setSounds(EntitySound inType, Map<String, String> inSounds)
	{
		this.m_sounds.put(inType, inSounds);
	}
}