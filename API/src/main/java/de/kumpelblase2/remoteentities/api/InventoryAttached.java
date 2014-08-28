package de.kumpelblase2.remoteentities.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface InventoryAttached
{
	public void copyInventory(Player inPlayer);
	public void copyInventory(Player inPlayer, boolean inIgnoreArmor);
	public void copyInventory(Inventory inInventory);
	public Inventory getInventory();

}