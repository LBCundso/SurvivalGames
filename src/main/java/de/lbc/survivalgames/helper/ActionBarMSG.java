package de.lbc.survivalgames.helper;

import de.lbc.survivalgames.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class ActionBarMSG extends BukkitRunnable {
    private static BukkitTask task;

    private static HashMap<Player, String> plrMsgs = new HashMap<>();
    private static HashMap<Player, Integer> plrTimes = new HashMap<>();

    public static void start() {
        if (task == null) {
            task = new ActionBarMSG().runTaskTimer(Main.getPlugin(), 0, 20);
        }
    }

    public static void stop() {
        if (task != null) {
            task.cancel();
        }
    }

    public static void sendMessage(Player plr, String msg, int seconds) {
        if (msg != null) {
            plrMsgs.put(plr, msg);
            plrTimes.put(plr, seconds);
            plr.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(msg));
        }
    }

    @Override
    public void run() {
        ArrayList<Player> toRemove = new ArrayList<>();

        for (Player plr : plrMsgs.keySet()) {
            plrTimes.put(plr, plrTimes.get(plr) - 1);
            if (plrTimes.get(plr) > 0) {
                plr.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(plrMsgs.get(plr)));
            } else {
                toRemove.add(plr);
            }
        }

        for (Player plr : toRemove) {
            plrTimes.remove(plr);
            plrMsgs.remove(plr);
        }
    }
}
