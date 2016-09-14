package de.colorizedmind.activitycoins.listeners;

import de.colorizedmind.activitycoins.ActivityCoins;
import de.colorizedmind.activitycoins.controllers.ActivityController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Created by Henning.Storck on 14.09.2016.
 */
public class BlockPlaceActivityListener implements Listener {

    private ActivityCoins plugin;
    private ActivityController activityController;

    public BlockPlaceActivityListener(ActivityCoins plugin, ActivityController activityController) {
        this.plugin = plugin;
        this.activityController = activityController;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        double worth = 0.0;

        switch(event.getPlayer().getGameMode()) {
            case SURVIVAL: worth = plugin.getConfig().getDouble("worth.blockPlaceSurvival"); break;
            case CREATIVE: worth = plugin.getConfig().getDouble("worth.blockPlaceCreative"); break;
        }

        activityController.addBlockActivity(
                event.getPlayer(),
                event.getBlock().getLocation(),
                worth
        );
    }

}
