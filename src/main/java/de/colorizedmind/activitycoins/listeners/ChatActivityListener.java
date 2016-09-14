package de.colorizedmind.activitycoins.listeners;

import de.colorizedmind.activitycoins.ActivityCoins;
import de.colorizedmind.activitycoins.controllers.ActivityController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Henning.Storck on 14.09.2016.
 */
public class ChatActivityListener extends ActivityListener {

    public ChatActivityListener(ActivityCoins plugin, ActivityController activityController) {
        super(plugin, activityController);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        activityController.addActivity(
                event.getPlayer(),
                plugin.getConfig().getDouble("worth.chat")
        );
    }

}
