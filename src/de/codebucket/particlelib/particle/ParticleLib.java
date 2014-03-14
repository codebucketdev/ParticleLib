package de.codebucket.particlelib.particle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.codebucket.particlelib.ParticleLibary;


public class ParticleLib 
{
	ParticleLibary plugin;
	private static ParticleLib instance;
	
	public ParticleLib(ParticleLibary plugin)
	{
		instance = this;
		this.plugin = plugin;		
	}
	
	public void playWorldParticle(Location location, Particle particle)
	{
		try 
		{
			for(Player player : getPlayersInRange(location, 16))
			{
				ParticleLibary.getParticlePacket().sendParticlePacket(player, location, particle);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void playCustomParticle(Location location, ParticleRadius radius, Particle particle)
	{
		try 
		{
			for(Player player : getPlayersInRange(location, 16))
			{
				ParticleLibary.getParticlePacket().sendParticlePacket(player, location, radius, particle);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void playWorldParticle(Player player, Location location, Particle particle)
	{
		try 
		{
			if(isPlayerInRange(player, location, 16))
			{
				ParticleLibary.getParticlePacket().sendParticlePacket(player, location, particle);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void playCustomParticle(Player player, Location location, ParticleRadius radius, Particle particle)
	{
		try 
		{
			if(isPlayerInRange(player, location, 16))
			{
				ParticleLibary.getParticlePacket().sendParticlePacket(player, location, radius, particle);
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
			for(Player player : getPlayersInRange(location, 60))
			{
				ParticleLibary.getFireworkPacket().sendFireworkPacket(player, location, effect);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void playFireworkEffect(Player player, Location location, FireworkEffect effect)
	{
		try 
		{
			if(isPlayerInRange(player, location, 60))
			{
				ParticleLibary.getFireworkPacket().sendFireworkPacket(player, location, effect);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private List<Player> getPlayersInRange(Location center, double range)
	{
		List<Player> players = new ArrayList<Player>();
		String name = center.getWorld().getName();
		double squared = range * range;
		for (Player player : Bukkit.getOnlinePlayers())
		{
			if (player.getWorld().getName().equals(name) && player.getLocation().distanceSquared(center) <= squared)
			{
				players.add(player);
			}
		}
		return players;
	}
	
	private boolean isPlayerInRange(Player player, Location center, double range)
	{
		return getPlayersInRange(center, range).contains(player);
	}
	
	public static ParticleLib getAPI()
	{
		return instance;
	}
}
