package de.lbc.survivalgames.events;

import de.lbc.survivalgames.helper.Game;
import de.lbc.survivalgames.helper.GameState;
import de.lbc.survivalgames.helper.RandomChestManager;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockEvents implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        for (Game game : Game.getGames()) {
            if (game.getWorld().equals(e.getBlock().getWorld())) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        for (Game game : Game.getGames()) {
            if (game.getWorld().equals(e.getBlock().getWorld())) {
                if (game.getGameState() == GameState.RUNNING) {
                    if (e.getBlock().equals(Material.LAVA)) return;
                    if (e.getBlock().equals(Material.TNT)) {
                        game.getWorld().getBlockAt(e.getBlock().getLocation()).setType(Material.AIR);
                        game.getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.PRIMED_TNT);
                    }
                } else e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChestOpen(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null || !(e.getClickedBlock().getState() instanceof Chest)) return;
        for (Game game : Game.getGames()) {
            if (!game.getPlayerPacks().containsKey(e.getPlayer())) continue;
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Player plr = e.getPlayer();
                Chest chest = (Chest) e.getClickedBlock().getState();

                if (!game.chests.contains(chest)) {
                    game.chests.add(chest);
                    //generate chest
                    RandomChestManager.fillRNG(chest, 4, 2, 1);
                }
            }
        }
    }

    @EventHandler
    public void onTNTExplode(EntityExplodeEvent e) {
        for (Game game : Game.getGames()) {
            if (game.getWorld().equals(e.getEntity().getWorld())) {
                e.setCancelled(true);
            }
        }
    }
}
