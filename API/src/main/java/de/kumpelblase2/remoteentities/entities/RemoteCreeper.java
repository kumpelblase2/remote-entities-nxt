package de.kumpelblase2.remoteentities.entities;

import de.kumpelblase2.remoteentities.api.Fightable;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public interface RemoteCreeper extends RemoteEntity, Fightable
{
	public void explode();
	public void explode(int inModifier);
}
