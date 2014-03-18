package de.codebucket.particlelib.protocol;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.ProtocolLibrary;

public class SoundPacketFix 
{
	private Plugin plugin;
	private List<SoundPacketListener> listeners;
	
	public SoundPacketFix(Plugin plugin)
	{
		this.plugin = plugin;
		this.listeners = new ArrayList<>();
	}
	
	public void registerSound(String soundname)
	{
		SoundPacketListener listener = new SoundPacketListener(plugin, soundname);
		ProtocolLibrary.getProtocolManager().addPacketListener(listener);
		listeners.add(listener);
	}
	
	public void unregisterSound(String soundname)
	{
		for(SoundPacketListener listener : listeners)
		{
			if(listener.getSoundname().equals(soundname))
			{
				ProtocolLibrary.getProtocolManager().removePacketListener(listener);
			}
		}
	}
	
	public void unregisterSounds()
	{
		for(SoundPacketListener listener : listeners)
		{
			ProtocolLibrary.getProtocolManager().removePacketListener(listener);
		}
	}
	
	public List<SoundPacketListener> getListeners()
	{
		return listeners;
	}
}
