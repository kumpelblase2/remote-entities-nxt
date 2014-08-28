package de.kumpelblase2.remoteentities.entities;

import java.lang.reflect.Field;
import net.minecraft.server.v1_7_R1.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.features.InventoryFeature;
import de.kumpelblase2.remoteentities.api.thinking.DesireItem;
import de.kumpelblase2.remoteentities.api.thinking.RideBehavior;
import de.kumpelblase2.remoteentities.nms.PathfinderGoalSelectorHelper;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public class RemoteGhastEntity extends EntityGhast implements RemoteEntityHandle
{
	private final RemoteEntity m_remoteEntity;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;

	public RemoteGhastEntity(World world)
	{
		this(world, null);
	}

	public RemoteGhastEntity(World world, RemoteEntity inRemoteEntity)
	{
		super(world);
		this.m_remoteEntity = inRemoteEntity;
		new PathfinderGoalSelectorHelper(this.goalSelector).clearGoals();
		new PathfinderGoalSelectorHelper(this.targetSelector).clearGoals();
	}

	@Override
	public Inventory getInventory()
	{
		if(!this.m_remoteEntity.getFeatures().hasFeature(InventoryFeature.class))
			return null;

		return this.m_remoteEntity.getFeatures().getFeature(InventoryFeature.class).getInventory();
	}

	@Override
	public RemoteEntity getRemoteEntity()
	{
		return this.m_remoteEntity;
	}

	@Override
	public void setupStandardGoals()
	{
	}

	@Override
	public void h()
	{
		super.h();
		if(this.getRemoteEntity() != null)
			this.getRemoteEntity().getMind().tick();
	}

	public void setTarget(Entity inTarget)
	{
		try
		{
			Field targetField = ReflectionUtil.getOrRegisterField(EntityGhast.class, "target");
			targetField.set(this, inTarget);
		}
		catch(Exception e)
		{
		}
	}

	public Entity getTarget()
	{
		try
		{
			Field targetField = ReflectionUtil.getOrRegisterField(EntityGhast.class, "target");
			return (Entity)targetField.get(this);
		}
		catch(Exception e)
		{
			return null;
		}
	}

	@Override
	public void g(double x, double y, double z)
	{
		if(this.m_remoteEntity == null)
		{
			super.g(x, y, z);
			return;
		}

		Vector vector = ((RemoteBaseEntity)this.m_remoteEntity).onPush(x, y, z);
		if(vector != null)
			super.g(vector.getX(), vector.getY(), vector.getZ());
	}

	@Override
	public void move(double d0, double d1, double d2)
	{
		if(this.m_remoteEntity != null && this.m_remoteEntity.isStationary())
			return;

		super.move(d0, d1, d2);
	}

	@Override
	public void e(float inXMotion, float inZMotion)
	{
		float[] motion = new float[] { inXMotion, inZMotion, (float)this.motY };
		if(this.m_remoteEntity.getMind().hasBehavior(RideBehavior.class))
			this.m_remoteEntity.getMind().getBehavior(RideBehavior.class).ride(motion);

		this.motY = (double)motion[2];
		super.e(motion[0], motion[1]);
	}

	@Override
	public void collide(Entity inEntity)
	{
		if(this.getRemoteEntity() == null)
		{
			super.collide(inEntity);
			return;
		}

		if(((RemoteBaseEntity)this.m_remoteEntity).onCollide(inEntity.getBukkitEntity()))
			super.collide(inEntity);
	}

	@Override
	public boolean a(EntityHuman entity)
	{
		if(this.getRemoteEntity() == null)
			return super.a(entity);

		if(!(entity.getBukkitEntity() instanceof Player))
			return super.a(entity);

		return ((RemoteBaseEntity)this.m_remoteEntity).onInteract((Player)entity.getBukkitEntity()) && super.a(entity);
	}

	@Override
	public void die(DamageSource damagesource)
	{
		((RemoteBaseEntity)this.m_remoteEntity).onDeath();
		super.die(damagesource);
	}

	@Override
	public boolean bk()
	{
		return true;
	}

	@Override
	protected String t()
	{
		return this.m_remoteEntity.getSound(EntitySound.RANDOM);
	}

	@Override
	protected String aT()
	{
		return this.m_remoteEntity.getSound(EntitySound.HURT);
	}

	@Override
	protected String aU()
	{
		return this.m_remoteEntity.getSound(EntitySound.DEATH);
	}

	public static DesireItem[] getDefaultMovementDesires()
	{
		return new DesireItem[0];
	}

	public static DesireItem[] getDefaultTargetingDesires()
	{
		return new DesireItem[0];
	}
}