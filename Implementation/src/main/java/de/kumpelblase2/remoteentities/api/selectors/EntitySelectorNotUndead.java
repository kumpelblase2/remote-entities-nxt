package de.kumpelblase2.remoteentities.api.selectors;

import net.minecraft.server.v1_7_R1.*;

public class EntitySelectorNotUndead implements IEntitySelector
{
	@Override
	public boolean a(Entity inEntity)
	{
		return inEntity instanceof EntityLiving && ((EntityLiving)inEntity).getMonsterType() != EnumMonsterType.UNDEAD;
	}
}