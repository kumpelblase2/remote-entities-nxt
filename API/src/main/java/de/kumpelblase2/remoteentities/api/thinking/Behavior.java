package de.kumpelblase2.remoteentities.api.thinking;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.persistence.SerializableData;
import org.bukkit.event.Listener;

public interface Behavior extends Listener, Runnable, SerializableData
{
	/**
	 * Gets the name of the behavior
	 *
	 * @return name
	 */
	public String getName();

	/**
	 * Method that gets executed when the behavior gets added to the entity
	 */
	public void onAdd();

	/**
	 * Method that gets executed when the behavior gets removed from the entity
	 */
	public void onRemove();

	/**
	 * Gets the entity having this behavior
	 *
	 * @return entity
	 */
	public RemoteEntity getRemoteEntity();
}