package de.kumpelblase2.remoteentities.entities;

import java.util.*;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.events.*;
import de.kumpelblase2.remoteentities.api.features.*;
import de.kumpelblase2.remoteentities.api.thinking.*;
import de.kumpelblase2.remoteentities.nms.RemoteSpeedModifier;
import de.kumpelblase2.remoteentities.persistence.ISingleEntitySerializer;
import de.kumpelblase2.remoteentities.utilities.*;
import net.minecraft.server.v1_7_R1.*;
import net.minecraft.server.v1_7_R1.World;
import org.bukkit.*;
import org.bukkit.Chunk;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_7_R1.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public abstract class RemoteBaseEntity<T extends LivingEntity> implements RemoteEntity
{
	private final int m_id;
	protected Mind m_mind;
	protected FeatureSet m_features;
	protected boolean m_isStationary = false;
	protected final RemoteEntityType m_type;
	protected EntityLiving m_entity;
	protected boolean m_isPushable = true;
	protected final EntityManager m_manager;
	protected Location m_unloadedLocation;
	protected String m_nameToSpawnWith;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;
	protected double m_speed = -1;
	protected AttributeModifier m_speedModifier;
	protected Map<EntitySound, Object> m_sounds;

	public RemoteBaseEntity(int inID, RemoteEntityType inType, EntityManager inManager)
	{
		this.m_id = inID;
		this.m_mind = new Mind(this);
		this.m_features = new FeatureSet(this);
		this.m_type = inType;
		this.m_manager = inManager;
		this.m_sounds = new EnumMap<EntitySound, Object>(EntitySound.class);
		this.setupSounds();
	}

	@Override
	public int getID()
	{
		return this.m_id;
	}

	@Override
	public EntityManager getManager()
	{
		return this.m_manager;
	}

	@Override
	public Mind getMind()
	{
		return this.m_mind;
	}

	@Override
	public FeatureSet getFeatures()
	{
		return this.m_features;
	}

	@Override
	public void setStationary(boolean inState)
	{
		this.setStationary(inState, false);
	}

	@Override
	public void setStationary(boolean inState, boolean inKeepHeadFixed)
	{
		this.m_isStationary = inState;
		if(!inKeepHeadFixed)
		{
			this.getMind().resetFixedYaw();
			this.getMind().resetFixedPitch();
		}
	}

	@Override
	public boolean isStationary()
	{
		return this.m_isStationary;
	}

	@Override
	public RemoteEntityType getType()
	{
		return this.m_type;
	}

	@Override
	public EntityLiving getHandle()
	{
		return this.m_entity;
	}

	@Override
	public T getBukkitEntity()
	{
		if(this.isSpawned())
			return (T)this.m_entity.getBukkitEntity();

		return null;
	}

	@Override
	public void setYaw(float inYaw)
	{
		this.setYaw(inYaw, false);
	}

	@Override
	public void setYaw(float inYaw, boolean inRotate)
	{
		if(!this.isSpawned())
			return;

		Location newLoc = this.getBukkitEntity().getLocation();
		newLoc.setYaw(inYaw);
		if(inRotate)
			this.move(newLoc);
		else
		{
			if(this.isStationary())
				this.getMind().fixYawAt(inYaw);

			this.m_entity.yaw = inYaw;
			this.m_entity.aO = inYaw;
		}
	}

	@Override
	public void setPitch(float inPitch)
	{
		if(!this.isSpawned())
			return;

		if(this.isStationary())
			this.getMind().fixPitchAt(inPitch);

		this.m_entity.pitch = inPitch;
	}

	@Override
	public void setHeadYaw(float inHeadYaw)
	{
		if(!this.isSpawned())
			return;

		if(this.isStationary())
			this.getMind().fixHeadYawAt(inHeadYaw);

		this.m_entity.aP = inHeadYaw;
		this.m_entity.aQ = inHeadYaw;
		if(!(this.m_entity instanceof EntityHuman))
			this.m_entity.aN = inHeadYaw;
	}

	@Override
	public void spawn(Location inLocation)
	{
		if(this.isSpawned())
			return;

		RemoteEntitySpawnEvent event = new RemoteEntitySpawnEvent(this, inLocation);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;

		inLocation = event.getSpawnLocation();

		try
		{
			EntityTypesEntry entry = EntityTypesEntry.fromEntity(this.getNativeEntityName());
			ReflectionUtil.registerEntityType(this.getType().getEntityClass(), this.getNativeEntityName(), entry.getID());
			WorldServer worldServer = ((CraftWorld)inLocation.getWorld()).getHandle();
			this.m_entity = this.m_type.getEntityClass().getConstructor(World.class, RemoteEntity.class).newInstance(worldServer, this);
			this.m_entity.setPositionRotation(inLocation.getX(), inLocation.getY(), inLocation.getZ(), inLocation.getYaw(), inLocation.getPitch());
			worldServer.addEntity(this.m_entity, SpawnReason.CUSTOM);
			entry.restore();
			LivingEntity bukkitEntity = this.getBukkitEntity();
			if(bukkitEntity != null)
			{
				bukkitEntity.setMetadata("remoteentity", new FixedMetadataValue(this.m_manager.getPlugin(), this));
				if(this.getName() != null && this.getName().length() > 0)
				{
					bukkitEntity.setCustomName(this.getName());
					bukkitEntity.setCustomNameVisible(true);
				}
				bukkitEntity.setRemoveWhenFarAway(false);
			}

			this.setHeadYaw(inLocation.getYaw());
			this.setYaw(inLocation.getYaw());
			if(!inLocation.getBlock().getRelative(BlockFace.DOWN).isEmpty())
				this.m_entity.onGround = true;

			if(this.m_speed != -1)
				this.setSpeed(this.m_speed);
			else
				this.setSpeed(0.7d);

			if(this.m_speedModifier != null)
			{
				this.addSpeedModifier(this.m_speedModifier.d(), (this.m_speedModifier.c() == 0));
				this.m_speedModifier = null;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		this.m_unloadedLocation = null;
	}

	@Override
	public void spawn(Location inLocation, boolean inForce)
	{
		Chunk c = inLocation.getChunk();
		if(c == null)
			return;

		if(!c.isLoaded() && inForce)
		{
			if(!c.load())
				return;
		}

		this.spawn(inLocation);
	}

	@Override
	public boolean despawn(DespawnReason inReason)
	{
		RemoteEntityDespawnEvent event = new RemoteEntityDespawnEvent(this, inReason);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled() && inReason != DespawnReason.PLUGIN_DISABLE)
			return false;

		if(inReason != DespawnReason.CHUNK_UNLOAD && inReason != DespawnReason.NAME_CHANGE)
		{
			for(Behavior behaviour : this.getMind().getBehaviours())
			{
				behaviour.onRemove();
			}

			this.getMind().clearBehaviours();
		}
		else
			this.m_unloadedLocation = (this.getBukkitEntity() != null ? this.getBukkitEntity().getLocation() : null);

		if(this.getBukkitEntity() != null)
			this.getBukkitEntity().remove();
		this.m_entity = null;
		return true;
	}

	@Override
	public void setSpeed(double inSpeed)
	{
		if(this.m_entity == null)
			this.m_speed = inSpeed;
		else
			this.m_entity.getAttributeInstance(GenericAttributes.d).setValue(inSpeed);
	}

	/**
	 * Copies the inventory from the given player to the inventory of this entity.
	 *
	 * @param inPlayer	Player to copy inventory from
	 */
	public void copyInventory(Player inPlayer)
	{
		this.copyInventory(inPlayer, false);
	}

	/**
	 * Copies the inventory from the given player to the inventory of this entity.
	 *
	 * @param inPlayer			Player to copy inventory from
	 * @param inIgnoreArmor		If armor should not be copied or if it should
	 */
	public void copyInventory(Player inPlayer, boolean inIgnoreArmor)
	{
		this.copyInventory(inPlayer.getInventory());
		EntityEquipment equip = this.getBukkitEntity().getEquipment();
		if(!inIgnoreArmor)
			equip.setArmorContents(inPlayer.getInventory().getArmorContents());

		if(this.getInventory() instanceof CraftInventoryPlayer)
			((CraftInventoryPlayer)this.getInventory()).setHeldItemSlot(inPlayer.getInventory().getHeldItemSlot());
		else
			equip.setItemInHand(inPlayer.getItemInHand());
	}

	/**
	 * Copies the the contents of the given inventory to the inventory of this entity.
	 *
	 * @param inInventory	Inventory to copy from.
	 */
	public void copyInventory(Inventory inInventory)
	{
		if(this.getInventory() != null)
			this.getInventory().setContents(inInventory.getContents());
	}

	/**
	 * Gets the inventory of this entity.
	 *
	 * @return	Inventory
	 */
	public Inventory getInventory()
	{
		if(this.getHandle() instanceof RemoteEntityHandle)
			return ((RemoteEntityHandle)this.getHandle()).getInventory();

		if(!this.getFeatures().hasFeature(InventoryFeature.class))
			return null;

		return this.getFeatures().getFeature(InventoryFeature.class).getInventory();
	}

	@Override
	public boolean save()
	{
		if(this.getManager().getSerializer() instanceof ISingleEntitySerializer)
		{
			ISingleEntitySerializer serializer = (ISingleEntitySerializer)this.getManager().getSerializer();
			serializer.save(serializer.prepare(this));
			return true;
		}

		return false;
	}

	public String getName()
	{
		if(!this.isSpawned())
			return this.m_nameToSpawnWith;

		return this.getBukkitEntity().getCustomName();
	}

	public void setName(String inName)
	{
		if(this.isSpawned())
		{
			if(inName == null)
			{
				this.getBukkitEntity().setCustomNameVisible(false);
				this.getBukkitEntity().setCustomName(null);
			}
			else
			{
				this.getBukkitEntity().setCustomNameVisible(true);
				this.getBukkitEntity().setCustomName(inName);
			}
		}
		else
			this.m_nameToSpawnWith = inName;
	}

	/**
	 * Gets the location the entity was last unloaded.
	 *
	 * @return	unloading location
	 */
	public Location getUnloadedLocation()
	{
		return this.m_unloadedLocation;
	}

	Vector onPush(double inX, double inY, double inZ)
	{
		RemoteEntityPushEvent event = new RemoteEntityPushEvent(this, new Vector(inX, inY, inZ));
		event.setCancelled(!this.isPushable() || this.isStationary());
		Bukkit.getPluginManager().callEvent(event);

		if(!event.isCancelled())
			return event.getVelocity();
		else
			return null;
	}

	boolean onCollide(Entity inEntity)
	{
		if(this.getMind() == null)
			return true;

		if(this.m_lastBouncedId != inEntity.getEntityId() || System.currentTimeMillis() - this.m_lastBouncedTime > 1000)
		{
			RemoteEntityTouchEvent event = new RemoteEntityTouchEvent(this, inEntity);
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled())
				return false;

			if(inEntity instanceof Player && this.getMind().canFeel() && this.getMind().hasBehavior(TouchBehavior.class))
			{
				if(inEntity.getLocation().distanceSquared(getBukkitEntity().getLocation()) <= 1)
					this.getMind().getBehavior(TouchBehavior.class).onTouch((Player)inEntity);
			}
		}

		this.m_lastBouncedTime = System.currentTimeMillis();
		this.m_lastBouncedId = inEntity.getEntityId();
		return true;
	}

	void onDeath()
	{
		if(this.getMind().hasBehavior(DeathBehavior.class))
			this.getMind().getBehavior(DeathBehavior.class).onDeath();

		this.getMind().clearMovementDesires();
		this.getMind().clearTargetingDesires();
	}

	boolean onInteract(Player inEntity)
	{
		if(this.getFeatures().hasFeature(TradingFeature.class))
		{
			TradingFeature feature = this.getFeatures().getFeature(TradingFeature.class);
			feature.openFor(inEntity);
			return false;
		}

		if(this.getMind() == null)
			return true;

		if(this.getMind().canFeel())
		{
			RemoteEntityInteractEvent event = new RemoteEntityInteractEvent(this, inEntity);
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled())
				return false;

			if(this.getMind().hasBehavior(InteractBehavior.class))
				this.getMind().getBehavior(InteractBehavior.class).onInteract(inEntity);
		}
		return true;
	}

	protected abstract void setupSounds();
}