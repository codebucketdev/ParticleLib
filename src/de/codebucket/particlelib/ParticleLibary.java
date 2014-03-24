package de.codebucket.particlelib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.codebucket.particlelib.packet.FireworkPacket;
import de.codebucket.particlelib.packet.ParticlePacket;
import de.codebucket.particlelib.particle.ParticleLib;

public class ParticleLibary extends JavaPlugin
{
	private static ParticleLib api;
	private static ParticlePacket particle;
	private static FireworkPacket firework;
	private String bukkitVersion = "UNKNOWN";
	private String serverVersion = "UNKNOWN";
	
	@Override
	public void onEnable() 
	{
		//INITIALIZE
		checkBukkitVersion();
		checkServerVersion();
		api = new ParticleLib(this);
		particle = new ParticlePacket(this);
		firework = new FireworkPacket(this);
		
		//INFORMATION
		getLogger().info("Version 1.2 by Codebucket");
	}
	
	@Override
	public void onDisable()
	{
		//INFORMATION
		getLogger().info("Plugin disabled. Using Reflection.");
	}
	
	private void checkServerVersion()
	{
		try 
		{
			for(Package pa : Package.getPackages())
			{
				if(pa.getName().startsWith("net.minecraft.server."))
				{
					serverVersion = pa.getName().split("\\.")[3];
					getLogger().info("This Server is running with CraftBukkit version " + bukkitVersion + "!");
					break;
				}
			}
		} 
		catch (Exception e) 
		{
			getLogger().severe("Unknown or unsupported CraftBukkit version! Is the Plugin up to date?");
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}
	
	private void checkBukkitVersion()
	{
		String version = Bukkit.getVersion();
		version = version.replace("(", "");
		version = version.replace(")", "");
		version = version.split(" ")[2];
		this.bukkitVersion = version;
	}
	
	public static ParticlePacket getParticlePacket()
	{
		return particle;
	}
	
	public static FireworkPacket getFireworkPacket()
	{
		return firework;
	}
	
	public static ParticleLib getAPI()
	{
		return api;
	}
	
	public String getBukkitVersion()
	{
		return bukkitVersion;
	}
	
	public String getServerVersion()
	{
		return serverVersion;
	}
}
