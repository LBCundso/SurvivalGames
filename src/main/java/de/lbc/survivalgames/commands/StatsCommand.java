package de.lbc.survivalgames.commands;

import de.lbc.survivalgames.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player plr = (Player) sender;
            FileConfiguration cfg = Main.getPlugin().getConfig();
            int kills = cfg.getInt("stats." + plr.getUniqueId() + ".kills", 0);
            int wins = cfg.getInt("stats." + plr.getUniqueId() + ".wins", 0);
            int deaths = cfg.getInt("stats." + plr.getUniqueId() + ".deaths", 0);
            int playedGames = cfg.getInt("stats." + plr.getUniqueId() + ".playedGames", 0);
            float kd;

            if(deaths == 0){
                kd = (float) kills;
            } else {
                kd = (float) kills/deaths;
            }

            plr.sendMessage(Main.PREFIX + "-----------------------------------------------------");
            plr.sendMessage(Main.PREFIX + "Deine Stats:");
            plr.sendMessage(Main.PREFIX + "Gespielte Spiele: " + playedGames);
            plr.sendMessage(Main.PREFIX + "Getötete Spieler: " + kills);
            plr.sendMessage(Main.PREFIX + "Tode: " + deaths);
            plr.sendMessage(Main.PREFIX + "K/D: " + kd);
            plr.sendMessage(Main.PREFIX + "Gewonnene Spiele: " + wins);
            plr.sendMessage(Main.PREFIX + "-----------------------------------------------------");
        }

        return false;
    }
}
