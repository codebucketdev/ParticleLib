package de.codebucket.particlelib.packet;

public final class PacketSendingException extends RuntimeException
{
	private static final long serialVersionUID = -4683406761572504271L;

	public PacketSendingException(String message, Throwable cause)
	{
		super(message, cause);
	}
}