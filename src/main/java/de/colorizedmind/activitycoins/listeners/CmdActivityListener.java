package de.colorizedmind.activitycoins.listeners;

import de.colorizedmind.activitycoins.ActivityCoins;
import de.colorizedmind.activitycoins.controllers.ActivityController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by Henning.Storck on 14.09.2016.
 */
public class CmdActivityListener extends ActivityListener {

    public CmdActivityListener(ActivityCoins plugin, ActivityController activityController) {
        super(plugin, activityController);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        activityController.addActivity(
                event.getPlayer(),
                plugin.getConfig().getDouble("worth.command")
        );
    }

}
