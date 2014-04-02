/*
 * Copyright 2014 Matthew Prenger
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

package com.matthewprenger.servertools.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BlockLogger {

    private File logDirectory;

    private static final String FILE_HEADER = "TimeStamp,PlayerName,DimID,BlockX,BlockY,BlockZ,BlockName";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-YYYY");
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("kk-mm-ss");

    public BlockLogger(File logDirectory) {

        if (logDirectory.exists() && !logDirectory.isDirectory())
            throw new IllegalArgumentException("File with same name as block logging directory detected");

        logDirectory.mkdirs();

        this.logDirectory = logDirectory;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {

        try {
            File logFile = new File(logDirectory, DATE_FORMAT.format(Calendar.getInstance().getTime()) + ".csv");

            if (!logFile.exists())
                writeHeader(logFile);

            FileWriter fw = new FileWriter(logFile, true);
            BufferedWriter writer = new BufferedWriter(fw);

            writer.write(String.format("%s,%s,%s,%s,%s,%s,%s", TIME_FORMAT.format(Calendar.getInstance().getTime()), event.getPlayer().getCommandSenderName(),
                    event.world.provider.dimensionId, event.x, event.y, event.z, event.block.getUnlocalizedName()));
            writer.newLine();

            writer.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            ServerTools.log.getLogger().warn("Failed to write block break event to file", e);
        }
    }

    private static void writeHeader(File file) {

        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fw);

            writer.write(FILE_HEADER);
            writer.newLine();

            writer.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
