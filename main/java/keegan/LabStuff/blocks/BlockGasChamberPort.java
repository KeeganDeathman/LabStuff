package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityGasChamberPort;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockGasChamberPort extends Block implements ITileEntityProvider
{

	
	public boolean multiblock = false;
	public boolean visibility = true;
	private World world;
	private int xCoord;
	private int yCoord;
	private int zCoord;
	private int tileX;
	private int tileY;
	private int tileZ;
	private boolean coordsGiven = false;
	private Block[][][] multiblocks;
	
	public BlockGasChamberPort(Material p_i45394_1_) {
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}
	
	private IIcon side1;
	private IIcon side2;
	public boolean input = false;
	
	@Override
    // registerIcons
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        // blockIcon - blockIcon
        this.side1 = this.blockIcon = par1IconRegister.registerIcon("labstuff:gaschamberPort");
        this.side2 = par1IconRegister.registerIcon("labstuff:gaschamber9");
    }
	
	@Override
	public IIcon getIcon(int side, int metadata)
	{
		if(side == 0|| side == 1)
		{
			return this.side1;
		}
		return this.side2;
	}
	
	public void setInputState(boolean state)
	{
		input = state;
	}
	public boolean getInputState()
	{
		return input;
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par5, float par6, float par7, float par8)
    {
    	if(!world.isRemote)
    	{
    		player.openGui(LabStuffMain.instance, 5, world, x, y, z);
    		return true;
    	}
    	return false;
    }

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		//return new TileEntityGasChamberPort();
		return new TileEntityGasChamberPort();
	}

	public void setMultiBlockState(boolean state, Block[][][] array, int[] tileCoords, World worldObj)
	{
		multiblock = state;
		multiblocks = array;
		tileX = tileCoords[0];
		tileY = tileCoords[1];
		tileZ = tileCoords[2];
		this.world = worldObj;
		coordsGiven = true;
	}
	
	public boolean isMultiBlock()
	{
		return multiblock;
	}

}
