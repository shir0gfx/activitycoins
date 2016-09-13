package de.colorizedmind.activitycoins.listeners;

import de.colorizedmind.activitycoins.controllers.ActivityController;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;

import de.colorizedmind.activitycoins.ActivityCoins;

public class ActivityListener implements Listener {

    private ActivityCoins plugin;
	private ActivityController activityController;

	public ActivityListener(ActivityCoins plugin, ActivityController activityController) {
        this.plugin = plugin;
		this.activityController = activityController;
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
        activityController.addPlayerIfNotAdded(event.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
        activityController.removePlayer(event.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerKick(PlayerKickEvent event) {
        activityController.removePlayer(event.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent event) {
        activityController.addBlockActivity(
				event.getPlayer(),
				event.getBlock().getLocation(),
                plugin.getConfig().getDouble("worth.blockplace") * getMultiplier(event.getPlayer())
		);
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event) {
        activityController.addBlockActivity(
				event.getPlayer(),
				event.getBlock().getLocation(),
				this.plugin.getConfig().getDouble("worth.blockbreak") * getMultiplier(event.getPlayer())
		);
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onChat(AsyncPlayerChatEvent event) {
        activityController.addActivity(
				event.getPlayer(),
				this.plugin.getConfig().getDouble("worth.chat")
		);
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        activityController.addActivity(
				event.getPlayer(),
				this.plugin.getConfig().getDouble("worth.command")
		);
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerFishEvent(PlayerFishEvent event) {

		if(event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
		if(event.getPlayer().getGameMode() != GameMode.SURVIVAL) return;

        activityController.addActivity(
                event.getPlayer(),
                this.plugin.getConfig().getDouble("worth.fishing")
        );
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();

		if(!(entity.getLastDamageCause() instanceof EntityDamageByEntityEvent)) return;
        EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) entity.getLastDamageCause();

        if(!(damageEvent.getDamager() instanceof Player)) return;
        Player player = (Player) damageEvent.getDamager();

        activityController.addActivity(
                player,
                this.plugin.getConfig().getDouble("worth.kill") * getMultiplier(player)
        );
	}

	private double getMultiplier(Player player) {
		if(player.getGameMode() == GameMode.SURVIVAL) {
			return this.plugin.getConfig().getDouble("multiplier.survival");
		}
		if(player.getGameMode() == GameMode.CREATIVE) {
			return this.plugin.getConfig().getDouble("multiplier.creative");
		}
		return 0;
	}

}