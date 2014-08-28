package de.kumpelblase2.remoteentities.api.pathfinding;

import java.util.List;

public class Path
{
	private final BlockNode[] m_nodes;
	private int m_pos;
	private float m_speed = -1;
	
	public Path(BlockNode... inNodes)
	{
		this.m_pos = -1;
		this.m_nodes = inNodes;
	}
	
	public Path(List<BlockNode> inNodes)
	{
		this(inNodes.toArray(new BlockNode[inNodes.size()]));
	}
	
	public void setCustomSpeed(float inSpeed)
	{
		this.m_speed = inSpeed;
	}
	
	public float getCustomSpeed()
	{
		return this.m_speed;
	}
	
	public boolean hasCustomSpeed()
	{
		return this.getCustomSpeed() != -1;
	}
	
	public BlockNode next()
	{
		if(this.m_pos >= this.m_nodes.length - 1)
			return null;

		this.m_pos++;
		return this.m_nodes[this.m_pos];
	}
	
	public BlockNode previous()
	{
		if(this.m_pos <= 1)
			return null;

		this.m_pos--;
		return this.m_nodes[this.m_pos];
	}
	
	public BlockNode start()
	{
		this.m_pos = -1;
		return this.next();
	}

	public BlockNode current()
	{
		if(this.m_pos < 0 || this.m_pos >= this.m_nodes.length)
			return null;

		return this.m_nodes[this.m_pos];
	}

	public void setPosition(int inPos)
	{
		if(inPos < 0)
			inPos = 0;
		else if(inPos >= this.m_nodes.length)
			inPos = this.m_nodes.length - 1;

		this.m_pos = inPos;
	}
	
	public boolean isDone()
	{
		return this.m_pos == this.m_nodes.length - 1;
	}
}