package keegan.labstuff.galaxies;


import java.util.Locale;

import keegan.labstuff.util.Vector3;
import net.minecraft.util.text.translation.I18n;

public class SolarSystem
{
    protected final String systemName;
    protected String unlocalizedName;
    protected Vector3 mapPosition = null;
    protected Star mainStar = null;
    protected String unlocalizedGalaxyName;

    public SolarSystem(String solarSystem, String parentGalaxy)
    {
        this.systemName = solarSystem.toLowerCase(Locale.ENGLISH);
        this.unlocalizedName = solarSystem;
        this.unlocalizedGalaxyName = parentGalaxy;
    }

    public String getName()
    {
        return this.systemName;
    }

    public final int getID()
    {
        return GalaxyRegistry.getSolarSystemID(this.systemName);
    }

    public String getLocalizedName()
    {
        String s = this.getUnlocalizedName();
        return s == null ? "" : net.minecraft.util.text.translation.I18n.translateToLocal(s);
    }

    public String getUnlocalizedName()
    {
        return "solarsystem." + this.unlocalizedName;
    }

    public Vector3 getMapPosition()
    {
        return this.mapPosition;
    }

    public SolarSystem setMapPosition(Vector3 mapPosition)
    {
    	mapPosition.scale(500D);
        this.mapPosition = mapPosition;
        return this;
    }

    public Star getMainStar()
    {
        return this.mainStar;
    }

    public SolarSystem setMainStar(Star star)
    {
        this.mainStar = star;
        return this;
    }

    public String getLocalizedParentGalaxyName()
    {
        String s = this.getUnlocalizedParentGalaxyName();
        return s == null ? "" : I18n.translateToLocal(s);
    }

    public String getUnlocalizedParentGalaxyName()
    {
        return "galaxy." + this.unlocalizedGalaxyName;
    }
}