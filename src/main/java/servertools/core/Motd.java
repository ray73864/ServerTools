package servertools.core;

import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import servertools.core.config.ConfigSettings;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Motd implements IPlayerTracker {

    public static final String FILE_ENCODING = "UTF-8";

    private String[] motd;
    private final File motdFile;

    public Motd(File motdFile) {

        this.motdFile = motdFile;
        loadMotd();
        GameRegistry.registerPlayerTracker(this);
    }

    public void loadMotd() {

        try {
            if (!motdFile.exists()) {
                Writer out = new OutputStreamWriter(new FileOutputStream(motdFile), FILE_ENCODING);

                for (String line : CoreConstants.DEFAULT_MOTD) {
                    out.write(line);
                    out.write(CoreConstants.LINE_SEPARATOR);
                }
                out.close();
            }

            BufferedReader reader = new BufferedReader(new FileReader(motdFile));
            List<String> lines = new ArrayList<String>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
            motd = lines.toArray(new String[lines.size()]);

        } catch (Exception e) {
            e.printStackTrace(System.err);
            STLog.warning("Unable to read the MOTD from file");
        }
    }

    public void serveMotd(EntityPlayer player) {

        for (String line : motd) {
            line = line.replace("$PLAYER$", player.getDisplayName());
            ChatMessageComponent component = ChatMessageComponent.createFromText(line);
            player.sendChatToPlayer(component);
        }
    }

    @Override
    public void onPlayerLogin(EntityPlayer player) {

        if (ConfigSettings.SEND_MOTD_ON_LOGIN) {
            serveMotd(player);
        }
    }

    @Override
    public void onPlayerLogout(EntityPlayer player) {

    }

    @Override
    public void onPlayerChangedDimension(EntityPlayer player) {

    }

    @Override
    public void onPlayerRespawn(EntityPlayer player) {

    }
}
