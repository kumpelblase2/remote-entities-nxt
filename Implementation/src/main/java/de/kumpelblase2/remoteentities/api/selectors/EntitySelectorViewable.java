package de.kumpelblase2.remoteentities.api.selectors;

import de.kumpelblase2.remoteentities.utilities.NMSUtil;
import net.minecraft.server.v1_7_R1.*;

public class EntitySelectorViewable implements IEntitySelector
{
	private final EntityLiving m_entity;

	public EntitySelectorViewable(EntityLiving inEntity)
	{
		this.m_entity = inEntity;
	}

	@Override
	public boolean a(Entity inEntity)
	{
		return NMSUtil.getEntitySenses(this.m_entity).canSee(inEntity);
	}
}