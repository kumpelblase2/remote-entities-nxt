package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_7_R1.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.features.InventoryFeature;
import de.kumpelblase2.remoteentities.api.thinking.*;
import de.kumpelblase2.remoteentities.api.thinking.goals.*;
import de.kumpelblase2.remoteentities.nms.PathfinderGoalSelectorHelper;

public class RemoteSnowmanEntity extends EntitySnowman implements RemoteEntityHandle
{
	private final RemoteEntity m_remoteEntity;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;

	public RemoteSnowmanEntity(World world)
	{
		this(world, null);
	}

	public RemoteSnowmanEntity(World world, RemoteEntity inRemoteEntity)
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
		Mind mind = this.getRemoteEntity().getMind();
		mind.addMovementDesires(getDefaultMovementDesires());
		mind.addTargetingDesires(getDefaultTargetingDesires());
	}

	@Override
	public void h()
	{
		super.h();
		if(this.getRemoteEntity() != null)
			this.getRemoteEntity().getMind().tick();
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

	@Override
	public void a(EntityLiving entityliving, float f) {
		//Taken from EntitySnowman.java#62 - 72
		//modified to work with custom sounds
		EntitySnowball entitysnowball = new EntitySnowball(this.world, this);
		double d0 = entityliving.locX - this.locX;
		double d1 = entityliving.locY + (double) entityliving.getHeadHeight() - 1.100000023841858D - entitysnowball.locY;
		double d2 = entityliving.locZ - this.locZ;
		float f1 = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2F;

		entitysnowball.shoot(d0, d1 + (double) f1, d2, 1.6F, 12.0F);
		this.makeSound(this.m_remoteEntity.getSound(EntitySound.ATTACK), 1.0F, 1.0F / (this.aI().nextFloat() * 0.4F + 0.8F));
		this.world.addEntity(entitysnowball);
	}

	public static DesireItem[] getDefaultMovementDesires()
	{
		return new DesireItem[] {
				new DesireItem(new DesireRangedAttack(RemoteProjectileType.ENTITY_DEFAULT, 20, 10), 1),
				new DesireItem(new DesireWanderAround(), 2),
				new DesireItem(new DesireLookAtNearest(EntityHuman.class, 6), 3),
				new DesireItem(new DesireLookRandomly(), 4)
		};
	}

	public static DesireItem[] getDefaultTargetingDesires()
	{
		return new DesireItem[] {
				new DesireItem(new DesireFindNearestTarget(EntityInsentient.class, 16, false, true, 0), 1)
		};
	}
}