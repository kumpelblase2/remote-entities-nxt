package de.kumpelblase2.remoteentities.persistence;

/**
 * This class is used to serialize and deserialize any kind of object
 */
public interface ObjectParser
{
	public Object serialize(Object inObject);
	public Object deserialize(ParameterData inData);
}