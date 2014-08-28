package de.kumpelblase2.remoteentities.api.features;

import de.kumpelblase2.remoteentities.services.NMSEntityService;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

public abstract class RemoteMateFeature extends RemoteFeature implements MateFeature
{
	private LivingEntity m_partner;
	private final NMSEntityService m_entityService;

	public RemoteMateFeature()
	{
		super("MATE");
		this.m_entityService = Bukkit.getServicesManager().getRegistration(NMSEntityService.class).getProvider();
	}

	@Override
	public boolean isPossiblePartner(LivingEntity inPartner)
	{
		return true;
	}

	@Override
	public boolean mate(LivingEntity inPartner)
	{
		if(this.isPossiblePartner(inPartner))
		{
			this.m_partner = inPartner;
			return true;
		}

		return false;
	}

	@Override
	public boolean isAffected()
	{
		/*EntityLiving handle = this.m_entity.getHandle();
		return !(handle instanceof EntityAnimal) || ((EntityAnimal)handle).cc();*/
		return this.m_entityService.checkAffected(this.m_entity.getBukkitEntity());
	}

	@Override
	public void resetAffection()
	{
		/*if(this.m_entity.getHandle() instanceof EntityAnimal)
			((EntityAnimal)this.m_entity.getHandle()).cd();*/
		this.m_entityService.resetAffection(this.m_entity.getBukkitEntity());
	}
}