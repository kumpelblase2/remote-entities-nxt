package de.kumpelblase2.remoteentities.entities;

import de.kumpelblase2.remoteentities.api.Fightable;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public interface RemoteEnderDragon extends RemoteEntity, Fightable
{
	public boolean shouldDestroyBlocks();
	public void shouldDestroyBlocks(boolean inState);

	public boolean shouldFlyNormally();
	public void shouldFlyNormally(boolean inState);
}
