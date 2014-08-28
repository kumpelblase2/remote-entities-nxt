package de.kumpelblase2.remoteentities.services;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface CustomInventoryService
{
	public Inventory createInventory(InventoryHolder inHolder, int inSize);
	public Inventory createInventory(RemoteEntity inEntity, int inSize);
}