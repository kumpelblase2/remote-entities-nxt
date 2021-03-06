package de.kumpelblase2.remoteentities.api.events;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.pathfinding.CancelReason;
import org.bukkit.event.HandlerList;

public class RemotePathCancelEvent extends RemoteEvent
{
	private static final HandlerList handlers = new HandlerList();
	private final CancelReason m_reason;

	public RemotePathCancelEvent(RemoteEntity inEntity, CancelReason inReason)
	{
		super(inEntity);
		this.m_reason = inReason;
	}

	public CancelReason getReason()
	{
		return this.m_reason;
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