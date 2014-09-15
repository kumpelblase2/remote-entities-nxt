package de.kumpelblase2.remoteentities.api;

import java.util.*;

public class RemoteEntityType
{
	// We set them to null because we still want to have the static handle even though we don't have the implementation yet.
	public static final RemoteEntityType Human = new RemoteEntityType("Human", null, null, true);
	public static final RemoteEntityType Zombie = new RemoteEntityType("Zombie",null, null, false);
	public static final RemoteEntityType Spider = new RemoteEntityType("Spider", null, null, false);
	public static final RemoteEntityType Creeper = new RemoteEntityType("Creeper", null, null, false);
	public static final RemoteEntityType Skeleton = new RemoteEntityType("Skeleton", null, null, false);
	public static final RemoteEntityType Blaze = new RemoteEntityType("Blaze", null, null, false);
	public static final RemoteEntityType CaveSpider = new RemoteEntityType("CaveSpider", null, null, false);
	public static final RemoteEntityType Chicken = new RemoteEntityType("Chicken", null, null, false);
	public static final RemoteEntityType Cow = new RemoteEntityType("Cow", null, null, false);
	public static final RemoteEntityType EnderDragon = new RemoteEntityType("EnderDragon", null, null, false);
	public static final RemoteEntityType Enderman = new RemoteEntityType("Enderman", null, null, false);
	public static final RemoteEntityType Ghast = new RemoteEntityType("Ghast", null, null, false);
	public static final RemoteEntityType IronGolem = new RemoteEntityType("IronGolem", null, null, false);
	public static final RemoteEntityType LavaSlime = new RemoteEntityType("LavaSlime", null, null, false);
	public static final RemoteEntityType Mushroom = new RemoteEntityType("Mushroom", null, null, false);
	public static final RemoteEntityType Ocelot = new RemoteEntityType("Ocelote", null, null, false);
	public static final RemoteEntityType Pig = new RemoteEntityType("Pig", null, null, false);
	public static final RemoteEntityType Pigmen = new RemoteEntityType("Pigmen", null, null, false);
	public static final RemoteEntityType Sheep = new RemoteEntityType("Sheep", null, null, false);
	public static final RemoteEntityType Silverfish = new RemoteEntityType("Silverfish", null, null, false);
	public static final RemoteEntityType Slime = new RemoteEntityType("Slime", null, null, false);
	public static final RemoteEntityType Snowman = new RemoteEntityType("Snowman", null, null, false);
	public static final RemoteEntityType Squid = new RemoteEntityType("Squid", null, null, false);
	public static final RemoteEntityType Villager = new RemoteEntityType("Villager", null, null, false);
	public static final RemoteEntityType Wolf = new RemoteEntityType("Wolf", null, null, false);
	public static final RemoteEntityType Witch = new RemoteEntityType("Witch", null, null, false);
	public static final RemoteEntityType Wither = new RemoteEntityType("Wither", null, null, false);
	public static final RemoteEntityType Bat = new RemoteEntityType("Bat", null, null, false);
	public static final RemoteEntityType Horse = new RemoteEntityType("Horse", null, null, false);

	private static List<RemoteEntityType> values;
	private static RemoteEntityType[] lastConvert = new RemoteEntityType[0];

	private Class<?> m_entityClass;
	private Class<? extends RemoteEntity> m_remoteClass;
	private boolean m_isNamed = false;
	private final String m_name;

	private RemoteEntityType(String inName, Class<? extends RemoteEntity> inRemoteClass, Class<?> inEntityClass, boolean inNamed)
	{
		this.m_name = inName;
		this.m_entityClass = inEntityClass;
		this.m_remoteClass = inRemoteClass;
		this.m_isNamed = inNamed;
		if(values == null)
			values = new ArrayList<RemoteEntityType>();

		if(!values.contains(this))
			values.add(this);
	}

	public Class<? extends RemoteEntity> getRemoteClass()
	{
		return this.m_remoteClass;
	}

	public Class<?> getEntityClass()
	{
		return this.m_entityClass;
	}

	public boolean isNamed()
	{
		return this.m_isNamed;
	}

	public void setRemoteClass(Class<? extends RemoteEntity> inClass)
	{
		this.m_remoteClass = inClass;
	}

	public void setEntityClass(Class<?> inClass)
	{
		this.m_entityClass = inClass;
	}

	public void setNamed(boolean inNamed)
	{
		this.m_isNamed = inNamed;
	}

	public String toString()
	{
		return this.name();
	}

	public String name()
	{
		return this.m_name;
	}

	public int ordinal()
	{
		for(int i = 0; i < lastConvert.length; i++)
		{
			if(lastConvert[i] == this)
				return i;
		}
		return -1; //This shouldn't happen however.
	}

	public boolean equals(Object o)
	{
		return o instanceof RemoteEntity && ((RemoteEntityType)o).name().equals(this.name());
	}

	public int hashCode()
	{
		return this.name().hashCode();
	}

	public static RemoteEntityType[] values()
	{
		return lastConvert;
	}

	public static RemoteEntityType valueOf(String inName)
	{
		for(RemoteEntityType type : values)
		{
			if(type.name().equals(inName))
				return type;
		}
		return null;
	}

	public static boolean addType(String inName, Class<?> inEntityClass, Class<? extends RemoteEntity> inRemoteClass, boolean isNamed)
	{
		return addType(new RemoteEntityType(inName, inRemoteClass, inEntityClass, isNamed));
	}

	private static boolean addType(RemoteEntityType inType)
	{
		if(valueOf(inType.name()) != null)
			return false;

		if(!values.contains(inType))
			values.add(inType);

		update();
		return true;
	}

	public static boolean removeType(String inName)
	{
		Iterator<RemoteEntityType> it = values.iterator();
		int pos = 0;
		while(it.hasNext())
		{
			RemoteEntityType type = it.next();
			if(type.name().equals(inName))
			{
				if(pos <= 27)
					return false;

				it.remove();
				update();
				return true;
			}
			pos++;
		}
		return false;
	}

	public static RemoteEntityType getByEntityClass(Class<?> inEntityClass)
	{
		for(RemoteEntityType type : values())
		{
			if(type.getEntityClass().equals(inEntityClass) || type.getEntityClass().getSuperclass().equals(inEntityClass) || type.getEntityClass().isAssignableFrom(inEntityClass))
				return type;
		}
		return null;
	}

	public static RemoteEntityType getByRemoteClass(Class<? extends RemoteEntity> inRemoteClass)
	{
		for(RemoteEntityType type : values())
		{
			if(type.getRemoteClass().equals(inRemoteClass))
				return type;
		}
		return null;
	}

	public static void update()
	{
		lastConvert = values.toArray(new RemoteEntityType[values.size()]);
	}
}
