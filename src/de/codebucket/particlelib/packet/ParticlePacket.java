package de.codebucket.particlelib.packet;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.codebucket.particlelib.ParticleLibary;
import de.codebucket.particlelib.packet.ReflectionHandler.PackageType;
import de.codebucket.particlelib.packet.ReflectionHandler.PacketType;
import de.codebucket.particlelib.packet.ReflectionHandler.SubPackageType;
import de.codebucket.particlelib.particle.Particle;
import de.codebucket.particlelib.particle.ParticleRadius;

public class ParticlePacket 
{
	ParticleLibary plugin;
	private Constructor<?> packetPlayOutWorldParticles;
	private Method getHandle;
	private Field playerConnection;
	private Method sendPacket;
	
	public ParticlePacket(ParticleLibary plugin)
	{
		try 
		{
			this.plugin = plugin;
			packetPlayOutWorldParticles = ReflectionHandler.getConstructor(PacketType.PLAY_OUT_WORLD_PARTICLES.getPacket(), String.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class);
			getHandle = ReflectionHandler.getMethod("CraftPlayer", SubPackageType.ENTITY, "getHandle");
			playerConnection = ReflectionHandler.getField("EntityPlayer", PackageType.MINECRAFT_SERVER, "playerConnection");
			sendPacket = ReflectionHandler.getMethod(playerConnection.getType(), "sendPacket", ReflectionHandler.getClass("Packet", PackageType.MINECRAFT_SERVER));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private Object createParticlePacket(String name, Location loc, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	{
		if (amount < 1)
			throw new PacketInstantiationException("Amount cannot be lower than 1");
		
		try
		{
			return packetPlayOutWorldParticles.newInstance(name, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), offsetX, offsetY, offsetZ, speed, amount);
		} 
		catch (Exception e)
		{
			throw new PacketInstantiationException("Packet instantiation failed", e);
		}
	}

	private Object createIconCrackPacket(int id, Location loc, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	{
		return createParticlePacket("iconcrack_" + id, loc, offsetX, offsetY, offsetZ, speed, amount);
	}
	
	private Object createBlockCrackPacket(int id, byte data, Location loc, float offsetX, float offsetY, float offsetZ, int amount)
	{
		return createParticlePacket("blockcrack_" + id + "_" + data, loc, offsetX, offsetY, offsetZ, 0, amount);
	}
	
	private Object createBlockDustPacket(int id, byte data, Location loc, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	{
		return createParticlePacket("blockdust_" + id + "_" + data, loc, offsetX, offsetY, offsetZ, speed, amount);
	}

	private void sendPacket(Player p, Object packet)
	{
		try
		{
			sendPacket.invoke(playerConnection.get(getHandle.invoke(p)), packet);
		}
		catch (Exception e) 
		{
			throw new PacketSendingException("Failed to send a packet to player '" + p.getName() + "'",  e);
		}
	}
	
	public void sendParticlePacket(Player p, Location loc, Particle particle) 
	{
		float offsetX = new Random().nextFloat();
		float offsetY = new Random().nextFloat();
		float offsetZ = new Random().nextFloat();
		float speed = particle.getDefaultSpeed();
		int amount = particle.getParticleAmount();
		sendPacket(p, createParticlePacket(particle.getParticleName(), loc, offsetX, offsetY, offsetZ, speed, amount));
	}

	public void sendParticlePacket(Player p, Location loc, ParticleRadius radius, Particle particle) throws Exception
	{
		float offsetX = radius.getX();
		float offsetY = radius.getY();
		float offsetZ = radius.getZ();
		float speed = radius.getSpeed();
		int amount = radius.getAmount();
		sendPacket(p, createParticlePacket(particle.getParticleName(), loc, offsetX, offsetY, offsetZ, speed, amount));
	}
	
	public void sendIconCrackPacket(Player p, Location loc, int id, ParticleRadius radius)
	{
		float offsetX = radius.getX();
		float offsetY = radius.getY();
		float offsetZ = radius.getZ();
		float speed = radius.getSpeed();
		int amount = radius.getAmount();
		sendPacket(p, createIconCrackPacket(id, loc, offsetX, offsetY, offsetZ, speed, amount));
	}
	
	public void sendBlockCrackPacket(Player p, Location loc, int id, byte data, ParticleRadius radius) 
	{
		float offsetX = radius.getX();
		float offsetY = radius.getY();
		float offsetZ = radius.getZ();
		int amount = radius.getAmount();
		sendPacket(p, createBlockCrackPacket(id, data, loc, offsetX, offsetY, offsetZ, amount));
	}
	
	public void sendBlockDustPacket(Player p, Location loc, int id, byte data, ParticleRadius radius)
	{
		float offsetX = radius.getX();
		float offsetY = radius.getY();
		float offsetZ = radius.getZ();
		float speed = radius.getSpeed();
		int amount = radius.getAmount();
		sendPacket(p, createBlockDustPacket(id, data, loc, offsetX, offsetY, offsetZ, speed, amount));
	}
	
	private static final class PacketInstantiationException extends RuntimeException
	{
		private static final long serialVersionUID = 3203085387160737484L;

		public PacketInstantiationException(String message) 
		{
			super(message);
		}

		public PacketInstantiationException(String message, Throwable cause)
		{
			super(message, cause);
		}
	}
	
	private static final class PacketSendingException extends RuntimeException
	{
		private static final long serialVersionUID = 3203085387160737484L;
		
		public PacketSendingException(String message, Throwable cause)
		{
			super(message, cause);
		}
	}
}
