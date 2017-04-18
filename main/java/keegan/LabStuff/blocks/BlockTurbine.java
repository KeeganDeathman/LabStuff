package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.network.PipeUtils;
import keegan.labstuff.tileentity.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.*;
import net.minecraftforge.fluids.FluidStack;

public class BlockTurbine extends Block implements ITileEntityProvider
{
	
	public static final PropertyInteger TURBINES = PropertyInteger.create("turbines", 0, 2);
	public static final PropertyBool CONNECTED_DOWN = PropertyBool.create("connected_down");
    public static final PropertyBool CONNECTED_UP = PropertyBool.create("connected_up");
    public static final PropertyBool CONNECTED_NORTH = PropertyBool.create("connected_north");
    public static final PropertyBool CONNECTED_SOUTH = PropertyBool.create("connected_south");
    public static final PropertyBool CONNECTED_WEST = PropertyBool.create("connected_west");
    public static final PropertyBool CONNECTED_EAST = PropertyBool.create("connected_east");
    
	public BlockTurbine(Material p_i45394_1_) {
		super(p_i45394_1_);
		if(this.equals(LabStuffMain.blockTurbineCasing) || this.equals(LabStuffMain.blockTurbineGlass) || this.equals(LabStuffMain.blockElectromagneticCoil))
			this.setDefaultState(this.blockState.getBaseState().withProperty(CONNECTED_DOWN, Boolean.FALSE).withProperty(CONNECTED_EAST, Boolean.FALSE).withProperty(CONNECTED_NORTH, Boolean.FALSE).withProperty(CONNECTED_SOUTH, Boolean.FALSE).withProperty(CONNECTED_UP, Boolean.FALSE).withProperty(CONNECTED_WEST, Boolean.FALSE));
        if(this.equals(LabStuffMain.blockTurbineRotor))
        	this.setDefaultState(this.blockState.getBaseState().withProperty(TURBINES, 0));
	    }
	     
	    @Override
	    public IBlockState getActualState (IBlockState state, IBlockAccess world, BlockPos position) {
	        
	    	if(this.equals(LabStuffMain.blockTurbineCasing) || this.equals(LabStuffMain.blockTurbineGlass) || this.equals(LabStuffMain.blockElectromagneticCoil))
	    		return state.withProperty(CONNECTED_DOWN, this.isSideConnectable(world, position, EnumFacing.DOWN)).withProperty(CONNECTED_EAST, this.isSideConnectable(world, position, EnumFacing.EAST)).withProperty(CONNECTED_NORTH, this.isSideConnectable(world, position, EnumFacing.NORTH)).withProperty(CONNECTED_SOUTH, this.isSideConnectable(world, position, EnumFacing.SOUTH)).withProperty(CONNECTED_UP, this.isSideConnectable(world, position, EnumFacing.UP)).withProperty(CONNECTED_WEST, this.isSideConnectable(world, position, EnumFacing.WEST));
	    	return state;
	    }
	    
	    
	     
	    @Override
	    protected BlockStateContainer createBlockState () {
	         
		        return new BlockStateContainer(this, new IProperty[] {TURBINES, CONNECTED_DOWN, CONNECTED_UP, CONNECTED_NORTH, CONNECTED_SOUTH, CONNECTED_WEST, CONNECTED_EAST, TURBINES });
	    }
	 
	    @Override
	    public int getMetaFromState (IBlockState state) {
	        if(this.equals(LabStuffMain.blockTurbineRotor))
	        	return state.getValue(TURBINES);
	        return 0;
	    }
	     
	    /**
	     * Checks if a specific side of a block can connect to this block. For this example, a side
	     * is connectable if the block is the same block as this one.
	     * 
	     * @param world The world to run the check in.
	     * @param pos The position of the block to check for.
	     * @param side The side of the block to check.
	     * @return Whether or not the side is connectable.
	     */
	    private boolean isSideConnectable (IBlockAccess world, BlockPos pos, EnumFacing side) {
	         
	        final IBlockState state = world.getBlockState(pos.offset(side));
	        return (state == null) ? false : (state.getBlock() == this);
	    }
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		// TODO Auto-generated method stub
		if(this.equals(LabStuffMain.blockTurbineRotor))
			return new TileEntityTurbine();
		if(this.equals(LabStuffMain.blockTurbineValve))
			return new TileEntityTurbineValve();
		if(this.equals(LabStuffMain.blockTurbineVent))
			return new TileEntityTurbineVent();
		return null;
	}
	
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(TURBINES, meta);
	}
	
	public boolean validCasing()
	{
		return (this.equals(LabStuffMain.blockTurbineCasing) || this.equals(LabStuffMain.blockTurbineGlass) || this.equals(LabStuffMain.blockTurbineValve) || this.equals(LabStuffMain.blockTurbineVent));
	}
	
	@Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing facing, float fx, float par8, float par9) {
		System.out.println("Hello!");
		if(!world.isRemote)
		{
			if(held.getItem() != null && held.getItem().equals(LabStuffMain.itemTurbineBlades) && this.equals(LabStuffMain.blockTurbineRotor))
			{
				held.stackSize -=1;
				world.setBlockState(pos, world.getBlockState(pos).withProperty(TURBINES, world.getBlockState(pos).getValue(TURBINES)+1));
				return true;
			}
			else if(this.equals(LabStuffMain.blockTurbineValve))
			{
				TileEntityTurbineValve tile = (TileEntityTurbineValve)world.getTileEntity(pos);
				FluidStack buffer = tile.getTankInfo(null)[0].fluid;
				String fluid = 	buffer != null ? PipeUtils.localizeFluidStack(buffer) + " (" + buffer.amount + " mB)" : "None";
				player.addChatComponentMessage(new TextComponentString("Holding " + tile.getEnergy() + "RF and " + fluid));
			}
		}
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		if(this.equals(LabStuffMain.blockTurbineGlass) || this.equals(LabStuffMain.blockTurbineRotor) || this.equals(LabStuffMain.blockTurbineVent))
			return false;
		return true;
	}
	
	 public boolean renderAsNormalBlock()
	 {
		 return false;
	 }
	 
	 public BlockRenderLayer getBlockLayer()
	 {
		 return BlockRenderLayer.CUTOUT;
	 }
	
	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side)
	{
		if(this.equals(LabStuffMain.blockTurbineRotor))
			return false;
		if(access.getBlockState(pos.offset(side)).getBlock().equals(LabStuffMain.blockTurbineGlass) && this.equals(LabStuffMain.blockTurbineGlass))
			return false;
		return true;
	}

}
