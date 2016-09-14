package de.colorizedmind.activitycoins.listeners;

import de.colorizedmind.activitycoins.ActivityCoins;
import de.colorizedmind.activitycoins.controllers.ActivityController;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * Created by Henning.Storck on 14.09.2016.
 */
public class FishingActivityListener extends ActivityListener {

    public FishingActivityListener(ActivityCoins plugin, ActivityController activityController) {
        super(plugin, activityController);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerFishEvent(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) return;

        activityController.addActivity(
                event.getPlayer(),
                plugin.getConfig().getDouble("worth.fishing")
        );
    }

}
