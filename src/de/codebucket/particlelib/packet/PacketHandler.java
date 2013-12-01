package de.codebucket.particlelib.packet;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import de.codebucket.particlelib.Main;
import de.codebucket.particlelib.Particle;

public class PacketHandler 
{
	Main plugin;
	
	public PacketHandler(Main plugin)
	{
		this.plugin = plugin;
	}
	
	public void sendWorldPacket(Player player, Location location, Particle particle) throws Exception
	{
		Object packet = java.lang.Class.forName("net.minecraft.server." + getVersionString() + ".Packet63WorldParticles").getConstructors()[0].newInstance(new Object[0]);
		
		setValue(packet, "a", particle.getParticleName());
		setValue(packet, "b", Float.valueOf((float)location.getX()));
		setValue(packet, "c", Float.valueOf((float)location.getY()));
		setValue(packet, "d", Float.valueOf((float)location.getZ()));
		setValue(packet, "e", Float.valueOf(new Random().nextFloat()));
		setValue(packet, "f", Float.valueOf(new Random().nextFloat()));
		setValue(packet, "g", Float.valueOf(new Random().nextFloat()));
		setValue(packet, "h", Float.valueOf(particle.getDefaultSpeed()));
		setValue(packet, "i", Integer.valueOf(particle.getParticleAmount()));
		
		sendPacket(player, packet);
	}
	
	public void sendFireworkPacket(World world, Location location, FireworkEffect effect) throws Exception 
	{
		Method world_getHandle = null;
		Method nms_world_broadcastEntityEffect = null;
		Method firework_getHandle = null;
		
		Firework fw = (Firework) world.spawn(location, Firework.class);
		Object nms_world = null;
		Object nms_firework = null;
		
		if(world_getHandle == null) 
		{
			world_getHandle = getMethod(world.getClass(), "getHandle");
			firework_getHandle = getMethod(fw.getClass(), "getHandle");
		}
		
		nms_world = world_getHandle.invoke(world, (Object[]) null);
		nms_firework = firework_getHandle.invoke(fw, (Object[]) null);
		
		if(nms_world_broadcastEntityEffect == null) 
		{
			nms_world_broadcastEntityEffect = getMethod(nms_world.getClass(), "broadcastEntityEffect");
		}
		
		FireworkMeta data = (FireworkMeta) fw.getFireworkMeta();
		data.clearEffects();
		data.setPower(1);
		data.addEffect(effect);
		fw.setFireworkMeta(data);
		
		nms_world_broadcastEntityEffect.invoke(nms_world, new Object[] {nms_firework, (byte) 17});
		
		fw.remove();
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

	public void sendPacket(Location l, Object packet) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException
	{
	    sendPacket(l, packet, 20);
	}

	public void sendPacket(Location l, Object packet, int radius) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException
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

	public void sendPacket(Player p, Object packet) throws InvocationTargetException, IllegalAccessException, NoSuchFieldException
	{
	    Object nmsPlayer = getMethod(p.getClass(), "getHandle").invoke(p, new Object[0]);
	    Object con = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
	    getMethod(con.getClass(), "sendPacket").invoke(con, new Object[] { packet });
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
