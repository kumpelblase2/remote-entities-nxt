package de.kumpelblase2.remoteentities.api.events;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

public class RemoteEntityPushEvent extends RemoteEvent
{
	private static final HandlerList handlers = new HandlerList();
	private Vector m_velocity;

	public RemoteEntityPushEvent(RemoteEntity inEntity, Vector inVelocity)
	{
		super(inEntity);
		this.m_velocity = inVelocity;
	}

	public Vector getVelocity()
	{
		return this.m_velocity;
	}

	public void setVelocity(Vector inVelocity)
	{
		this.m_velocity = inVelocity;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}