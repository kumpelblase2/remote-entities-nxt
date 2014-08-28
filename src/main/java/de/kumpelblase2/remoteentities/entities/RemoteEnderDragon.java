package de.kumpelblase2.remoteentities.entities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.EntitySound;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteEnderDragon extends RemoteAttackingBaseEntity<EnderDragon>
{
	protected boolean m_shouldDestroyBlocks = false;
	protected boolean m_normalFly = false;

	public RemoteEnderDragon(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteEnderDragon(int inID, RemoteEnderDragonEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.EnderDragon, inManager);
		this.m_entity = inEntity;

		Bukkit.getPluginManager().registerEvents(new Listener()
			{
				@EventHandler
				public void onEntityExplode(EntityExplodeEvent event)
				{
					if(event.getEntity() instanceof EnderDragon)
					{
						if(event.getEntity() == getBukkitEntity() && !shouldDestroyBlocks())
							event.setCancelled(true);
					}
					else if(event.getEntity() instanceof ComplexEntityPart)
					{
						if(((ComplexEntityPart)event.getEntity()).getParent() == getBukkitEntity() && !shouldDestroyBlocks())
							event.setCancelled(true);
					}
				}
			},
		this.m_manager.getPlugin());
	}

	@Override
	public boolean move(Location inLocation, double inSpeed)
	{
		((RemoteEnderDragonEntity)this.m_entity).setTargetLocation(inLocation);
		return true;
	}

	/**
	 * Checks whether it should destroy blocks or not.
	 *
	 * @return	True if it should, false if not
	 */
	public boolean shouldDestroyBlocks()
	{
		return this.m_shouldDestroyBlocks;
	}

	/**
	 * Sets whether it should destroy blocks or not.
	 *
	 * @param inState 	destroy blocks
	 */
	public void shouldDestroyBlocks(boolean inState)
	{
		this.m_shouldDestroyBlocks = inState;
	}

	/**
	 * Gets whether the dragon should continue flying normally (like the default dragon does), or only when told by a plugin call.
	 *
	 * @return  True for normal flight, otherwise false
	 */
	public boolean shouldNormallyFly()
	{
		return this.m_normalFly;
	}

	/**
	 * Sets whether the dragon should continue flying normally (like the default dragon does), or only when told by a plugin call.
	 *
	 * @param inState   True for normal flight, false for plugin triggered flight
	 */
	public void shouldNormallyFly(boolean inState)
	{
		this.m_normalFly = inState;
	}

	@Override
	public String getNativeEntityName()
	{
		return "EnderDragon";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.RANDOM, "mob.enderdragon.growl");
		this.setSound(EntitySound.HURT, "mob.enderdragon.hit");
	}
}