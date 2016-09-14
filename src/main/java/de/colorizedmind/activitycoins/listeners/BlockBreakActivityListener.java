package de.colorizedmind.activitycoins.listeners;

import de.colorizedmind.activitycoins.ActivityCoins;
import de.colorizedmind.activitycoins.controllers.ActivityController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Created by Henning.Storck on 14.09.2016.
 */
public class BlockBreakActivityListener extends ActivityListener {

    public BlockBreakActivityListener(ActivityCoins plugin, ActivityController activityController) {
        super(plugin, activityController);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        activityController.addBlockActivity(
                event.getPlayer(),
                event.getBlock().getLocation(),
                plugin.getConfig().getDouble("worth.blockbreak") * getMultiplier(event.getPlayer())
        );
    }

}
