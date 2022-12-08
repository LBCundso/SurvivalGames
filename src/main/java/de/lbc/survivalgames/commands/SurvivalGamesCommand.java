package de.lbc.survivalgames.commands;

import de.lbc.survivalgames.Main;
import de.lbc.survivalgames.helper.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.awt.print.PageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static de.lbc.survivalgames.Main.PREFIX;
import static de.lbc.survivalgames.Main.setCfgLocation;

public class SurvivalGamesCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            FileConfiguration cfg = Main.getPlugin().getConfig();
            Player plr = (Player) sender;
            if (args.length == 0) {
                sender.sendMessage(PREFIX + "Nutze /" + command.getName() + " enable/disable/addspawn/remove/setbordercenter/setlobby/spawnvillager/info [MAP_NAME/BORDERWIDTH]");
                return false;
            }
            else if (args.length == 1 && args[0].equals("spawnvillager")) {
                Villager villager = (Villager) plr.getWorld().spawnEntity(plr.getLocation(), EntityType.VILLAGER);
                villager.setCustomName("§2Survival§aGames");
                villager.setCustomNameVisible(true);
                villager.setAI(false);
                Location villagerLoc = Main.getCfgLocation("joinvillager");
                if (villagerLoc != null) {
                    Collection<Entity> entities = plr.getWorld().getNearbyEntities(villagerLoc, 0.5, 0.5, 0.5);
                    for (Entity entity : entities) {
                        if (entity instanceof Villager && entity.getCustomName().equals("§2Survival§aGames"))
                            entity.remove();
                    }
                }
                Main.setCfgLocation("joinvillager", plr.getLocation());
            } else {
                // /sg COMMAND MAP_NAME

                switch (args[0]) {
                    case "enable":
                        boolean enabled = cfg.getBoolean("maps." + plr.getWorld().getName() + ".active", false);
                        int spawnNum = Game.getSpawnCount(plr.getWorld().getName());
                        int borderWidth = cfg.getInt("maps." + plr.getWorld().getName() + ".border.width", -1);
                        if (!enabled) {
                            Location gameLobby = Main.getCfgLocation("maps." + plr.getWorld().getName() + ".lobby");
                            if (spawnNum > 1 && borderWidth > 0 && gameLobby != null) {
                                cfg.set("maps." + plr.getWorld().getName() + ".active", true);
                                plr.sendMessage(PREFIX + "Auf dieser Welt kann nun SurvivalGames gespielt werden.");
                            } else
                                plr.sendMessage(PREFIX + "Diese Welt erfüllt nicht alle Anforderungen (/sg info " + plr.getWorld().getName() + ")");
                        } else plr.sendMessage(PREFIX + "Diese Welt ist bereits aktiviert.");
                        break;
                    case "disable":
                        enabled = cfg.getBoolean("maps." + plr.getWorld().getName() + ".active", false);
                        if (enabled) {
                            cfg.set("maps." + plr.getWorld().getName() + ".active", false);
                            plr.sendMessage(PREFIX + "Diese Welt kann nun nicht mehr in SurvivalGames gespielt werden.");
                        } else plr.sendMessage(PREFIX + "Diese Welt ist bereits inaktiv.");
                        break;
                    case "setbordercenter":
                        if (args.length == 3) {
                            try {
                                int width = Integer.parseInt(args[1]);
                                    Main.setCfgLocation("maps." + plr.getWorld().getName() + ".border", plr.getLocation());
                                    cfg.set("maps." + args[1] + ".border.width", width);
                                    plr.getWorld().getWorldBorder().setCenter(plr.getLocation());
                                    plr.getWorld().getWorldBorder().setSize(width);
                                    plr.sendMessage(PREFIX + "Mitte der Map und Radius von " + args[1] + " Blöcken festgelegt.");
                            } catch (Exception e) {
                                plr.sendMessage(PREFIX + "Nutze: /" + command.getName() + " setbordercenter MAP_NAME RADIUS");
                            }
                        } else
                            plr.sendMessage(PREFIX + "Nutze: /" + command.getName() + " setbordercenter MAP_NAME RADIUS");
                        break;
                    case "setlobby":
                            setCfgLocation("maps." + plr.getWorld().getName() + ".lobby", plr.getLocation());
                            plr.sendMessage(PREFIX + "Du hast erfolgreich die Wartelobby auf der Map " + plr.getWorld().getName() + " gesetzt.");
                        break;
                    case "addspawn":
                            int spawnNumber = Game.getSpawnCount(plr.getWorld().getName());
                            setCfgLocation("maps." + plr.getWorld().getName() + ".spawns." + spawnNumber, plr.getLocation());
                            plr.sendMessage(PREFIX + "Du hast erfolgreich einen Spawn auf der Map " + plr.getWorld().getName() + " gesetzt.");
                        break;
                    case "info":
                        if (cfg.get("maps." + args[1]) != null) {
                            enabled = cfg.getBoolean("maps." + args[1] + ".active", false);
                            spawnNum = Game.getSpawnCount(args[1]);
                            borderWidth = cfg.getInt("maps." + args[1] + ".border.width", -1);

                            String enabledCol = "§c";
                            String spawnNumCol = "§c";
                            String borderWidthCol = "§c";
                            String gameLobbyStr = "§cNein";

                            Location gameLobby = Main.getCfgLocation("maps." + args[1] + ".lobby");

                            if (enabled) enabledCol = "§a";
                            if (spawnNum > 1) spawnNumCol = "§a";
                            if (borderWidth > 0) borderWidthCol = "§a";
                            if (gameLobby != null) gameLobbyStr = "§aJa";

                            plr.sendMessage("-----------------------------------------------------");
                            plr.sendMessage(PREFIX + "Informationen:");
                            plr.sendMessage(PREFIX + "Name: " + args[1]);
                            plr.sendMessage(PREFIX + "Spielstatus: " + enabledCol + enabled);
                            plr.sendMessage(PREFIX + "Verfügb. Spawns: " + spawnNumCol + spawnNum);
                            plr.sendMessage(PREFIX + "Wartelobby gesetzt?: " + gameLobbyStr);
                            plr.sendMessage(PREFIX + "Grenzenbreite: " + borderWidthCol + borderWidth);
                            plr.sendMessage("-----------------------------------------------------");
                        } else plr.sendMessage(PREFIX + "Diese Welt ist nicht eingetragen.");

                        break;
                    case "remove":
                    case "delete":
                        cfg.set("maps." + args[1], null);
                        plr.sendMessage(PREFIX + "Die Map " + args[1] + " wurde aus der Konfiguration entfernt.");
                        break;
                    default:
                        plr.sendMessage(Main.PREFIX + "Nutze /" + command.getName() + " enable/disable/addspawn/remove/setbordercenter/setlobby/spawnvillager/info [MAP_NAME/BORDERWIDTH]");
                }
                Main.getPlugin().saveConfig();
            }

        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.add("enable");
            list.add("disable");
            list.add("addspawn");
            list.add("setbordercenter");
            list.add("setlobby");
            list.add("spawnvillager");
            list.add("info");
        }
        return list;
    }
}
