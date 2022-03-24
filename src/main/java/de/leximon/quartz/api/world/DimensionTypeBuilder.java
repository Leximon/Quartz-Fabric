package de.leximon.quartz.api.world;

import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

import javax.annotation.CheckReturnValue;
import java.util.OptionalLong;

/**
 * unfinished and will probably never finish
 */
@Deprecated
public class DimensionTypeBuilder {

    private OptionalLong fixedTime;
    private boolean hasSkyLight;
    private boolean hasCeiling;
    private boolean ultrawarm;
    private boolean natural;
    private double coordinateScale;
    private boolean hasEnderDragonFight;
    private boolean piglinSafe;
    private boolean bedWorks;
    private boolean respawnAnchorWorks;
    private boolean hasRaids;
    private int minimumY;
    private int height;
    private int logicalHeight;
    private TagKey<Block> infiniburn;
    private Identifier effects;
    private float ambientLight;

    private DimensionTypeBuilder(RegistryKey<DimensionType> preset) {
    }

    @CheckReturnValue
    public DimensionTypeBuilder fixedTime(OptionalLong fixedTime) { this.fixedTime = fixedTime; return this; }

    public DimensionTypeBuilder hasSkyLight(boolean hasSkyLight) { this.hasSkyLight = hasSkyLight; return this; }

    public DimensionTypeBuilder hasCeiling(boolean hasCeiling) { this.hasCeiling = hasCeiling; return this; }

    public DimensionTypeBuilder ultrawarm(boolean ultrawarm) { this.ultrawarm = ultrawarm; return this; }

    public DimensionTypeBuilder natural(boolean natural) { this.natural = natural; return this; }

    public DimensionTypeBuilder coordinateScale(double coordinateScale) { this.coordinateScale = coordinateScale; return this; }

    public DimensionTypeBuilder hasEnderDragonFight(boolean hasEnderDragonFight) { this.hasEnderDragonFight = hasEnderDragonFight; return this; }

    public DimensionTypeBuilder piglinSafe(boolean piglinSafe) { this.piglinSafe = piglinSafe; return this; }

    public DimensionTypeBuilder bedWorks(boolean bedWorks) { this.bedWorks = bedWorks; return this; }

    public DimensionTypeBuilder respawnAnchorWorks(boolean respawnAnchorWorks) { this.respawnAnchorWorks = respawnAnchorWorks; return this; }

    public DimensionTypeBuilder hasRaids(boolean hasRaids) { this.hasRaids = hasRaids; return this; }

    public DimensionTypeBuilder minimumY(int minimumY) { this.minimumY = minimumY; return this; }

    public DimensionTypeBuilder height(int height) { this.height = height; return this; }

    public DimensionTypeBuilder logicalHeight(int logicalHeight) { this.logicalHeight = logicalHeight; return this; }

    public DimensionTypeBuilder infiniburn(TagKey<Block> infiniburn) { this.infiniburn = infiniburn; return this; }

    public DimensionTypeBuilder effects(Identifier effects) { this.effects = effects; return this; }

    public DimensionTypeBuilder ambientLight(float ambientLight) { this.ambientLight = ambientLight; return this; }

    public DimensionType create() {
        return DimensionType.create(fixedTime, hasSkyLight, hasCeiling, ultrawarm, natural, coordinateScale, hasEnderDragonFight, piglinSafe, bedWorks, respawnAnchorWorks, hasRaids, minimumY, height, logicalHeight, infiniburn, effects, ambientLight);
    }
}
