package de.kumpelblase2.remoteentities;

import java.util.HashMap;
import java.util.Map;

public class TypeParams
{
	private final Map<String, Object> m_params = new HashMap<String, Object>();

	public TypeParams with(String inName, Object inValue)
	{
		this.m_params.put(inName, inValue);
		return this;
	}

	public Map<String, Object> toHash()
	{
		return this.m_params;
	}
}