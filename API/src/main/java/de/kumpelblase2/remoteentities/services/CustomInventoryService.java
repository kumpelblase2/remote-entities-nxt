package de.kumpelblase2.remoteentities.services;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface CustomInventoryService
{
	public Inventory createInventory(InventoryHolder inHolder, int inSize);
}