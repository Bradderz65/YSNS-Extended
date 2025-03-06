package elocindev.ysns.api;

public class BiomeSpawnHolder {
    private String entityId;
    private String biome;
    private float spawn_chance;
    
    public BiomeSpawnHolder(String entityId, String biome, float spawn_chance) {
        this.entityId = entityId;
        this.biome = biome;
        this.spawn_chance = spawn_chance;
    }

    public String getEntityIdentifier() {
        return entityId;
    }

    public String getBiome() {
        return biome;
    }

    public float getSpawnChance() {
        return spawn_chance;
    }
} 