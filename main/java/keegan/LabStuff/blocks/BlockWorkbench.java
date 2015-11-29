package keegan.labstuff.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockWorkbench extends Block{

	public BlockWorkbench(Material p_i45394_1_) {
		super(p_i45394_1_);
	}
	
	private IIcon workbenchSide;
	private IIcon workbenchTop;
	private IIcon workbenchBottom;
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		this.workbenchBottom = this.blockIcon = register.registerIcon("labstuff:blockWorkbenchSide");
		this.workbenchBottom = register.registerIcon("labstuff:blockWorkbenchBottom");
		this.workbenchTop = register.registerIcon("labstuff:blockWorkbenchTop");
	}
	
	@Override
	public IIcon getIcon(int side, int metadata)
	{
		if(side == 0)
		{
			return this.workbenchTop;
		}
		else if(side == 1)
		{
			return this.workbenchBottom;
		}
		return this.workbenchSide;
	}

}
