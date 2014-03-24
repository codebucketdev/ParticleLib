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
	private Field getPlayerConnection;
	private Method sendPacket;
	private Method getFireworkHandle;

	public FireworkPacket(ParticleLibary plugin)
	{
		try 
		{
			this.plugin = plugin;
			packetPlayOutEntityStatus = getMCClass("PacketPlayOutEntityStatus").getConstructor(getMCClass("Entity"), byte.class);
			getEntityHandle = getCraftClass("entity.CraftPlayer").getMethod("getHandle");
			getPlayerConnection = getMCClass("EntityPlayer").getDeclaredField("playerConnection");
			sendPacket = getMCClass("PlayerConnection").getMethod("sendPacket", getMCClass("Packet"));
			getFireworkHandle = getCraftClass("entity.CraftEntity").getMethod("getHandle");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public void sendFireworkPacket(final Player player, final Location loc, final FireworkEffect fe) 
	{
		Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() 
		{
			@Override
			public void run() 
			{
				sendPacket(player, createFireworkPacket(loc, fe));
			}
		}, 1L);
	}
	
	private Object createFireworkPacket(Location loc, FireworkEffect fe)
	{
		try
		{
			Object nms_firework = null;
			Firework firework = loc.getWorld().spawn(loc, Firework.class);
			FireworkMeta data = (FireworkMeta) firework.getFireworkMeta();
			data.clearEffects();
			data.addEffect(fe);
			data.setPower(3);
			firework.setFireworkMeta(data);
			firework.teleport(loc);
			nms_firework = getFireworkHandle.invoke(firework);
			Object packet = packetPlayOutEntityStatus.newInstance(nms_firework, (byte) 17);
			firework.remove();
			return packet;
		}
		catch(Exception e)
		{
			throw new PacketInstantiationException("Packet instantiation failed", e);
		}
	}

	private void sendPacket(final Player player, final Object packet) 
	{
		try
		{
			sendPacket.invoke(getPlayerConnection.get(getEntityHandle.invoke(player)), packet);
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
