package de.codebucket.particlelib;

public enum Particle 
{
	SMOKE("largesmoke", 0.2F, 20, "smoke", false), 
	RED_SMOKE("reddust", 0.0F, 40, "red_smoke", false), 
	RAINBOW_SMOKE("reddust", 1.0F, 100, "rainbow_smoke", false), 
	FIRE("flame", 0.05F, 100, "fire", false), 
	HEART("heart", 0.0F, 4, "heart", false), 
	MAGIC_RUNES("enchantmenttable", 1.0F, 100, "magic_runes", false), 
	LAVA_SPARK("lava", 0.0F, 4, "lava_spark", false), 
	SPLASH("splash", 1.0F, 40, "splash", false), 
	PORTAL("portal", 1.0F, 100, "portal", false), 

	CLOUD("explode", 0.1F, 10, "death_cloud", false), 
	CRITICAL("crit", 0.1F, 100, "critical", false), 
	MAGIC_CRITIAL("magicCrit", 0.1F, 100, "magic_critical", false), 
	MAGIC_CRITIAL_SMALL("magicCrit", 0.01F, 20, "magic_critical", false), 
	ANGRY_VILLAGER("angryVillager", 0.0F, 20, "angry_sparkle", false), 
	SPARKLE("happyVillager", 0.0F, 20, "sparkle", false), 
	WATER_DRIP("dripWater", 0.0F, 100, "water_drip", false), 
	LAVA_DRIP("dripLava", 0.0F, 100, "lava_drip", false), 
	WITCH_MAGIC("witchMagic", 1.0F, 20, "witch_magic", false), 
	WITCH_MAGIC_SMALL("witchMagic", 0.1F, 5, "witch_magic", false), 

	SNOWBALL("snowballpoof", 1.0F, 20, "snowball", false), 
	SNOW_SHOVEL("snowshovel", 0.02F, 30, "snow", false), 
	SLIME_SPLAT("slime", 1.0F, 30, "slime", false), 
	BUBBLE("bubble", 0.0F, 50, "bubble", false), 
	SPELL_AMBIENT("mobSpellAmbient", 1.0F, 100, "spellAmbient", false), 
	VOID("townaura", 1.0F, 100, "void", false);

	private String particleName;
	private float defaultSpeed;
	private int particleAmount;
	private String configName;
	public boolean requiresData;

	private Particle(String particleName, float defaultSpeed, int particleAmount, String configName, boolean requiresData)
	{
	    this.particleName = particleName;
	    this.defaultSpeed = defaultSpeed;
	    this.particleAmount = particleAmount;
	    this.configName = configName;
	    this.requiresData = requiresData;
	}
	
	public String getParticleName() 
	{
	    return this.particleName;
	}
	
	public int getParticleAmount() 
	{
	    return this.particleAmount;
	}
	
	public float getDefaultSpeed() 
	{
	    return this.defaultSpeed;
	}
	
	public String getConfigName() 
	{
	    return this.configName;
	}
	
	public Particle getParticleByConfigName(String s) 
	{
	    for (Particle sp : values()) 
	    {
	        if (sp.getConfigName().equalsIgnoreCase(s)) 
	        {
	        	return sp;
	        }
	    }
	    return null;
	}
}
