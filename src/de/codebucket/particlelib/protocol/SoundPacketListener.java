package de.codebucket.particlelib.protocol;

import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;

public class SoundPacketListener implements PacketListener 
{
	private Plugin plugin;
	private String soundname;
	
	public SoundPacketListener(Plugin plugin, String soundname)
	{
		this.plugin = plugin;
		this.soundname = soundname;
	}
	
	@Override
	public void onPacketSending(PacketEvent event)
	{
		String sound = event.getPacket().getStrings().read(0);
		if (getSoundname().equals(sound)) 
		{
			event.setCancelled(true);
		}
	}
	
	@Override
	public void onPacketReceiving(PacketEvent pe) 
	{
		
	}

	@Override
	public Plugin getPlugin()
	{
		return plugin;
	}
	
	public String getSoundname()
	{
		return soundname;
	}

	@Override
	public ListeningWhitelist getReceivingWhitelist()
	{
		return null;
	}

	@Override
	public ListeningWhitelist getSendingWhitelist() 
	{
		return null;
	}
}
