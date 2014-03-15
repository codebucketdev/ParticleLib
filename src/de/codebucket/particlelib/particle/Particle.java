package de.codebucket.particlelib.particle;

public enum Particle 
{
	SMOKE("largesmoke", 0.2F, 20), 
	REDSTONE_SMOKE("reddust", 0.0F, 40), 
	RAINBOW_SMOKE("reddust", 1.0F, 100), 
	FIRE("flame", 0.05F, 100), 
	HEART("heart",0.5F, 5), 
	NOTE("note", 0.5F, 1),
	ENCHANTMENT_TABLE("enchantmenttable", 1.0F, 100), 
	LAVA_SPARK("lava", 0.0F, 4), 
	SPLASH("splash", 1.0F, 40), 
	PORTAL("portal",1.0F, 100),
	EXPLODE("explode", 0.1F, 1), 
	LARGE_EXPLOSION("largeexplode", 0.1F, 1), 
	HUGE_EXPLOSION("hugeexplosion", 0.1F, 1), 
	CLOUD("explode", 0.1F, 10), 
	CRITICAL("crit", 0.1F, 80), 
	MAGIC_CRITICAL("magicCrit", 0.1F, 80), 
	ANGRY_VILLAGER("angryVillager", 0.0F, 10), 
	GREEN_SPARKLE("happyVillager", 0.0F, 20), 
	WATER_DRIP("dripWater", 0.0F, 100), 
	LAVA_DRIP("dripLava", 0.0F, 100), 
	WITCH_MAGIC("witchMagic", 1.0F, 20),
	SNOWBALL_HIT("snowballpoof", 1.0F, 20), 
	SNOW_SHOVEL("snowshovel", 0.02F, 30),
	SUSPEND("suspend", 1.0F, 30),
	DEPTH_SUSPEND("depthSuspend", 1.0F, 30),
	SLIME_SPLAT("slime", 1.0F, 30), 
	AIR_BUBBLE("bubble", 0.0F, 50), 
	MOB_SPELL("mobSpell", 1.0F, 100), 
	SPELL_AMBIENT("mobSpellAmbient", 1.0F, 100), 
	SPELL("spell", 1.0F, 100), 
	INSTANT_SPELL("instantSpell", 1.0F, 100), 
	TOWN_AURA("townaura", 1.0F, 100),
    FIREWORK_SPARK("fireworksSpark", 0.1F, 50),
    FOOTSTEP("footstep", 0.0F, 1),
    LARGE_SMOKE("largesmoke", 0.0F, 1);
	
	private String particleName;
	private float defaultSpeed;
	private int particleAmount;

	private Particle(String particleName, float defaultSpeed, int particleAmount) 
	{
		this.particleName = particleName;
		this.defaultSpeed = defaultSpeed;
		this.particleAmount = particleAmount;
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

	public Particle getParticleByName(String s) 
	{
		for (Particle sp : values())
		{
			if (sp.getParticleName().equalsIgnoreCase(s)) 
			{
				return sp;
			}
		}
		return null;
	}
}
