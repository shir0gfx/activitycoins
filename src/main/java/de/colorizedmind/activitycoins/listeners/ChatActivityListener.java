package de.colorizedmind.activitycoins.listeners;

import de.colorizedmind.activitycoins.ActivityCoins;
import de.colorizedmind.activitycoins.controllers.ActivityController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Henning.Storck on 14.09.2016.
 */
public class ChatActivityListener implements Listener {

    private ActivityCoins plugin;
    private ActivityController activityController;

    public ChatActivityListener(ActivityCoins plugin, ActivityController activityController) {
        this.plugin = plugin;
        this.activityController = activityController;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        activityController.addActivity(
                event.getPlayer(),
                plugin.getConfig().getDouble("worth.chat")
        );
    }

}
