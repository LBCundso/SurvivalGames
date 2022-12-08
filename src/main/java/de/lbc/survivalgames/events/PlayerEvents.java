package de.lbc.survivalgames.events;

import de.lbc.survivalgames.Main;
import de.lbc.survivalgames.helper.Game;
import de.lbc.survivalgames.helper.GameState;
import de.lbc.survivalgames.helper.PlayerPack;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerEvents implements Listener {

    //Spieler tritt bei
    @EventHandler
    public void onGameVillagerClick(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Villager) {
            if (e.getRightClicked().getCustomName() == null) return;
            Player plr = e.getPlayer();
            Villager villager = (Villager) e.getRightClicked();

            String name = ChatColor.stripColor(villager.getCustomName());
            if (name.equals("SurvivalGames")) {
                e.setCancelled(true);
                boolean joined = Game.addPlayer(plr);
                if (!joined) plr.sendMessage(Main.PREFIX + "Zurzeit gibt es kein offenes Spiel.");
                else plr.sendMessage(Main.PREFIX + "Spiel gefunden! Teleportiere...");
            }
        }
    }

    //Spieler verlässt Spiel
    @EventHandler
    public void onPlrQuit(PlayerQuitEvent e) {
        Player plr = e.getPlayer();
        for (Game game : Game.getGames()) {
            if (game.getPlayerPacks().containsKey(plr)) {
                game.removePlayer(plr);
                return;
            }
        }
    }

    @EventHandler
    public void onPlrTP(PlayerTeleportEvent e) {
        Player plr = e.getPlayer();
        for (Game game : Game.getGames()) {
            if (game.getPlayerPacks().containsKey(plr)) {
                if (!e.getTo().getWorld().equals(game.getWorld())) {
                    game.removePlayer(plr);
                    return;
                } else {
                    plr.getInventory().clear();
                }
            }
        }
    }

    // Spieler stirbt
    @EventHandler
    public void onPlrDmg(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player plr = (Player) e.getEntity();
            if (plr.getHealth() - e.getDamage() <= 0) {
                for (Game game : Game.getGames()) {
                    if (game.getPlayerPacks().containsKey(plr)) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                PlayerPack pack = game.getPlayerPacks().get(plr);
                                if (pack.getLastAttacker() != null && System.currentTimeMillis() - pack.getLastAttack() <= 500)
                                    game.getPlayerPacks().get(pack.getLastAttacker()).increaseKills();
                                game.killPlayer(plr);
                                plr.setHealth(20);
                                plr.setFoodLevel(20);
                                dropItems(plr);
                                plr.setGameMode(GameMode.SPECTATOR);
                            }
                        }.runTaskLater(Main.getPlugin(), 1);
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlrDMGPlr(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player plr = (Player) e.getEntity();
            for (Game game : Game.getGames()) {
                if (game.getPlayerPacks().containsKey(plr)) {
                    if (plr.getHealth() - e.getDamage() <= 0) e.setCancelled(true);
                    Player attacker = (Player) e.getDamager();
                    game.getPlayerPacks().get(plr).setLastAttacker(attacker);
                }
            }
        }
    }

    private void dropItems(Player plr) {
        for (ItemStack item : plr.getInventory().getContents()) {
            if (item == null) continue;
            plr.getWorld().dropItemNaturally(plr.getLocation(), item);
        }
        plr.getInventory().clear();
    }

    //Spieler bewegt sich während Countdown
    @EventHandler
    public void onPlrMove(PlayerMoveEvent e) {
        if (e.getTo() == null || e.getTo().distance(e.getFrom()) == 0) return;
        Player plr = e.getPlayer();
        for (Game game : Game.getGames()) {
            if (game.getPlayerPacks().containsKey(plr)) {
                if (game.getGameState() == GameState.STARTING) e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPlrDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player plr = (Player) e.getEntity();
            for (Game game : Game.getGames()) {
                if (game.getPlayerPacks().containsKey(plr)) {
                    if (!game.getWorld().equals(e.getEntity().getWorld())) return;
                    if (game.getGameState() == GameState.RUNNING) return;
                    e.setCancelled(true);
                    ((Player) e.getEntity()).setHealth(20);

                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlrHunger(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player plr = (Player) e.getEntity();
            for (Game game : Game.getGames()) {
                if (game.getPlayerPacks().containsKey(plr)) {
                    if (!game.getWorld().equals(e.getEntity().getWorld())) return;
                    if (game.getGameState() == GameState.RUNNING) return;
                    e.setCancelled(true);
                    ((Player) e.getEntity()).setHealth(20);

                    return;
                }
            }
        }
    }
}
