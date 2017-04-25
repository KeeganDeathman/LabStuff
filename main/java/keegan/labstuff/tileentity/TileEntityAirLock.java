package keegan.labstuff.tileentity;

public class TileEntityAirLock extends TileEntityAdvanced
{
    @Override
    public void update()
    {
        super.update();
    }

    @Override
    public double getPacketRange()
    {
        return 0;
    }

    @Override
    public int getPacketCooldown()
    {
        return 0;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return false;
    }
}