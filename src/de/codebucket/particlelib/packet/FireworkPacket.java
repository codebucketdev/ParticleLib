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

	public void sendFireworkPacket(Player p, Location loc, FireworkEffect fe) 
	{
		Object packet = createPacket(loc, fe);
		sendPacket(packet, p);
	}

	private Object createPacket(Location loc, FireworkEffect fe) 
	{
		try 
		{
			Firework firework = loc.getWorld().spawn(loc, Firework.class);
			FireworkMeta data = (FireworkMeta) firework.getFireworkMeta();
			data.clearEffects();
			data.setPower(1);
			data.addEffect(fe);
			firework.setFireworkMeta(data);
			Object nms_firework = null;
			nms_firework = getFireworkHandle.invoke(firework);
			firework.remove();
			return packetPlayOutEntityStatus.newInstance(nms_firework, (byte) 17);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private void sendPacket(Object packet, Player player) 
	{
		try 
		{
			Object nms_player = getEntityHandle.invoke(player);
			Object nms_connection = getPlayerConnection.get(nms_player);
			sendPacket.invoke(nms_connection, packet);
			System.out.println(player.getName() + " recieved packet.");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
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
