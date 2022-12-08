package de.lbc.survivalgames.helper;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerPack {
    Player plr;
    Location lobbyspawn;
    int kills = 0;
    boolean death = false;
    boolean win = false;

    Player lastAttacker;
    long lastAttack = System.currentTimeMillis();

    public void increaseKills(){
        kills++;
    }

    public long getLastAttack() {
        return lastAttack;
    }

    public Player getLastAttacker() {
        return lastAttacker;
    }

    public void setLastAttacker(Player lastAttacker) {
        this.lastAttacker = lastAttacker;
        lastAttack = System.currentTimeMillis();
    }

    public PlayerPack(Player player){
        plr = player;
        lobbyspawn = player.getLocation();
    }
}
