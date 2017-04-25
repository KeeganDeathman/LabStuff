package keegan.labstuff.world;

import java.util.Random;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.event.LSCoreEventPopulate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;

public class BiomeDecoratorVenus extends BiomeDecorator
{
    private WorldGenerator aluminumGen;
    private WorldGenerator copperGen;
    private WorldGenerator galenaGen;
    private WorldGenerator quartzGen;
    private WorldGenerator siliconGen;
    private WorldGenerator tinGen;
    private World worldObj;

    public BiomeDecoratorVenus()
    {
        this.aluminumGen = new WorldGenMinableMeta(LabStuffMain.venusBlock, 6, 4, true, LabStuffMain.venusBlock, 1);
        this.copperGen = new WorldGenMinableMeta(LabStuffMain.venusBlock, 6, 5, true, LabStuffMain.venusBlock, 1);
        this.galenaGen = new WorldGenMinableMeta(LabStuffMain.venusBlock, 6, 6, true, LabStuffMain.venusBlock, 1);
        this.quartzGen = new WorldGenMinableMeta(LabStuffMain.venusBlock, 6, 7, true, LabStuffMain.venusBlock, 1);
        this.siliconGen = new WorldGenMinableMeta(LabStuffMain.venusBlock, 6, 8, true, LabStuffMain.venusBlock, 1);
        this.tinGen = new WorldGenMinableMeta(LabStuffMain.venusBlock, 6, 9, true, LabStuffMain.venusBlock, 1);
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
            this.generateVenus(random);
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

    private void generateVenus(Random random)
    {
        MinecraftForge.EVENT_BUS.post(new LSCoreEventPopulate.Pre(this.worldObj, random, chunkPos));
        this.genStandardOre(18, this.aluminumGen, 0, 60, random);
        this.genStandardOre(24, this.copperGen, 0, 60, random);
        this.genStandardOre(18, this.galenaGen, 0, 60, random);
        this.genStandardOre(26, this.quartzGen, 0, 60, random);
        this.genStandardOre(4, this.siliconGen, 0, 60, random);
        this.genStandardOre(22, this.tinGen, 0, 60, random);
        MinecraftForge.EVENT_BUS.post(new LSCoreEventPopulate.Post(this.worldObj, random, chunkPos));
    }
}