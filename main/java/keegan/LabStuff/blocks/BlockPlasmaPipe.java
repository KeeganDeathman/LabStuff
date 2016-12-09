package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.TileEntityPlasmaPipe;
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

public class BlockPlasmaPipe extends Block implements ITileEntityProvider {

	public BlockPlasmaPipe(Material p_i45394_1_) {
		super(p_i45394_1_);
		this.setDefaultState(getDefaultState().withProperty(PLASMA, false));
	}
	
	public static final PropertyBool PLASMA = PropertyBool.create("plasma");

	
	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		// TODO Auto-generated method stub
		return new TileEntityPlasmaPipe();
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(PLASMA) ? 1 : 0);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(PLASMA, meta > 0);
	}
	
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[]{PLASMA});
	}
	
	@Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing facing, float fx, float par8, float par9) {
    	if(!world.isRemote)
    	{
    		TileEntity tile = world.getTileEntity(pos);
    		if(tile instanceof TileEntityPlasmaPipe)
    		{
    			player.addChatMessage(new TextComponentString("Network is holding " + ((TileEntityPlasmaPipe)tile).getPlasma()));
    			return true;
    		}
    		return false;
    	}
    	return false;
    }
	
	@Override
    public boolean isOpaqueCube(IBlockState state) 
    {
            return false;
    }
    
	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
		return false;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}


}
