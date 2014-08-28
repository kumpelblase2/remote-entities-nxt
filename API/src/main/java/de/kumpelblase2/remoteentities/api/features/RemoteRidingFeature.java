package de.kumpelblase2.remoteentities.api.features;

import de.kumpelblase2.remoteentities.persistence.*;
import de.kumpelblase2.remoteentities.services.NMSMathService;
import org.bukkit.Bukkit;

public class RemoteRidingFeature extends RemoteFeature implements RidingFeature
{
	@SerializeAs(pos = 1)
	protected boolean m_isRideable;
	@SerializeAs(pos = 2)
	protected int m_temper;
	private final NMSMathService m_mathService;

	public RemoteRidingFeature()
	{
		this(false, 500);
	}

	public RemoteRidingFeature(boolean inRideable, int m_temper)
	{
		super("RIDING");
		this.m_isRideable = inRideable;
		this.m_temper = m_temper;
		this.m_mathService = Bukkit.getServicesManager().getRegistration(NMSMathService.class).getProvider();
	}

	@Override
	public boolean isPreparedToRide()
	{
		return this.m_isRideable;
	}

	@Override
	public void setRideable(boolean inStatus)
	{
		this.m_isRideable = inStatus;
	}

	@Override
	public int getTemper()
	{
		return m_temper;
	}

	@Override
	public void increaseTemper(int inTemper)
	{
		this.m_temper = this.m_mathService.increaseRandom(this.getTemper(), 0, 100);
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return PersistenceUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}