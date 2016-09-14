package de.colorizedmind.activitycoins.listeners;

import de.colorizedmind.activitycoins.ActivityCoins;
import de.colorizedmind.activitycoins.controllers.ActivityController;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Created by Henning.Storck on 14.09.2016.
 */
public abstract class ActivityListener implements Listener {

    protected ActivityCoins plugin;
    protected ActivityController activityController;

    public ActivityListener(ActivityCoins plugin, ActivityController activityController) {
        this.plugin = plugin;
        this.activityController = activityController;
    }

    protected double getMultiplier(Player player) {
        if (player.getGameMode() == GameMode.SURVIVAL) {
            return this.plugin.getConfig().getDouble("multiplier.survival");
        }
        if (player.getGameMode() == GameMode.CREATIVE) {
            return this.plugin.getConfig().getDouble("multiplier.creative");
        }
        return 0;
    }

}
