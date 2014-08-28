package de.kumpelblase2.remoteentities.api.features;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;
import de.kumpelblase2.remoteentities.TypeParams;

public class Features
{
	private static final Set<Class<? extends Feature>> s_features = new HashSet<Class<? extends Feature>>();

	private Features()
	{
	}

	public static void registerFeature(Class<? extends Feature> inFeature)
	{
		s_features.add(inFeature);
	}

	public static <T extends Feature> T ofType(Class<T> inType, TypeParams inParams)
	{
		for(Class<? extends Feature> featureType : s_features)
		{
			if(featureType.isAssignableFrom(inType))
			{
				for(Constructor c : featureType.getConstructors())
				{
					if(c.getParameterTypes().length == 1)
					{
						try
						{
							return (T)c.newInstance(inParams);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}

		return null;
	}
}