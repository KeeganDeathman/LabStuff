package keegan.labstuff.tileentity;

import java.util.*;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.blocks.BlockMulti;
import keegan.labstuff.blocks.BlockMulti.EnumBlockMultiType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntitySpaceStationBase extends TileEntityMulti implements IMultiBlock
{
    public TileEntitySpaceStationBase()
    {
        super(null);
    }

    public String ownerUsername = "bobby";
    private boolean initialised;

    @Override
    public void update()
    {
        if (!this.initialised)
        {
            this.initialised = this.initialiseMultiTiles(this.getPos(), this.worldObj);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.ownerUsername = nbt.getString("ownerUsername");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setString("ownerUsername", this.ownerUsername);
        return nbt;
    }

    public void setOwner(String username)
    {
        this.ownerUsername = username;
    }

    public String getOwner()
    {
        return this.ownerUsername;
    }

    @Override
    public boolean onActivated(EntityPlayer entityPlayer)
    {
        return false;
    }

    @Override
    public void onCreate(World world, BlockPos placedPosition)
    {
        this.mainBlockPosition = placedPosition;
        this.markDirty();

        List<BlockPos> positions = new LinkedList();
        this.getPositions(placedPosition, positions);
        ((BlockMulti) LabStuffMain.fakeBlock).makeFakeBlock(world, positions, placedPosition, EnumBlockMultiType.SPACE_STATION_BASE);
    }

    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        int buildHeight = this.worldObj.getHeight() - 1;

        for (int y = 1; y < 3; y++)
        {
            if (placedPosition.getY() + y > buildHeight)
            {
                return;
            }
            positions.add(new BlockPos(placedPosition.getX(), placedPosition.getY() + y, placedPosition.getZ()));
        }
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        final BlockPos thisBlock = getPos();
        List<BlockPos> positions = new LinkedList();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            IBlockState stateAt = this.worldObj.getBlockState(pos);

            if (stateAt.getBlock() == LabStuffMain.fakeBlock && (EnumBlockMultiType) stateAt.getValue(BlockMulti.MULTI_TYPE) == EnumBlockMultiType.SPACE_STATION_BASE)
            {
                this.worldObj.setBlockToAir(pos);
            }
        }
        this.worldObj.destroyBlock(this.getPos(), false);
    }
}