package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_7_R1.*;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;
import de.kumpelblase2.remoteentities.utilities.WorldUtilities;

/**
 * Using this desire the entity will try to defend the nearest village at night, just like iron golems do.
 * However, this desire only does the targeting and not the movement which has to be done by a different desire.
 */
public class DesireDefendVillage extends DesireTargetBase
{
	protected EntityLiving m_nextTarget;

	@Deprecated
	public DesireDefendVillage(RemoteEntity inEntity)
	{
		this(inEntity, 16f, false, true);
	}

	@Deprecated
	public DesireDefendVillage(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight, boolean inShouldMelee)
	{
		this(inDistance, inShouldCheckSight, inShouldMelee);
		this.m_entity = inEntity;
	}

	public DesireDefendVillage()
	{
		this(16f, false, true);
	}

	public DesireDefendVillage(float inDistance, boolean inShouldCheckSight, boolean inShouldMelee)
	{
		super(inDistance, inShouldCheckSight, inShouldMelee);
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;

		Village nextVillage;
		if(this.getEntityHandle() instanceof EntityIronGolem)
			nextVillage = ((EntityIronGolem)this.getEntityHandle()).bX();
		else
			nextVillage = WorldUtilities.getClosestVillage(this.getEntityHandle());

		if(nextVillage == null)
			return false;
		else
		{
			this.m_nextTarget = nextVillage.b(this.getEntityHandle());
			if(!this.isSuitableTarget(this.m_nextTarget, false))
			{
				if(this.getEntityHandle().aI().nextInt(20) == 0)
				{
					this.m_nextTarget = nextVillage.c(this.getEntityHandle());
					return this.isSuitableTarget(this.m_nextTarget, false);
				}
				else
					return false;
			}
			return true;
		}
	}

	@Override
	public void startExecuting()
	{
		NMSUtil.setGoalTarget(this.getEntityHandle(), this.m_nextTarget);
		super.startExecuting();
	}
}