package de.kumpelblase2.remoteentities.helper.socket;

import java.io.IOException;
import java.net.SocketAddress;
import de.kumpelblase2.remoteentities.helper.ReflectionUtil;
import net.minecraft.server.v1_7_R1.MinecraftServer;
import net.minecraft.server.v1_7_R1.NetworkManager;

public class RemoteEntityNetworkManager extends NetworkManager
{

	public RemoteEntityNetworkManager(MinecraftServer server) throws IOException
	{
		super(false);

		this.assignReplacementNetworking();
	}

	private void assignReplacementNetworking()
	{
		ReflectionUtil.setNetworkChannel(this, new NullChannel(null));
		ReflectionUtil.setNetworkAddress(this, new SocketAddress(){});
	}
}