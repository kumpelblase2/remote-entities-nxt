package de.kumpelblase2.remoteentities.entities;

import org.bukkit.Location;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public interface RemotePlayer extends RemoteEntity
{
	public boolean enterBed(Location inLocation);
	public void leaveBed();
	public boolean isSleeping();
	public void doArmSwing();
	public void fakeDamage();
}