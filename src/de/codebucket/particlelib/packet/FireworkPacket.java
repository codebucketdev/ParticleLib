package de.codebucket.particlelib.packet;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import de.codebucket.particlelib.ParticleLibary;

public class FireworkPacket
{
	ParticleLibary plugin;
	private Constructor<?> packetPlayOutEntityStatus;
	private Method getEntityHandle;
	private Method getFireworkHandle;
	private Field getPlayerConnection;
	private Method sendPacket;

	public FireworkPacket(ParticleLibary plugin)
	{
		try 
		{
			this.plugin = plugin;
			packetPlayOutEntityStatus = getMCClass("PacketPlayOutEntityStatus").getConstructor(getMCClass("Entity"), byte.class);
			getEntityHandle = getCraftClass("entity.CraftPlayer").getMethod("getHandle");
			getFireworkHandle = getCraftClass("entity.CraftEntity").getMethod("getHandle");
			getPlayerConnection = getMCClass("EntityPlayer").getDeclaredField("playerConnection");
			sendPacket = getMCClass("PlayerConnection").getMethod("sendPacket", getMCClass("Packet"));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public void sendFireworkPacket(final Player player, final Location loc, final FireworkEffect fe)
	{
		try
		{
			Firework fw = loc.getWorld().spawn(loc, Firework.class);
			FireworkMeta data = (FireworkMeta) fw.getFireworkMeta();
			data.clearEffects();
			data.addEffect(fe);
			data.setPower(0);
			fw.setFireworkMeta(data);
			
			Object nms_firework = null;
			nms_firework = getFireworkHandle.invoke(fw);
			Object epacket = packetPlayOutEntityStatus.newInstance(nms_firework, (byte) 17);
			sendPacket(player, epacket);
			
			Class<?> packetPlayOutEntityDestroy = getMCClass("PacketPlayOutEntityDestroy");
			Object dpacket = packetPlayOutEntityDestroy.newInstance();
			Field a = packetPlayOutEntityDestroy.getDeclaredField("a");
			a.setAccessible(true);
			a.set(dpacket, new int[] { fw.getEntityId() });
			for(Player pl : fw.getWorld().getPlayers())
				sendPacket(pl, dpacket);
			
			fw.teleport(fw.getLocation().subtract(0.0, 128.0, 0.0));
		}
		catch (Exception e) 
		{
			throw new PacketInstantiationException("Packet instantiation failed", e);
		}
	}

	private void sendPacket(final Player player, final Object packet) 
	{
		try
		{
			Object nms_player = getEntityHandle.invoke(player);
			Object nms_connection = getPlayerConnection.get(nms_player);
			sendPacket.invoke(nms_connection, packet);
		}
		catch (Exception e) 
		{
			throw new PacketSendingException("Failed to send a packet to player '" + player.getName() + "'",  e);
		}
	}

	private Class<?> getMCClass(String name) throws ClassNotFoundException 
	{
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
		String className = "net.minecraft.server." + version + name;
		return Class.forName(className);
	}

	private Class<?> getCraftClass(String name) throws ClassNotFoundException 
	{
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
		String className = "org.bukkit.craftbukkit." + version + name;
		return Class.forName(className);
	}

}
