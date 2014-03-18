package de.codebucket.particlelib.protocol;

import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

@SuppressWarnings("deprecation")
public class SoundPacketFix 
{
	public static void registerSound(final Plugin plugin, final String soundname)
	{
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, ConnectionSide.SERVER_SIDE, Packets.Server.NAMED_SOUND_EFFECT)
		{
			@Override
			public void onPacketSending(PacketEvent event) 
			{
				String sound = event.getPacket().getStrings().read(0);
				if (soundname.equalsIgnoreCase(sound))
				{
					event.setCancelled(true);
				}
			}		
		});
	}
}
