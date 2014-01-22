package servertools.core.handler;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.Random;

public class FlatBedrockHandler implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider iChunkProvider, IChunkProvider iChunkProvider2) {

        BiomeGenBase biomeGenBase = world.getBiomeGenForCoords(chunkX, chunkZ);
        boolean isNether = biomeGenBase.biomeName.toLowerCase().equals("hell");

        for (int blockX = 0; blockX < 16; blockX++)
            for (int blockZ = 0; blockZ < 16; blockZ++) {

                if (isNether)
                    for (int blockY = 126; blockY > 121; blockY--)
                        if (world.getBlockId(chunkX * 16 + blockX, blockY, chunkZ * 16 + blockZ) == Block.bedrock.blockID)
                            world.setBlock(chunkX * 16 + blockX, blockY, chunkZ * 16 + blockZ, Block.netherrack.blockID, 0, 2);

                for (int blockY = 5; blockY > 0; blockY--)
                    if (world.getBlockId(chunkX * 16 + blockX, blockY, chunkZ * 16 + blockZ) == Block.bedrock.blockID)
                        if (isNether)
                            world.setBlock(chunkX * 16 + blockX, blockY, chunkZ * 16 + blockZ, Block.netherrack.blockID, 0, 2);
                        else
                            world.setBlock(chunkX * 16 + blockX, blockY, chunkZ * 16 + blockZ, Block.stone.blockID, 0, 2);

            }
    }
}
