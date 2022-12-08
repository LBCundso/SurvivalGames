package de.lbc.survivalgames.helper;

import de.lbc.survivalgames.Main;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {
    private static ArrayList<Game> games = new ArrayList<>();

    World world;
    Location lobby;
    ArrayList<Location> spawns = new ArrayList<>();
    HashMap<Player, PlayerPack> playerPacks = new HashMap<>();
    public ArrayList<Chest> chests = new ArrayList<>();
    double borderMaxWidth;
    GameState gameState = GameState.WAITING;
    long waitingSince;
    BukkitTask waitingTimer = null;
    int minPlayers = 2;

    public Game(ArrayList<Location> locations, Location gameLobby) {
        world = locations.get(0).getWorld();
        world.setDifficulty(Difficulty.PEACEFUL);
        spawns = locations;
        lobby = gameLobby;
        borderMaxWidth = world.getWorldBorder().getSize();
        waitingSince = System.currentTimeMillis();
    }

    public static ArrayList<Game> getGames() {
        return games;
    }

    public HashMap<Player, PlayerPack> getPlayerPacks() {
        return playerPacks;
    }

    public World getWorld() {
        return world;
    }

    public GameState getGameState() {
        return gameState;
    }

    public static boolean addPlayer(Player plr) {
        Game game2Join = null;
        for (Game game : getGames()) {
            if (!(game.getGameState() == GameState.WAITING)) continue;
            if (game2Join == null || game2Join.waitingSince > game.waitingSince) game2Join = game;
        }
        if (game2Join == null) return false; //Kein offenes Spiel verfügbar.

        //Zuweisung eines Spielers zu game2Join
        game2Join.playerPacks.put(plr, new PlayerPack(plr));
        plr.teleport(game2Join.lobby);
        if (game2Join.playerPacks.size() == game2Join.spawns.size()) {
            game2Join.startGame();
        } else if (game2Join.playerPacks.size() >= game2Join.minPlayers && game2Join.waitingTimer == null) {
            Game finalGame2Join = game2Join;
            int cfinal = 60;
            game2Join.waitingTimer = new BukkitRunnable() {
                int c = cfinal;

                @Override
                public void run() {
                    for (Player plr : finalGame2Join.playerPacks.keySet()) {
                        plr.setLevel(c);
                        plr.setExp(c / (float) cfinal);
                    }
                    if (finalGame2Join.getGameState() != GameState.WAITING) {
                        plr.setLevel(0);
                        plr.setExp(0);
                        cancel();
                    } else if (c == 0) {
                        finalGame2Join.startGame();
                        cancel();
                    }
                    c--;
                }
            }.runTaskTimer(Main.getPlugin(), 0, 20);
        }
        return true;
    }

    public void startGame() {
        gameState = GameState.STARTING;
        Player[] plrs = playerPacks.keySet().toArray(new Player[0]);
        for (int i = 0; i < plrs.length; i++) {
            plrs[i].teleport(spawns.get(i));
        }
        world.setTime(1000);
        world.setDifficulty(Difficulty.NORMAL);
        new BukkitRunnable() {
            int c = 10;

            @Override
            public void run() {
                for (Player plr : playerPacks.keySet()) {
                    ActionBarMSG.sendMessage(plr, "Das Spiel startet in " + c + " Sekunde(n).", 1);
                    plr.playSound(plr.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
                }
                if (c == 0) {
                    for (Player plr : playerPacks.keySet()) {
                        ActionBarMSG.sendMessage(plr, "Das Spiel startet jetzt!", 3);
                        plr.playSound(plr.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                        world.getWorldBorder().setSize(20, (long) (borderMaxWidth * 4));
                    }
                    gameState = GameState.RUNNING;
                    cancel();
                }
                c--;
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
    }

    public void killPlayer(Player plr) {
        PlayerPack pack = playerPacks.get(plr);
        pack.death = true;

        plr.setGameMode(GameMode.SPECTATOR);

        int alive = checkForGameEnd();
        if (alive > 1) {
            broadcastMessage(Main.PREFIX + plr.getName() + " ist gestorben. Es verbleiben " + alive + " Spieler.");
        }
        plr.setLevel(0);
        plr.setExp(0);
    }

    public void removePlayer(Player plr) {
        if (gameState == GameState.RUNNING || gameState == GameState.STARTING) {
            playerPacks.get(plr).death = true;
            savePlrStats(playerPacks.get(plr));
            playerPacks.remove(plr);

            int alive = checkForGameEnd();
            if (alive > 1) {
                broadcastMessage(Main.PREFIX + plr.getName() + " hat die SurvivalGames verlassen. Es verbleiben " + alive + " Spieler.");
            }
        } else if (gameState == GameState.WAITING) {
            playerPacks.remove(plr);
            if (playerPacks.keySet().size() <= minPlayers && waitingTimer != null) {
                waitingTimer.cancel();
                waitingTimer = null;

                for (Player player : playerPacks.keySet()) {
                    player.setExp(0);
                    player.setLevel(0);
                }
            }
        }
        plr.setExp(0);
        plr.setLevel(0);
    }

    private int checkForGameEnd() {
        int alive = 0;
        Player winner = null;
        for (Player player : playerPacks.keySet()) {
            if (!playerPacks.get(player).death && player.getGameMode() == GameMode.SURVIVAL) {
                alive++;
                winner = player;
            }
        }

        if (alive <= 1) {
            if (alive == 1) {
                playerPacks.get(winner).win = true;
                gameState = GameState.WIN;
                broadcastMessage(Main.PREFIX + winner.getName() + " hat das Spiel gewonnen!");
                for (Player player : playerPacks.keySet()) {
                    player.sendTitle(winner.getName() + " hat gewonnen!", "Läuft...", 10, 5 * 20, 10);
                    player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
                    player.setFoodLevel(20);
                    player.setHealth(20);
                }
            } else if (alive == 0) {
                broadcastMessage(Main.PREFIX + "Niemand hat gewonnen.");
            }
            broadcastMessage(Main.PREFIX + "Das Spiel wird in 10 Sekunden zurückgesetzt.");
            new BukkitRunnable() {
                @Override
                public void run() {
                    restart();
                }
            }.runTaskLater(Main.getPlugin(), 200);
        }
        return alive;
    }

    private void restart() {
        gameState = GameState.RESTARTING;

        for (Player player : playerPacks.keySet()) {
            PlayerPack pack = playerPacks.get(player);
            savePlrStats(pack);
            player.teleport(pack.lobbyspawn);
        }
        playerPacks = new HashMap<>();
        chests = new ArrayList<>();

        for (Entity item : world.getEntities()) {
            if (item.getType() == EntityType.DROPPED_ITEM) item.remove();
        }

        world.getWorldBorder().setSize(borderMaxWidth);

        waitingTimer = null;
        waitingSince = System.currentTimeMillis();
        gameState = GameState.WAITING;
    }

    public void savePlrStats(PlayerPack pack) {
        FileConfiguration cfg = Main.getPlugin().getConfig();
        int kills = cfg.getInt("stats." + pack.plr.getUniqueId() + ".kills", 0);
        int wins = cfg.getInt("stats." + pack.plr.getUniqueId() + ".wins", 0);
        int deaths = cfg.getInt("stats." + pack.plr.getUniqueId() + ".deaths", 0);
        int playedGames = cfg.getInt("stats." + pack.plr.getUniqueId() + ".playedGames", 0);

        kills += pack.kills;
        if (pack.death) deaths++;
        if (pack.win) wins++;
        playedGames++;

        cfg.set("stats." + pack.plr.getUniqueId() + ".kills", kills);
        cfg.set("stats." + pack.plr.getUniqueId() + ".wins", wins);
        cfg.set("stats." + pack.plr.getUniqueId() + ".deaths", deaths);
        cfg.set("stats." + pack.plr.getUniqueId() + ".playedGames", playedGames);
        Main.getPlugin().saveConfig();
    }

    private void broadcastMessage(String message) {
        for (Player player : playerPacks.keySet()) {
            player.sendMessage(message);
        }
    }


    public static void loadMaps() {
        FileConfiguration cfg = Main.getPlugin().getConfig();
        ConfigurationSection mapSection = cfg.getConfigurationSection("maps");
        if (mapSection == null) return;
        for (String mapKey : mapSection.getKeys(false)) {
            ConfigurationSection spawnSection = cfg.getConfigurationSection("maps." + mapKey + ".spawns");
            if (spawnSection == null) return;
            boolean enabled = cfg.getBoolean("maps." + mapKey + ".active", false);
            if (!enabled) {
                continue;
            }
            loadGames(spawnSection, mapKey);
        }
    }

    private static void loadGames(ConfigurationSection spawnSection, String mapKey) {
        ArrayList<Location> locations = new ArrayList<>();
        for (String spawnKey : spawnSection.getKeys(false)) {
            Location loc = Main.getCfgLocation("maps." + mapKey + ".spawns." + spawnKey);
            if (loc != null) {
                locations.add(loc);
            }
        }
        FileConfiguration cfg = Main.getPlugin().getConfig();
        Location gameLobby = Main.getCfgLocation("maps." + mapKey + ".lobby");
        int width = cfg.getInt("maps." + mapKey + ".border.width", -1);
        Location borderMid = Main.getCfgLocation("maps." + mapKey + ".border");

        for(World world1 : Bukkit.getWorlds()){
            if(!world1.getName().startsWith(mapKey)) continue;
            Location borderMid2 = copyLocation(borderMid, world1);
            Location gameLobby2 = copyLocation(gameLobby, world1);

            ArrayList<Location> locations2 = new ArrayList<>();
            for (Location location : locations){
                locations2.add(copyLocation(location, world1));
            }

            if (borderMid2 != null) {
                borderMid2.getWorld().getWorldBorder().setCenter(borderMid2);
                borderMid2.getWorld().getWorldBorder().setSize(width);
            }
            games.add(new Game(locations2, gameLobby2));
        }
    }

    private static Location copyLocation(Location location, World wld) {
        return new Location(wld, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public static int getSpawnCount(String mapName) {
        FileConfiguration cfg = Main.getPlugin().getConfig();
        ConfigurationSection spawnSection = cfg.getConfigurationSection("maps." + mapName + ".spawns");
        if (spawnSection == null) return 0;
        return spawnSection.getKeys(false).size();
    }
}
