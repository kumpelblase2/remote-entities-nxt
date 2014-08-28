package de.kumpelblase2.remoteentities.api.features;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.persistence.*;
import de.kumpelblase2.remoteentities.services.CustomInventoryService;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

@IgnoreSerialization
public class RemoteInventoryFeature extends RemoteFeature implements InventoryFeature
{
	@SerializeAs(pos = 1)
	private Inventory m_inventory;
	protected int m_size;
	private final CustomInventoryService m_inventoryService;

	public RemoteInventoryFeature()
	{
		this(36);
	}

	public RemoteInventoryFeature(int inSize)
	{
		this(null);
		this.m_size = inSize;
	}

	public RemoteInventoryFeature(Inventory inInventory)
	{
		super("INVENTORY");
		this.m_inventory = inInventory;
		this.m_inventoryService = Bukkit.getServicesManager().getRegistration(CustomInventoryService.class).getProvider();
	}

	@Override
	public Inventory getInventory()
	{
		return this.m_inventory;
	}

	public void onAdd(RemoteEntity inEntity)
	{
		super.onAdd(inEntity);
		this.m_inventory = this.m_inventoryService.createInventory(this.m_entity, this.m_size);
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return PersistenceUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}