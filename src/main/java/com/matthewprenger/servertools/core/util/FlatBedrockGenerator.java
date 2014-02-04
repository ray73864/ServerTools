package com.matthewprenger.servertools.core.util;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.Random;

/*
 * Copyright 2014 matthewprenger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class FlatBedrockGenerator implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider iChunkProvider, IChunkProvider iChunkProvider2) {

        BiomeGenBase biomeGenBase = world.getBiomeGenForCoords(chunkX, chunkZ);
        boolean isNether = "hell".equals(biomeGenBase.biomeName.toLowerCase());

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
