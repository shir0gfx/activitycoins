package de.colorizedmind.activitycoins.listeners;

import de.colorizedmind.activitycoins.ActivityCoins;
import de.colorizedmind.activitycoins.controllers.ActivityController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Created by Henning.Storck on 14.09.2016.
 */
public class BlockPlaceActivityListener extends ActivityListener {

    public BlockPlaceActivityListener(ActivityCoins plugin, ActivityController activityController) {
        super(plugin, activityController);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        activityController.addBlockActivity(
                event.getPlayer(),
                event.getBlock().getLocation(),
                plugin.getConfig().getDouble("worth.blockplace") * getMultiplier(event.getPlayer())
        );
    }

}
