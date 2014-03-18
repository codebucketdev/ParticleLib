package de.codebucket.particlelib.packet;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
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

	public void sendFireworkPacket(final Player p, Location loc, FireworkEffect fe) 
	{
		final Firework firework = createFirework(loc, fe);
		final Object packet = createPacket(firework);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
		{
			@Override
			public void run() 
			{
				sendPacket(packet, p);
				firework.remove();
			}
		}, 2L);		
	}
	
	private Firework createFirework(Location loc, FireworkEffect fe)
	{
		Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		FireworkMeta data = (FireworkMeta) firework.getFireworkMeta();
		data.clearEffects();
		data.setPower(1);
		data.addEffect(fe);
		firework.setFireworkMeta(data);
		return firework;
	}

	private Object createPacket(Firework firework) 
	{
		 try
		 {
			 Object nms_firework = null;
			 nms_firework = getFireworkHandle.invoke(firework);
			 return packetPlayOutEntityStatus.newInstance(nms_firework, (byte) 17);
		 } 
		 catch (Exception e) 
		 {
			 return createPacket(firework);
		 }
	}

	private void sendPacket(final Object packet, Player player) 
	{
		try 
		{
			Object nms_player = getEntityHandle.invoke(player);
			Object nms_connection = getPlayerConnection.get(nms_player);
			sendPacket.invoke(nms_connection, packet);
		} 
		catch (Exception e) 
		{
			sendPacket(packet, player);
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
