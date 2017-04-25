package keegan.labstuff.world;

import java.util.Random;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.event.LSCoreEventPopulate;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;

public class BiomeDecoratorMars extends BiomeDecorator
{
	private WorldGenerator dirtGen;
    private WorldGenerator tinGen;
    private WorldGenerator copperGen;
    private WorldGenerator ironGen;
    private WorldGenerator iceGen;
    private World worldObj;

    public BiomeDecoratorMars()
    {
    	 this.copperGen = new WorldGenMinableMeta(LabStuffMain.marsBlock, 4, 0, true, LabStuffMain.marsBlock, 9);
         this.tinGen = new WorldGenMinableMeta(LabStuffMain.marsBlock, 4, 1, true, LabStuffMain.marsBlock, 9);
         this.ironGen = new WorldGenMinableMeta(LabStuffMain.marsBlock, 8, 3, true, LabStuffMain.marsBlock, 9);
         this.dirtGen = new WorldGenMinableMeta(LabStuffMain.marsBlock, 32, 6, true, LabStuffMain.marsBlock, 9);
         this.iceGen = new WorldGenMinableMeta(Blocks.ICE, 18, 0, true, LabStuffMain.marsBlock, 6);
    }

    @Override
    public void decorate(World worldIn, Random random, Biome biome, BlockPos blockPos)
    {
        if (this.worldObj != null)
        {
            throw new RuntimeException("Already decorating!!");
        }
        else
        {
            this.worldObj = worldIn;
            this.chunkPos = blockPos;
            this.generateMars(random);
            this.worldObj = null;
        }
    }

    private void genStandardOre(int amountPerChunk, WorldGenerator worldGenerator, int minY, int maxY, Random random)
    {
        for (int var5 = 0; var5 < amountPerChunk; ++var5)
        {
            BlockPos blockpos = this.chunkPos.add(random.nextInt(16), random.nextInt(maxY - minY) + minY, random.nextInt(16));
            worldGenerator.generate(this.worldObj, random, blockpos);
        }
    }

    private void generateMars(Random random)
    {
        MinecraftForge.EVENT_BUS.post(new LSCoreEventPopulate.Pre(this.worldObj, random, chunkPos));
        this.genStandardOre(26, this.copperGen, 0, 60, random);
        this.genStandardOre(23, this.tinGen, 0, 60, random);
        this.genStandardOre(20, this.ironGen, 0, 64, random);
        this.genStandardOre(20, this.dirtGen, 0, 200, random);
        this.genStandardOre(4, this.iceGen, 60, 120, random);
        MinecraftForge.EVENT_BUS.post(new LSCoreEventPopulate.Post(this.worldObj, random, chunkPos));
    }
}