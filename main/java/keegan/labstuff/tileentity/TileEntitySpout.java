package keegan.labstuff.tileentity;

import java.util.Random;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.util.Vector3;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntitySpout extends TileEntity implements ITickable
{
    private final Random rand = new Random(System.currentTimeMillis());

    @Override
    public void update()
    {
        if (this.worldObj.isRemote)
        {
            if (rand.nextInt(400) == 0)
            {
                IBlockState stateAbove = this.worldObj.getBlockState(this.getPos().up());
                if (stateAbove.getBlock().isAir(this.worldObj.getBlockState(this.getPos().up()), this.worldObj, this.getPos().up()))
                {
                    double posX = (double)pos.getX() + 0.45 + rand.nextDouble() * 0.1;
                    double posY = (double)pos.getY() + 1.0;
                    double posZ = (double)pos.getZ() + 0.45 + rand.nextDouble() * 0.1;
                    for (int i = 0; i < 4 + rand.nextInt(4); ++i)
                    {
                        LabStuffMain.spawnParticle("acidVapor", new Vector3(posX, posY, posZ), new Vector3(rand.nextDouble() * 0.5 - 0.25, rand.nextDouble() * 0.5 + 0.5, rand.nextDouble() * 0.5 - 0.25));
                    }
                }
            }
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }
}