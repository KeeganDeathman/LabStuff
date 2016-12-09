package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntitySolenoidAxel;
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

public class BlockSolenoidAxel extends Block implements ITileEntityProvider
{

	public BlockSolenoidAxel(Material p_i45394_1_)
	{
		super(p_i45394_1_);
		this.setDefaultState(getDefaultState().withProperty(RENDER, EnumRender.BLOCK));
		// TODO Auto-generated constructor stub
	}
	
	public static final PropertyEnum<EnumRender> RENDER = PropertyEnum.create("render", EnumRender.class);

	public static enum EnumRender implements IStringSerializable {
	BLOCK("block"), AIR("air"), SOLENOID("solenoid");

	private final String name;

	private EnumRender(String name) {
		this.name = name;
	}

	public String toString() {
		return this.name;
	}

	public static EnumRender fromString(String string) {
		switch (string) {
		case "block":
			return BLOCK;
		case "air":
			return AIR;
		case "solenoid":
			return SOLENOID;
		default:
			return BLOCK;
		}
	}

	public String getName() {
		return this.name;
	}
}


	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[]{RENDER});
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		// TODO Auto-generated method stub
		return new TileEntitySolenoidAxel();
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		switch(state.getValue(RENDER))
		{
			default:
				return 0;
			case BLOCK:
				return 0;
			case AIR:
				return 1;
			case SOLENOID:
				return 2;
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		switch(meta)
		{
			default:
				return getDefaultState().withProperty(RENDER, EnumRender.BLOCK);
			case 0:
				return getDefaultState().withProperty(RENDER, EnumRender.BLOCK);
			case 1:
				return getDefaultState().withProperty(RENDER, EnumRender.AIR);
			case 2:
				return getDefaultState().withProperty(RENDER, EnumRender.SOLENOID);
		}
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return true;
		
	}
	
	@Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing facing, float fx, float par8, float par9) {
    	if(!world.isRemote)
    	{
    		int xCoord = pos.getX();
    		int yCoord = pos.getY();
    		int zCoord = pos.getZ();
    		setBlock(xCoord+10,yCoord+1,zCoord-1,LabStuffMain.blockFusionToroidalMagnet, world);
			 setBlock(xCoord+10,yCoord+1,zCoord+1,LabStuffMain.blockFusionToroidalMagnet, world);
			 setBlock(xCoord-10, yCoord+1, zCoord-1,LabStuffMain.blockFusionToroidalMagnet, world);
			 setBlock(xCoord-10, yCoord+1, zCoord+1,LabStuffMain.blockFusionToroidalMagnet, world);
			 setBlock(xCoord+1, yCoord+1, zCoord-10,LabStuffMain.blockFusionToroidalMagnet, world);
			 setBlock(xCoord-1, yCoord+1, zCoord-10,LabStuffMain.blockFusionToroidalMagnet, world);
			 setBlock(xCoord+1, yCoord+1, zCoord+10,LabStuffMain.blockFusionToroidalMagnet, world);
			 setBlock(xCoord-1, yCoord+1, zCoord+10,LabStuffMain.blockFusionToroidalMagnet, world);
			 setBlock(xCoord+7, yCoord+1, zCoord+7,LabStuffMain.blockFusionToroidalMagnet, world);
			 setBlock(xCoord-7, yCoord+1, zCoord+7,LabStuffMain.blockFusionToroidalMagnet, world);
			 setBlock(xCoord+7, yCoord+1, zCoord-7,LabStuffMain.blockFusionToroidalMagnet, world);
			 setBlock(xCoord-7, yCoord+1, zCoord-7,LabStuffMain.blockFusionToroidalMagnet, world);
			 setBlock(xCoord+9, yCoord+1, zCoord+4,LabStuffMain.blockFusionToroidalMagnet, world);
			 setBlock(xCoord-9, yCoord+1, zCoord+4,LabStuffMain.blockFusionToroidalMagnet, world);
			 setBlock(xCoord+9, yCoord+1, zCoord-4,LabStuffMain.blockFusionToroidalMagnet, world);
			 setBlock(xCoord-9, yCoord+1, zCoord-4,LabStuffMain.blockFusionToroidalMagnet, world);
			 setBlock(xCoord+4, yCoord+1, zCoord+9,LabStuffMain.blockFusionToroidalMagnet, world);
			 setBlock(xCoord-4, yCoord+1, zCoord+9,LabStuffMain.blockFusionToroidalMagnet, world);
			 setBlock(xCoord+4, yCoord+1, zCoord-9,LabStuffMain.blockFusionToroidalMagnet, world);
			 setBlock(xCoord-4, yCoord+1, zCoord-9,LabStuffMain.blockFusionToroidalMagnet, world);
    		TileEntity tile = world.getTileEntity(pos.down(2));
    		if(tile instanceof TileEntitySolenoidAxel)
    		{
    			if(((TileEntitySolenoidAxel)tile).isMultiBlock() && ((TileEntitySolenoidAxel)tile).getEnergy() >= 250000)
    				player.addChatMessage(new TextComponentString("Spinning"));
    			else
    					player.addChatMessage(new TextComponentString("Still"));
    			return true;
    		}
    		return false;
    	}
    	return false;
    }
	
	private void setBlock(int i, int j, int k, Block block, World world) {
		world.setBlockState(new BlockPos(i,j,k), block.getDefaultState());
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
		return !(state.getValue(RENDER).equals(EnumRender.AIR) || state.getValue(RENDER).equals(EnumRender.SOLENOID));
	}
	
	

}
