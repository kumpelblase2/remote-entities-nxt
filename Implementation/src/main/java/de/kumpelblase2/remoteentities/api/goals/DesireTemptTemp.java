package de.kumpelblase2.remoteentities.api.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import net.minecraft.server.v1_7_R1.Items;
import net.minecraft.server.v1_7_R1.PathfinderGoalTempt;

public class DesireTemptTemp extends PathfinderGoalTempt
{
	private final RemoteEntity m_entity;

	public DesireTemptTemp(RemoteEntity inEntity)
	{
		super(null, 0, Items.SEEDS, false);
		this.m_entity = inEntity;
	}

	@Override
	public boolean f()
	{
		if(this.m_entity.getMind().getMovementDesire(DesireTempt.class) == null)
			return false;

		return this.m_entity.getMind().getMovementDesire(DesireTempt.class).isTempted();
	}
}
