package de.colorizedmind.activitycoins.listeners;

import de.colorizedmind.activitycoins.ActivityCoins;
import de.colorizedmind.activitycoins.controllers.ActivityController;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * Created by Henning.Storck on 14.09.2016.
 */
public class KillActivityListener extends ActivityListener {

    public KillActivityListener(ActivityCoins plugin, ActivityController activityController) {
        super(plugin, activityController);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();

        if (!(entity.getLastDamageCause() instanceof EntityDamageByEntityEvent)) {
            return;
        }

        EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) entity.getLastDamageCause();

        if (!(damageEvent.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) damageEvent.getDamager();

        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        activityController.addActivity(
                player,
                plugin.getConfig().getDouble("worth.kill") * getMultiplier(player)
        );
    }

}
