package com.matthewprenger.servertools.teleport;

import com.matthewprenger.servertools.core.YesNoRequest;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class WarpToRequest extends YesNoRequest {

    String sender;
    String target;

    public WarpToRequest(String sender, String target) {

        this.sender = sender;
        this.target = target;
    }

    @Override
    public void onYes() {

        EntityPlayerMP senderPlayer = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(sender);
        EntityPlayerMP targetPlayer = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(target);

        if (senderPlayer == null || targetPlayer == null) return;

        if (senderPlayer.worldObj.provider.dimensionId != targetPlayer.worldObj.provider.dimensionId)
            senderPlayer.travelToDimension(targetPlayer.worldObj.provider.dimensionId);

        senderPlayer.setPositionAndUpdate(targetPlayer.posX, targetPlayer.posY, targetPlayer.posZ);

        senderPlayer.addChatMessage("Teleported to " + target);
        targetPlayer.addChatMessage(sender + " teleported to you");
    }

    @Override
    public void onNo() {

        EntityPlayerMP senderPlayer = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(sender);
        EntityPlayerMP targetPlayer = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(target);

        if (senderPlayer == null || targetPlayer == null) return;

        senderPlayer.addChatMessage(target + " denied your warp request");
        targetPlayer.addChatMessage("Denied warp request from " + sender);
    }
}
