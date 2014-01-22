package servertools.core.handler;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandServerEmote;
import net.minecraft.command.CommandServerMessage;
import net.minecraft.command.CommandServerSay;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.ServerChatEvent;
import servertools.core.STLog;
import servertools.core.ServerTools;
import servertools.core.lib.Strings;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

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

public class VoiceHandler {

    private Set<String> voicedUsers;
    private Set<String> silencedUsers;

    private final File voiceFile;
    private final File silenceFile;

    private final Gson gson;

    public VoiceHandler() {

        voicedUsers = new HashSet<String>();
        silencedUsers = new HashSet<String>();

        voiceFile = new File(ServerTools.serverToolsDir, "voice.json");
        silenceFile = new File(ServerTools.serverToolsDir, "silence.json");

        gson = new GsonBuilder().setPrettyPrinting().create();

        loadSilenceList();
        loadVoiceList();

        MinecraftForge.EVENT_BUS.register(this);
    }

    public boolean isUserVoiced(String username) {

        return voicedUsers.contains(username.toLowerCase());
    }

    public void giveVoice(String username) {

        voicedUsers.add(username.toLowerCase());
        saveVoiceList();
    }

    public boolean removeVoice(String username) {

        if (voicedUsers.contains(username.toLowerCase())) {
            voicedUsers.remove(username.toLowerCase());
            saveVoiceList();
            return true;
        }
        return false;
    }

    public boolean isUserSilenced(String username) {

        return silencedUsers.contains(username.toLowerCase());
    }

    public void silence(String username) {

        silencedUsers.add(username.toLowerCase());
        saveSilenceList();
    }

    public boolean removeSilence(String username) {

        if (silencedUsers.contains(username.toLowerCase())) {
            silencedUsers.remove(username.toLowerCase());
            saveSilenceList();
            return true;
        }
        return false;
    }

    public Set<String> getVoicedUsers() {

        return ImmutableSet.copyOf(voicedUsers);
    }

    public Set<String> getSilencedUsers() {

        return ImmutableSet.copyOf(silencedUsers);
    }

    public void saveVoiceList() {

        String gsonRepresentation = gson.toJson(voicedUsers);

        try {
            writeStringToFile(gsonRepresentation, voiceFile);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            STLog.warning("Error saving voice file");
        }
    }

    @SuppressWarnings("unchecked")
    public void loadVoiceList() {

        if (!voiceFile.exists())
            return;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(voiceFile));
            voicedUsers = gson.fromJson(reader, HashSet.class);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            STLog.warning("Error loading voice file");
        }
    }

    public void saveSilenceList() {

        String gsonRepresentation = gson.toJson(silencedUsers);

        try {
            writeStringToFile(gsonRepresentation, silenceFile);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            STLog.warning("Error saving silence file");
        }
    }

    @SuppressWarnings("unchecked")
    public void loadSilenceList() {

        if (!silenceFile.exists())
            return;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(silenceFile));
            silencedUsers = gson.fromJson(reader, HashSet.class);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            STLog.warning("Error loading silence file");
        }
    }

    @ForgeSubscribe
    public void serverChat(ServerChatEvent event) {

        if (isUserVoiced(event.username)) {
            ChatMessageComponent component = event.component;
            event.component = ChatMessageComponent.createFromText(EnumChatFormatting.AQUA + "[+]" + EnumChatFormatting.RESET);
            event.component.appendComponent(component);
        }
        if (MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(event.username) && !Minecraft.getMinecraft().isSingleplayer()) {
            ChatMessageComponent component = event.component;
            event.component = ChatMessageComponent.createFromText(EnumChatFormatting.RED + "[OP]" + EnumChatFormatting.RESET);
            event.component.appendComponent(component);
        }
        if (isUserSilenced(event.username)) {
            event.setCanceled(true);
            event.player.sendChatToPlayer(ChatMessageComponent.createFromText(Strings.ERROR_SILENCED).setColor(EnumChatFormatting.RED));
        }

    }

    @ForgeSubscribe
    public void command(CommandEvent event) {

        if (isUserSilenced(event.sender.getCommandSenderName())) {
            if (event.command instanceof CommandServerSay || event.command instanceof CommandServerMessage || event.command instanceof CommandServerEmote) {

                event.setCanceled(true);
                event.sender.sendChatToPlayer(ChatMessageComponent.createFromText(Strings.ERROR_SILENCED).setColor(EnumChatFormatting.RED));
            }
        }
    }

    private void writeStringToFile(String string, File file) throws IOException {

        FileWriter writer = new FileWriter(file);
        writer.write(string);
        writer.flush();
        writer.close();
    }
}
