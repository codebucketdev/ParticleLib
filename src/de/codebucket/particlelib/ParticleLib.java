package de.codebucket.particlelib;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.codebucket.particlelib.packet.PacketHandler;

public class ParticleLib 
{
	Main plugin;
	PacketHandler packet;
	
	private static ParticleLib instance;
	
	public ParticleLib(Main plugin)
	{
		this.plugin = plugin;
		this.packet = new PacketHandler(plugin);
		
		instance = this;
	}
	
	public void playWorldParticle(Location location, Particle particle)
	{
		try 
		{
			for(Player player : location.getWorld().getPlayers())
			{
				packet.sendWorldPacket(player, location, particle);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void playFireworkEffect(Location location, FireworkEffect effect)
	{
		try 
		{
			packet.sendFireworkPacket(location.getWorld(), location, effect);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static ParticleLib getAPI()
	{
		return instance;
	}
}
