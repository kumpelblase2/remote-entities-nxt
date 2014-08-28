package de.kumpelblase2.remoteentities.helper.trading;

import de.kumpelblase2.remoteentities.api.features.RemoteTradingFeature;
import net.minecraft.server.v1_7_R1.InventoryMerchant;

public class CustomMerchantInventory extends InventoryMerchant
{
	private final RemoteTradingFeature m_feature;

	public CustomMerchantInventory(RemoteTradingFeature inFeature)
	{
		super(null, new VirtualMerchant(inFeature));
		this.m_feature = inFeature;
	}

	@Override
	public String getInventoryName()
	{
		return this.m_feature.getTradeName();
	}
}