package de.kumpelblase2.remoteentities.api.selectors;

import net.minecraft.server.v1_7_R1.Entity;
import net.minecraft.server.v1_7_R1.IEntitySelector;

public class EntitySelectorLiving implements IEntitySelector
{
	@Override
	public boolean a(Entity inEntity)
	{
		return inEntity.isAlive();
	}
}