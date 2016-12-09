package keegan.labstuff.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.fluids.*;

public class BlockSteam extends BlockFluidClassic 
{
    
    public BlockSteam(Fluid fluid, Material material) {
            super(fluid, material);
    }
    
    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos pos) {
            if (world.getBlockState(pos).getMaterial().isLiquid()) return false;
            return super.canDisplace(world, pos);
    }
    
    @Override
    public boolean displaceIfPossible(World world, BlockPos pos) 
    {
            if (world.getBlockState(pos).getMaterial().isLiquid()) return false;
            return super.displaceIfPossible(world, pos);
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
    	return EnumBlockRenderType.LIQUID;
    }
}

 