package keegan.labstuff.tileentity;

import java.util.*;

import keegan.labstuff.blocks.BlockAdvanced;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityMulti extends TileEntity
{
    //NOTE: No need for networking in 1.8+: see comment in initialiseMultiTiles()
    
    // The the position of the main block
    public BlockPos mainBlockPosition;

    public TileEntityMulti()
    {

    }

    public TileEntityMulti(BlockPos mainBlockPosition)
    {
        this.mainBlockPosition = mainBlockPosition;
    }

    //    public void setMainBlock(BlockPos mainBlock)
//    {
//        this.mainBlockPosition = mainBlock;
//
//        if (!this.worldObj.isRemote)
//        {
//            this.worldObj.notifyBlockUpdate(this.getPos());
//        }
//    }

    public void onBlockRemoval()
    {
        if (this.mainBlockPosition != null)
        {
            TileEntity tileEntity = this.worldObj.getTileEntity(this.mainBlockPosition);

            if (tileEntity instanceof IMultiBlock)
            {
                IMultiBlock mainBlock = (IMultiBlock) tileEntity;
                mainBlock.onDestroy(this);
            }
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, EntityPlayer player)
    {
        if (this.mainBlockPosition != null)
        {
            TileEntity tileEntity = this.worldObj.getTileEntity(this.mainBlockPosition);

            if (tileEntity instanceof IMultiBlock)
            {
                return ((IMultiBlock) tileEntity).onActivated(player);
            }
        }

        return false;
    }

    public boolean onBlockWrenched(World world, BlockPos pos, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (this.mainBlockPosition != null)
        {
            IBlockState state = this.worldObj.getBlockState(this.mainBlockPosition);

            if (state.getBlock() instanceof BlockAdvanced)
            {
                return ((BlockAdvanced) state.getBlock()).onBlockActivated(world, this.mainBlockPosition, state, entityPlayer, hand, heldItem, side, hitX, hitY, hitZ);
            }
        }

        return false;
    }

    public TileEntity getMainBlockTile()
    {
        if (this.mainBlockPosition != null)
        {
            return this.worldObj.getTileEntity(this.mainBlockPosition);
        }

        return null;
    }

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        NBTTagCompound tag = nbt.getCompoundTag("mainBlockPosition");
        this.mainBlockPosition = new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        if (this.mainBlockPosition != null)
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("x", this.mainBlockPosition.getX());
            tag.setInteger("y", this.mainBlockPosition.getY());
            tag.setInteger("z", this.mainBlockPosition.getZ());
            nbt.setTag("mainBlockPosition", tag);
        }

        return nbt;
    }

    protected boolean initialiseMultiTiles(BlockPos pos, World world)
    {
        IMultiBlock thisTile = (IMultiBlock)this;
        
        //Client can create its own fake blocks and tiles - no need for networking in 1.8+
        if (world.isRemote) thisTile.onCreate(world, pos);
        
        List<BlockPos> positions = new ArrayList();
        thisTile.getPositions(pos, positions);
        boolean result = true;
        for (BlockPos vecToAdd : positions)
        {
            TileEntity tile = world.getTileEntity(vecToAdd);
            if (tile instanceof TileEntityMulti)
            {
                ((TileEntityMulti) tile).mainBlockPosition = pos;
            }
            else
            {
                result = false;
            }
        }
        return result;
    }
}