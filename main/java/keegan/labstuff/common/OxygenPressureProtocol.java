package keegan.labstuff.common;

import java.util.*;

import keegan.labstuff.blocks.IPartialSealableBlock;
import keegan.labstuff.config.ConfigManagerCore;
import keegan.labstuff.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OxygenPressureProtocol
{
    public final static Map<Block, ArrayList<Integer>> nonPermeableBlocks = new HashMap<Block, ArrayList<Integer>>();

    static
    {
        for (final String s : ConfigManagerCore.sealableIDs)
        {
            try
            {
                BlockTuple bt = ConfigManagerCore.stringToBlock(s, "External Sealable IDs", true);
                if (bt == null)
                {
                    continue;
                }

                int meta = bt.meta;

                if (OxygenPressureProtocol.nonPermeableBlocks.containsKey(bt.block))
                {
                    final ArrayList<Integer> list = OxygenPressureProtocol.nonPermeableBlocks.get(bt.block);
                    if (!list.contains(meta))
                    {
                        list.add(meta);
                    }
                    else
                    {
                    }
                }
                else
                {
                    final ArrayList<Integer> list = new ArrayList<Integer>();
                    list.add(meta);
                    OxygenPressureProtocol.nonPermeableBlocks.put(bt.block, list);
                }
            }
            catch (final Exception e)
            {
            }
        }
    }

    public static void updateSealerStatus(TileEntityOxygenSealer head)
    {
        try
        {
            head.threadSeal = new ThreadFindSeal(head);
        }
        catch (IllegalThreadStateException e)
        {

        }
    }

    public static void onEdgeBlockUpdated(World world, BlockPos vec)
    {
        if (ConfigManagerCore.enableSealerEdgeChecks)
        {
            TickHandlerServer.scheduleNewEdgeCheck(LabStuffUtils.getDimensionID(world), vec);
        }
    }

    public static boolean canBlockPassAir(World world, Block block, BlockPos pos, EnumFacing side)
    {
        if (block == null)
        {
            return true;
        }

        //Check leaves first, because their isOpaqueCube() test depends on graphics settings
        //(See net.minecraft.block.BlockLeaves.isOpaqueCube()!)
        if (block instanceof BlockLeaves)
        {
            return true;
        }

        if (block.isOpaqueCube(world.getBlockState(pos)))
        {
            return block instanceof BlockGravel || block.getMaterial(world.getBlockState(pos)) == Material.CLOTH || block instanceof BlockSponge;

        }

        if (block instanceof BlockGlass || block instanceof BlockStainedGlass)
        {
            return false;
        }

        if (block instanceof IPartialSealableBlock)
        {
            return !((IPartialSealableBlock) block).isSealed(world, pos, side);
        }

        //Solid but non-opaque blocks, for example special glass
        if (OxygenPressureProtocol.nonPermeableBlocks.containsKey(block))
        {
            ArrayList<Integer> metaList = OxygenPressureProtocol.nonPermeableBlocks.get(block);
            IBlockState state = world.getBlockState(pos);
            if (metaList.contains(Integer.valueOf(-1)) || metaList.contains(state.getBlock().getMetaFromState(state)))
            {
                return false;
            }
        }

        //Half slab seals on the top side or the bottom side according to its metadata
        if (block instanceof BlockSlab)
        {
            IBlockState state = world.getBlockState(pos);
            int meta = state.getBlock().getMetaFromState(state);
            return !(side == EnumFacing.DOWN && (meta & 8) == 8 || side == EnumFacing.UP && (meta & 8) == 0);
        }

        //Farmland etc only seals on the solid underside
        if (block instanceof BlockFarmland || block instanceof BlockEnchantmentTable || block instanceof BlockLiquid)
        {
            return side != EnumFacing.UP;
        }

        if (block instanceof BlockPistonBase)
        {
            BlockPistonBase piston = (BlockPistonBase) block;
            IBlockState state = world.getBlockState(pos);
            if (((Boolean) state.getValue(BlockPistonBase.EXTENDED)).booleanValue())
            {
                EnumFacing facing = (EnumFacing) state.getValue(BlockPistonBase.FACING);
                return side != facing;
            }
            return false;
        }

        //General case - this should cover any block which correctly implements isBlockSolidOnSide
        //including most modded blocks - Forge microblocks in particular is covered by this.
        // ### Any exceptions in mods should implement the IPartialSealableBlock interface ###
        return !block.isSideSolid(world.getBlockState(pos), world, pos, EnumFacing.getFront(side.getIndex() ^ 1));
    }
}