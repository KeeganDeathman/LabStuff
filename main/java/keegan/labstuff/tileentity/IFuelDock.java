package keegan.labstuff.tileentity;

import java.util.HashSet;

import keegan.labstuff.entities.IDockable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IFuelDock
{
    public HashSet<ILandingPadAttachable> getConnectedTiles();

    public boolean isBlockAttachable(IBlockAccess world, BlockPos pos);

    public IDockable getDockedEntity();

	void dockEntity(IDockable entity);
}