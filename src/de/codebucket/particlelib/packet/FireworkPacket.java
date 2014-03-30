package de.codebucket.particlelib.packet;

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
	private Class<?> packetPlayOutEntityDestroy;
	private Method getPlayerHandle;
	private Method getFireworkHandle;
	private Field getPlayerConnection;
	private Method sendPacket;

	public FireworkPacket(ParticleLibary plugin)
	{
		try 
		{
			this.plugin = plugin;
			packetPlayOutEntityDestroy = getMCClass("PacketPlayOutEntityDestroy");
			getPlayerHandle = getCraftClass("entity.CraftPlayer").getMethod("getHandle");
			getFireworkHandle = getCraftClass("entity.CraftFirework").getMethod("getHandle");
			getPlayerConnection = getMCClass("EntityPlayer").getDeclaredField("playerConnection");
			sendPacket = getMCClass("PlayerConnection").getMethod("sendPacket", getMCClass("Packet"));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public void sendFireworkPacket(Player player, Location loc, FireworkEffect fe)
	{
		try
		{
			Firework fw = loc.getWorld().spawn(loc, Firework.class);
			FireworkMeta data = (FireworkMeta) fw.getFireworkMeta();
			data.clearEffects();
			data.addEffect(fe);
			data.setPower(0);
			fw.setFireworkMeta(data);
			
			Object dpacket = packetPlayOutEntityDestroy.newInstance();
			Field a = packetPlayOutEntityDestroy.getDeclaredField("a");
			a.setAccessible(true);
			a.set(dpacket, new int[] { fw.getEntityId() });
			for(Player pl : fw.getWorld().getPlayers())
			{
				if(!pl.equals(player)) sendPacket(pl, dpacket);
			}
			detonateFirework(fw);
		}
		catch (Exception e) 
		{
			throw new PacketInstantiationException("Packet instantiation failed", e);
		}
	}
	
	public void detonateFirework(Firework fw)
	{
		try
		{
			Object nms_firework = getFireworkHandle.invoke(fw, (Object[]) null);
			Field a = nms_firework.getClass().getDeclaredField("ticksFlown");
			a.setAccessible(true);
			a.set(nms_firework, Integer.parseInt("3"));
			Field b = nms_firework.getClass().getDeclaredField("expectedLifespan");
			b.setAccessible(true);
			b.set(nms_firework, Integer.parseInt("-1"));
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private void sendPacket(final Player player, final Object packet) 
	{
		try
		{
			sendPacket.invoke(getPlayerConnection.get(getPlayerHandle.invoke(player)), packet);
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
