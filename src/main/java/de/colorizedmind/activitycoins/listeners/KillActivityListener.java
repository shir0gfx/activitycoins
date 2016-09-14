package de.colorizedmind.activitycoins.listeners;

import de.colorizedmind.activitycoins.ActivityCoins;
import de.colorizedmind.activitycoins.controllers.ActivityController;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by Henning.Storck on 14.09.2016.
 */
public class KillActivityListener implements Listener {

    private ActivityCoins plugin;
    private ActivityController activityController;

    public KillActivityListener(ActivityCoins plugin, ActivityController activityController) {
        this.plugin = plugin;
        this.activityController = activityController;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();

        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        activityController.addActivity(
                player,
                plugin.getConfig().getDouble("worth.kill")
        );
    }

}
