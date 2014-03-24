package de.codebucket.particlelib.packet;

public final class PacketInstantiationException extends RuntimeException
{
	private static final long serialVersionUID = 7032503381633456747L;

	public PacketInstantiationException(String message) 
	{
		super(message);
	}

	public PacketInstantiationException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
