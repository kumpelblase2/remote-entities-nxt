package de.kumpelblase2.remoteentities.persistence;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import de.kumpelblase2.remoteentities.RemoteEntities;
import de.kumpelblase2.remoteentities.api.thinking.Desire;

public class PersistenceUtil
{
	/**
	 * Gets the data for the parameters of the classes' constructor
	 *
	 * @param inClass   The class to get the data for
	 * @return          List of data for each parameter in order
	 */
	public static List<ParameterData> getParameterDataForClass(Object inClass)
	{
		Class<?> clazz = inClass.getClass();
		List<ParameterData> parameters = new ArrayList<ParameterData>();
		Set<String> membersLooked = new HashSet<String>();
		while(clazz != Object.class && clazz != Desire.class)
		{
			for(Field field : clazz.getDeclaredFields())
			{
				field.setAccessible(true);
				if(membersLooked.contains(field.getName()))
					continue;

				membersLooked.add(field.getName());
				for(Annotation an : field.getAnnotations())
				{
					if(an instanceof SerializeAs)
					{
						SerializeAs sas = (SerializeAs)an;
						try
						{
							Object value = field.get(inClass);
							parameters.add(new ParameterData(Math.max(0, sas.pos() - 1), field.getType().getName(), value, sas.special()));
							break;
						}
						catch(Exception e)
						{
							RemoteEntities.getLogger().warning("Unable to add desire parameter. " + e.getMessage());
						}
					}
				}
			}

			clazz = clazz.getSuperclass();
		}
		return parameters;
	}
}