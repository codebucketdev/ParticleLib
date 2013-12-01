package de.codebucket.particlelib.packet;

import java.lang.reflect.Method;
import java.util.Random;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import de.codebucket.particlelib.packet.NMSPacket;
import de.codebucket.particlelib.Main;
import de.codebucket.particlelib.Particle;

public class PacketHandler 
{
	Main plugin;
	private String version = "";
	
	public PacketHandler(Main plugin)
	{
		this.plugin = plugin;
		this.version = plugin.getServerVersion();
	}
	
	public void sendWorldPacket(Player player, Location location, Particle particle) throws Exception
	{
		NMSPacket packet = null;
		
		try 
		{
			packet = new NMSPacket("Packet63WorldParticles");
		} 
		catch (InstantiationException e) 
		{
			plugin.getLogger().severe("Unknown or unsupported CraftBukkit version! Is the Plugin up to date?");
			plugin.getLogger().severe(e.getMessage());
		} 
		catch (IllegalAccessException e) 
		{
			plugin.getLogger().severe("Unknown or unsupported CraftBukkit version! Is the Plugin up to date?");
			plugin.getLogger().severe(e.getMessage());
		} 
		catch (ClassNotFoundException e) 
		{
			plugin.getLogger().severe("Unknown or unsupported CraftBukkit version! Is the Plugin up to date?");
			plugin.getLogger().severe(e.getMessage());
		}
		
		packet.setDeclaredField("a", particle.getParticleName());
		packet.setDeclaredField("b", (float) location.getX());
		packet.setDeclaredField("c", (float) location.getY());
		packet.setDeclaredField("d", (float) location.getZ());
		packet.setDeclaredField("e", new Random().nextFloat());
		packet.setDeclaredField("f", new Random().nextFloat());
		packet.setDeclaredField("g", new Random().nextFloat());
		packet.setDeclaredField("h", particle.getDefaultSpeed());
		packet.setDeclaredField("i", particle.getParticleAmount());
		
		sendPacket(player, packet);
	}
	
	public void sendPacket(Player player, Object o)
    {
	    try 
	    {
		    Class<?> packet = Class.forName("net.minecraft.server." + version + ".Packet");
		    Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
		    
		    if(!packet.isAssignableFrom(o.getClass()))
		    {
			    throw new IllegalArgumentException("Object o wasn't a packet!");
		    }
		    
		    Object cp = craftPlayer.cast(player);
		    Object handle = craftPlayer.getMethod("getHandle").invoke(cp);
		    Object con = handle.getClass().getField("playerConnection").get(handle);
		    con.getClass().getMethod("sendPacket", packet).invoke(con, o);
        } 
	    catch (Exception e)
	    {
		    plugin.getLogger().severe("An error has occurred whilst sending the packets. Is Bukkit up to date?");
		    plugin.getLogger().severe(e.getMessage());
	    }
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
	
	private static Method getMethod(Class<?> cl, String method)
	{
		for(Method m : cl.getMethods()) 
		{
			if(m.getName().equals(method)) 
			{
				return m;
			}
		}
		return null;
	}
}
