package de.codebucket.particlelib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.codebucket.particlelib.packet.PacketHandler;

public class Main extends JavaPlugin
{
	ParticleLib api;
	PacketHandler packet;
	
	private String bukkitVersion = "UNKNOWN";
	private String serverVersion = "UNKNOWN";
	
	@Override
	public void onEnable() 
	{
		//INITIALIZE
		checkBukkitVersion();
		checkServerVersion();
		this.api = new ParticleLib(this);
		this.packet = new PacketHandler(this);
		
		//INFORMATION
		getLogger().info("Version 0.8 by Codebucket");
	}
	
	@Override
	public void onDisable()
	{
		
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
	
	public String getBukkitVersion()
	{
		return bukkitVersion;
	}
	
	public String getServerVersion()
	{
		return serverVersion;
	}
}
