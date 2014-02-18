package de.codebucket.particlelib.packet;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;

import de.codebucket.particlelib.Main;
import de.codebucket.particlelib.Particle;
import de.codebucket.particlelib.ParticleRadius;

@SuppressWarnings("deprecation")
public class PacketHandler 
{
	Main plugin;
	
	public PacketHandler(Main plugin)
	{
		this.plugin = plugin;
	}
	
	public void sendWorldPacket(Player player, Location location, Particle particle) throws Exception
	{
		PacketContainer packet = new PacketContainer(Packets.Server.WORLD_PARTICLES);
		packet.getStrings().write(0, particle.getParticleName());
		packet.getFloat().write(0, Float.valueOf((float)location.getX()));
		packet.getFloat().write(1, Float.valueOf((float)location.getY()));
		packet.getFloat().write(2, Float.valueOf((float)location.getZ()));
		packet.getFloat().write(3, Float.valueOf(new Random().nextFloat()));
		packet.getFloat().write(4, Float.valueOf(new Random().nextFloat()));
		packet.getFloat().write(5, Float.valueOf(new Random().nextFloat()));
		packet.getFloat().write(6, Float.valueOf(particle.getDefaultSpeed()));
		packet.getIntegers().write(0, Integer.valueOf(particle.getParticleAmount()));
		sendPacket(player, packet);
	}
	
	public void sendCustomPacket(Player player, Location location, ParticleRadius radius, Particle particle) throws Exception
	{
		PacketContainer packet = new PacketContainer(Packets.Server.WORLD_PARTICLES);
		packet.getStrings().write(0, particle.getParticleName());
		packet.getFloat().write(0, Float.valueOf((float)location.getX()));
		packet.getFloat().write(1, Float.valueOf((float)location.getY()));
		packet.getFloat().write(2, Float.valueOf((float)location.getZ()));
		packet.getFloat().write(3, Float.valueOf(radius.getX()));
		packet.getFloat().write(4, Float.valueOf(radius.getY()));
		packet.getFloat().write(5, Float.valueOf(radius.getZ()));
		packet.getFloat().write(6, Float.valueOf(radius.getSpeed()));
		packet.getIntegers().write(0, Integer.valueOf(radius.getAmount()));
		sendPacket(player, packet);
	}
	
	public void sendFireworkPacket(World world, Location location, FireworkEffect effect) throws Exception 
	{
		final Firework fw = (Firework) world.spawn(location, Firework.class);
		FireworkMeta data = (FireworkMeta) fw.getFireworkMeta();
		data.addEffect(effect);
		fw.setFireworkMeta(data);	
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
		{
			@Override
			public void run()
			{
				fw.detonate();
			}
		}, 1L);
	}
	
	public Method getMethod(Class<?> cl, String method)
	{
	    for (Method m : cl.getMethods()) if (m.getName().equals(method)) return m;
	    return null;
	}

	public Field getField(Class<?> cl, String field) 
	{
	    for (Field f : cl.getFields()) if (f.getName().equals(field)) return f;
	    return null;
	}

	public String getVersionString() 
	{
	    String packageName = plugin.getServer().getClass().getPackage().getName();
	    String[] packageSplit = packageName.split("\\.");
	    String version = packageSplit[(packageSplit.length - 1)];
	    return version;
	}

	public void setValue(Object instance, String fieldName, Object value) throws Exception 
	{
	    Field field = instance.getClass().getDeclaredField(fieldName);
	    field.setAccessible(true);
	    field.set(instance, value);
	}

	public void sendPacket(Location l, PacketContainer packet) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException
	{
	    sendPacket(l, packet, 20);
	}

	public void sendPacket(Location l, PacketContainer packet, int radius) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException
	{
	    if (!getNearbyEntities(l, radius).isEmpty())
	    {
	    	for (Entity e : getNearbyEntities(l, 20))
	    	{
	    		if ((e != null) && ((e instanceof Player))) 
	    		{
	    			Player p = (Player)e;
	    			sendPacket(p, packet);
	    		}
	        }
	    }
	}

	public void sendPacket(Player p, PacketContainer packet) throws InvocationTargetException, IllegalAccessException, NoSuchFieldException
	{
		ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet);
	}

	public List<Entity> getNearbyEntities(Location l, int range) 
	{
	    List<Entity> entities = new ArrayList<>();
	    
	    for (Entity entity : l.getWorld().getEntities()) 
	    {
	    	if (isInBorder(l, entity.getLocation(), range)) 
	    	{
	    		entities.add(entity);
	    	}
	    }
	    
	    return entities;
	}

	public boolean isInBorder(Location center, Location l, int range)
	{
	    int x = center.getBlockX(); int z = center.getBlockZ();
	    int x1 = l.getBlockX(); int z1 = l.getBlockZ();
	    if ((x1 >= x + range) || (z1 >= z + range) || (x1 <= x - range) || (z1 <= z - range)) 
	    {
	    	return false;
	    }
	    
	    return true;
	}
}
