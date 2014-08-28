package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;

/**
 * Using this desire the entity will search for the nearest entity of the given type and sets it as the next target, but only at night.
 */
public class DesireFindNearestTargetAtNight extends DesireFindNearestTarget
{
	@Deprecated
	public DesireFindNearestTargetAtNight(RemoteEntity inEntity, Class<?> inTargetClass, float inDistance, boolean inShouldCheckSight, boolean inShouldMelee, int inChance)
	{
		super(inEntity, inTargetClass, inDistance, inShouldCheckSight, inShouldMelee, inChance);
		this.m_onlyAtNight = true;
	}

	@Deprecated
	public DesireFindNearestTargetAtNight(RemoteEntity inEntity, Class<?> inTargetClass, float inDistance, boolean inShouldCheckSight, int inChance)
	{
		this(inEntity, inTargetClass, inDistance, inShouldCheckSight, false, inChance);
	}

	@Deprecated
	public DesireFindNearestTargetAtNight(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight, boolean inMelee, Class<?> inTargetClass, int inChance)
	{
		this(inEntity, inTargetClass, inDistance, inShouldCheckSight, inMelee, inChance);
	}

	public DesireFindNearestTargetAtNight(Class<?> inTargetClass, float inDistance, boolean inShouldCheckSight, boolean inShouldMelee, int inChance)
	{
		super(inTargetClass, inDistance, inShouldCheckSight, inShouldMelee, inChance);
		this.m_onlyAtNight = true;
	}

	public DesireFindNearestTargetAtNight(Class<?> inTargetClass, float inDistance, boolean inShouldCheckSight, int inChance)
	{
		this(inTargetClass, inDistance, inShouldCheckSight, false, inChance);
	}

	public DesireFindNearestTargetAtNight(float inDistance, boolean inShouldCheckSight, boolean inMelee, Class<?> inTargetClass, int inChance)
	{
		this(inTargetClass, inDistance, inShouldCheckSight, inMelee, inChance);
	}
}