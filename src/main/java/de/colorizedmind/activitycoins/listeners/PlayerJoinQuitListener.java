package de.colorizedmind.activitycoins.listeners;

import de.colorizedmind.activitycoins.controllers.ActivityController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Henning.Storck on 14.09.2016.
 */
public class PlayerJoinQuitListener implements Listener {

    private ActivityController activityController;

    public PlayerJoinQuitListener(ActivityController activityController) {
        this.activityController = activityController;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        activityController.addPlayerIfNotAdded(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        activityController.removePlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event) {
        activityController.removePlayer(event.getPlayer());
    }

}
