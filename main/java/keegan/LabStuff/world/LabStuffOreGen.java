package keegan.labstuff.world;

import java.util.Random;

import keegan.labstuff.LabStuffMain;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.*;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class LabStuffOreGen implements IWorldGenerator
{

    @Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        switch (world.provider.getDimension())
        {
            case -1:
                this.generateNether(world, random, chunkX * 16, chunkZ * 16);
            case 0:
                this.generateSurface(world, random, chunkX * 16, chunkZ * 16);

        }

    }

    private void generateSurface(World world, Random rand, int baseX, int baseZ)
    {
        // rarity -smaller number = rarer
        for (int x = 0; x < 25; x++)
        {
            int Xcoord = baseX + rand.nextInt(16);
            int Zcoord = baseZ + rand.nextInt(16);
            int Ycoord = rand.nextInt(30);
            // Max Vein Size
            
            new WorldGenMinable((IBlockState) LabStuffMain.blockCopperOre.getDefaultState(), 4).generate(world, rand, new BlockPos(Xcoord,Ycoord,Zcoord));
        }
        
        for (int x = 0; x < 25; x++)
        {
            int Xcoord = baseX + rand.nextInt(16);
            int Zcoord = baseZ + rand.nextInt(16);
            int Ycoord = rand.nextInt(30);
            // Max Vein Size
            
            new WorldGenMinable((IBlockState) LabStuffMain.bauxiteOre.getDefaultState(), 4).generate(world, rand, new BlockPos(Xcoord,Ycoord,Zcoord));
        }
        
        for (int x = 0; x < 25; x++)
        {
            int Xcoord = baseX + rand.nextInt(16);
            int Zcoord = baseZ + rand.nextInt(16);
            int Ycoord = rand.nextInt(30);
            // Max Vein Size
            
            new WorldGenMinable((IBlockState) LabStuffMain.titaniumOre.getDefaultState(), 4).generate(world, rand, new BlockPos(Xcoord,Ycoord,Zcoord));
        }
        
        for (int x = 0; x < 35; x++)
        {
            int Xcoord = baseX + rand.nextInt(16);
            int Zcoord = baseZ + rand.nextInt(16);
            int Ycoord = rand.nextInt(30);
            // Max Vein Size
            
            new WorldGenMinable((IBlockState) LabStuffMain.marble.getDefaultState(), 4).generate(world, rand, new BlockPos(Xcoord,Ycoord,Zcoord));
        }
        
        for(int i = 0; i < 5; i++)
		{
			int randPosX = (baseX*16) + rand.nextInt(16);
			int randPosZ = (baseZ*16) + rand.nextInt(16);
			BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(randPosX, 60, randPosZ));
			new WorldGenSalt(6).generate(world, rand, pos);
		}

        // rarity -smaller number = rarer
       
        for (int x = 0; x < 25; x++)
        {
            int Xcoord = baseX + rand.nextInt(16);
            int Zcoord = baseZ + rand.nextInt(16);
            int Ycoord = rand.nextInt(20) + 30;
            
            // Max Vein Size
            new WorldGenMinable((IBlockState) LabStuffMain.blockZincOre.getDefaultState(), 5).generate(world, rand, new BlockPos(Xcoord,Ycoord,Zcoord));
        }
        for (int x = 0; x < 25; x++)
        {
            int Xcoord = baseX + rand.nextInt(16);
            int Zcoord = baseZ + rand.nextInt(16);
            int Ycoord = rand.nextInt(20) + 30;
            
            // Max Vein Size
            new WorldGenMinable((IBlockState) LabStuffMain.blockMangOre.getDefaultState(), 5).generate(world, rand, new BlockPos(Xcoord,Ycoord,Zcoord));
        }
        for (int x = 0; x < 25; x++)
        {
            int Xcoord = baseX + rand.nextInt(16);
            int Zcoord = baseZ + rand.nextInt(16);
            int Ycoord = rand.nextInt(20) + 30;
            
            // Max Vein Size
            new WorldGenMinable((IBlockState) LabStuffMain.blockSiliconOre.getDefaultState(), 5).generate(world, rand, new BlockPos(Xcoord,Ycoord,Zcoord));
        }
        
    }

    private void generateNether(World world, Random random, int i, int j)
    {

    }

}