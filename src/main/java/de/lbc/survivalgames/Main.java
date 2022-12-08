package de.lbc.survivalgames;

import de.lbc.survivalgames.commands.StatsCommand;
import de.lbc.survivalgames.commands.SurvivalGamesCommand;
import de.lbc.survivalgames.events.BlockEvents;
import de.lbc.survivalgames.events.PlayerEvents;
import de.lbc.survivalgames.helper.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private static Plugin plugin;

    public static String PREFIX = "[§2Survival§aGames§r] >> ";

    public static Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        Game.loadMaps();

        //register Commands
        getCommand("survivalgames").setExecutor(new SurvivalGamesCommand());
        getCommand("sgstats").setExecutor(new StatsCommand());

        //register Events
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new BlockEvents(), this);
        pm.registerEvents(new PlayerEvents(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void setCfgLocation(String path, Location location) {
        FileConfiguration cfg = Main.getPlugin().getConfig();
        cfg.set(path + ".world", location.getWorld().getName());
        cfg.set(path + ".x", location.getX());
        cfg.set(path + ".y", location.getY());
        cfg.set(path + ".z", location.getZ());
        cfg.set(path + ".yaw", location.getYaw());
        cfg.set(path + ".pitch", location.getPitch());
        Main.getPlugin().saveConfig();
    }

    public static Location getCfgLocation(String path) {
        FileConfiguration cfg = Main.getPlugin().getConfig();
        String wldName = cfg.getString(path + ".world");
        if (wldName == null) return null;

        World world = Bukkit.getWorld(wldName);
        double x = cfg.getDouble(path + ".x");
        double y = cfg.getDouble(path + ".y");
        double z = cfg.getDouble(path + ".z");
        float yaw = (float) cfg.getDouble(path + ".yaw");
        float pitch = (float) cfg.getDouble(path + ".pitch");
        return new Location(world, x, y, z, yaw, pitch);
    }

}
