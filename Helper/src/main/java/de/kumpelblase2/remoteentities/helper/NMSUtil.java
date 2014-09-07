package de.kumpelblase2.remoteentities.helper;

import net.minecraft.server.v1_7_R1.*;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;

public class NMSUtil
{
	public static boolean isInHomeArea(EntityLiving inEntity)
	{
		return !NMSUtil.hasHomeArea(inEntity) || !(inEntity instanceof EntityCreature) || ((EntityCreature)inEntity).bS();
	}

	public static boolean isInHomeArea(EntityLiving inEntity, int x, int y, int z)
	{
		return !NMSUtil.hasHomeArea(inEntity) || !(inEntity instanceof EntityCreature) || ((EntityCreature)inEntity).b(x, y, z);
	}
}