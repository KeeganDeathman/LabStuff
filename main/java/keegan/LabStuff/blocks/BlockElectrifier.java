package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityElectrifier;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public class BlockElectrifier extends Block implements ITileEntityProvider
{

	
	public BlockElectrifier(Material p_i45394_1_) 
	{
		super(p_i45394_1_);
		this.setDefaultState(getDefaultState().withProperty(WATERED, false));
	}


	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(FACING).getIndex() + 1) * ((state.getValue(WATERED) ? 2 : 1));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		if(meta < 6)
			return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta-1)).withProperty(WATERED, false);
		else
			return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta/2-1)).withProperty(WATERED, true);
	}
	
	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityElectrifier();
	}
	
	//It's not an opaque cube, so you need this.
    @Override
    public boolean isOpaqueCube(IBlockState state) 
    {
            return false;
    }
    
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final PropertyBool WATERED = PropertyBool.create("watered");
    
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[]{FACING, WATERED});
	}
	
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
	
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack)
    {
    	int l = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
    	world.setBlockState(pos, getDefaultState().withProperty(FACING, EnumFacing.fromAngle(90*l)).withProperty(WATERED, false));    
    }
    
    public boolean shouldSideBeRendered(IBlockAccess access, int i, int j, int k, int l)
	{
		return false;
	}
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing facing, float fx, float par8, float par9) 
    {
    	System.out.println("Im awake!");
    	if(!world.isRemote)
    	{
    		player.openGui(LabStuffMain.instance, 4, world, pos.getX(), pos.getY(), pos.getZ());
    		return true;
    	}
    	return false;
    }

}
