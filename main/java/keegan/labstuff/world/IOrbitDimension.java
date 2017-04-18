package keegan.labstuff.world;

import keegan.labstuff.world.ILabstuffWorldProvider;

public interface IOrbitDimension extends ILabstuffWorldProvider
{
    /**
     * @return the name of the world that this dimension is orbiting. For the
     * overworld it returns "Overworld"
     */
    public String getPlanetToOrbit();

    /**
     * @return the y-coordinate that entities will fall back into the world we
     * are orbiting
     */
    public int getYCoordToTeleportToPlanet();
}