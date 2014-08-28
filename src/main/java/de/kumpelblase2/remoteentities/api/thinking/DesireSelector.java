package de.kumpelblase2.remoteentities.api.thinking;

import java.util.*;
import org.bukkit.Bukkit;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.events.*;

public class DesireSelector
{
	private final List<DesireItem> m_desires;
	private final List<DesireItem> m_executingDesires;
	private int m_tick = 0;
	protected int m_delay = 3;
	private final RemoteEntity m_entity;

	public DesireSelector(RemoteEntity inEntity)
	{
		this.m_entity = inEntity;
		this.m_desires = new ArrayList<DesireItem>();
		this.m_executingDesires = new ArrayList<DesireItem>();
	}

	public void onUpdate()
	{
		Iterator<DesireItem> it;
		DesireItem item;

		if(++this.m_tick % this.m_delay == 0)
		{
			it = this.m_desires.iterator();
			while(it.hasNext())
			{
				item = it.next();
				if(this.m_executingDesires.contains(item))
				{
					RemoteDesireStopEvent.StopReason reason = null;
					if(!this.hasHighestPriority(item))
						reason = RemoteDesireStopEvent.StopReason.LOWER_PRIORITY;
					else if(!item.getDesire().canContinue())
						reason = RemoteDesireStopEvent.StopReason.CANT_CONTINUE;

					if(reason == null)
						continue;

					RemoteDesireStopEvent event = new RemoteDesireStopEvent(item, reason);
					Bukkit.getPluginManager().callEvent(event);
					if(event.isCancelled())
						continue;

					item = event.getDesireItem();
					item.getDesire().stopExecuting();
					this.m_executingDesires.remove(item);
					if(item.getDesire() instanceof OneTimeDesire && ((OneTimeDesire)item.getDesire()).isFinished())
					{
						it.remove();
						continue;
					}
				}

				if(this.hasHighestPriority(item) && item.getDesire().shouldExecute())
				{
					RemoteDesireStartEvent event = new RemoteDesireStartEvent(item);
					Bukkit.getPluginManager().callEvent(event);
					if(event.isCancelled())
						continue;

					item = event.getDesireItem();
					item.getDesire().startExecuting();
					this.m_executingDesires.add(item);
				}
			}
			this.m_tick = 0;
		}
		else
		{
			it = this.m_executingDesires.iterator();
			while(it.hasNext())
			{
				item = it.next();
				if(!item.getDesire().canContinue())
				{
					RemoteDesireStopEvent event = new RemoteDesireStopEvent(item);
					Bukkit.getPluginManager().callEvent(event);
					item.getDesire().stopExecuting();
					if(item.getDesire() instanceof OneTimeDesire && ((OneTimeDesire)item.getDesire()).isFinished())
						this.m_desires.remove(item);

					it.remove();
				}
			}
		}

		it = this.m_executingDesires.iterator();
		while(it.hasNext())
		{
			item = it.next();
			if(!item.getDesire().update())
			{
				RemoteDesireStopEvent event = new RemoteDesireStopEvent(item);
				Bukkit.getPluginManager().callEvent(event);
				if(item.getDesire() instanceof OneTimeDesire && ((OneTimeDesire)item.getDesire()).isFinished())
					this.m_desires.remove(item);

				it.remove();
			}
		}
	}

	public void addDesire(Desire inDesire, int inPriority)
	{
		RemoteDesireAddEvent event = new RemoteDesireAddEvent(this.m_entity, inDesire, inPriority);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;

		inDesire.onAdd(this.m_entity);
		this.m_desires.add(new DesireItem(event.getDesire(), event.getPriority()));
	}

	public boolean hasHighestPriority(DesireItem inItem)
	{
		for(DesireItem item : this.m_desires)
		{
			if(item.equals(inItem))
				continue;

			if(inItem.getPriority() >= item.getPriority())
			{
				if(!areTasksCompatible(item.getDesire(), inItem.getDesire()) && this.m_executingDesires.contains(item))
					return false;
			}
			else if(!item.getDesire().isContinuous() && this.m_executingDesires.contains(item))
				return false;
		}
		return true;
	}

	public static boolean areTasksCompatible(Desire inFirstDesire, Desire inSecondDesire)
	{
		return inFirstDesire.getType().isCompatibleWith(inSecondDesire.getType());
	}

	public List<DesireItem> getDesires()
	{
		return this.m_desires;
	}

	public boolean removeDesireByType(Class<? extends Desire> inType)
	{
		List<DesireItem> temp = new ArrayList<DesireItem>();
		for(DesireItem item : this.m_desires)
		{
			if(item.getDesire().getClass().equals(inType) || item.getDesire().getClass().getSuperclass().equals(inType))
				temp.add(item);
		}

		if(temp.size() == 0)
			return false;

		if(temp.size() > 1)
		{
			DesireItem lowest = temp.get(0);
			for(DesireItem item : temp)
			{
				if(this.hasLowestPriority(item))
				{
					lowest = item;
					break;
				}
			}
			temp.clear();
			temp.add(lowest);
			RemoteDesireStopEvent event = new RemoteDesireStopEvent(lowest, RemoteDesireStopEvent.StopReason.REMOVE);
			Bukkit.getPluginManager().callEvent(event);
			lowest.getDesire().stopExecuting();
		}
		else
		{
			DesireItem t = temp.get(0);
			RemoteDesireStopEvent event = new RemoteDesireStopEvent(t, RemoteDesireStopEvent.StopReason.REMOVE);
			Bukkit.getPluginManager().callEvent(event);
			t.getDesire().stopExecuting();
		}

		this.m_desires.remove(temp.get(0));
		this.m_executingDesires.remove(temp.get(0));
		return true;
	}

	public void clearDesires()
	{
		for(DesireItem item : this.m_executingDesires)
		{
			RemoteDesireStopEvent event = new RemoteDesireStopEvent(item, RemoteDesireStopEvent.StopReason.REMOVE);
			Bukkit.getPluginManager().callEvent(event);
			item.getDesire().stopExecuting();
		}
		this.m_desires.clear();
		this.m_executingDesires.clear();
	}

	public int getHighestPriority()
	{
		int highest = 0;
		for(DesireItem item : this.m_desires)
		{
			if(item.getPriority() > highest)
				highest = item.getPriority();
		}

		return highest;
	}

	protected boolean hasLowestPriority(DesireItem inItem)
	{
		int lowest = inItem.getPriority();
		for(DesireItem item : this.m_desires)
		{
			if(item.getDesire().getClass().equals(inItem.getDesire().getClass()) || item.getDesire().getClass().getSuperclass().equals(inItem.getDesire().getClass()))
			{
				if(item.getPriority() < lowest)
					lowest = item.getPriority();
			}
		}
		return lowest == inItem.getPriority();
	}
}