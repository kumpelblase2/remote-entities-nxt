package de.kumpelblase2.remoteentities.entities;

import de.kumpelblase2.remoteentities.api.Fightable;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public interface RemoteBat extends RemoteEntity, Fightable
{
	public boolean isHanging();
	public void setHanging(boolean inHanging);
}
