package keegan.labstuff.world;
import java.util.Random;

import keegan.labstuff.LabStuffMain;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.*;
import net.minecraftforge.fml.common.IWorldGenerator;

public class TreeManager implements IWorldGenerator {
	

@Override
public void generate(Random random, int chunkX, int chunkZ, World world,
		IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
	switch(world.provider.getDimension()){
		case -1: generateNether(world, random, chunkX * 16, chunkZ * 16);
		case 0: generateSurface(world, random, chunkX * 16, chunkZ * 16);
		case 1: generateEnd(world, random, chunkX * 16, chunkZ * 16);
	}
	
}

private void generateEnd(World world, Random random, int x, int z) {
	
}

private void generateSurface(World world, Random random, int x, int z) 
{
	Biome biomeGenBase = world.getBiomeGenForCoords(new BlockPos(x+16, 0, z+16));
	if(biomeGenBase == Biome.REGISTRY.getObject(new ResourceLocation("forest")))
	for (int i = 0; i < 20; i++){
		int Xcoord1 = x + random.nextInt(16); //where in chuck it generates
		int Ycoord1 = random.nextInt(100); //how high it generates
		int Zcoord1 = z + random.nextInt(16); //where in chunk it generates
		
		new WorldGenTree(false, 4, LabStuffMain.blockRubberLog.getDefaultState(), LabStuffMain.blockRubberLeaves.getDefaultState(), false).generate(world, random, new BlockPos(Xcoord1, Ycoord1, Zcoord1));
		
	}
	
}

private void generateNether(World world, Random random, int x, int z) {
	
}
}