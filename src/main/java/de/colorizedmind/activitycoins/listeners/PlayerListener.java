package de.colorizedmind.activitycoins.listeners;

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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.colorizedmind.activitycoins.ActivityCoins;

public class PlayerListener implements Listener {

	private ActivityCoins plugin;

	public PlayerListener(ActivityCoins plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		plugin.addPlayer(event.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		plugin.removePlayer(event.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerKick(PlayerKickEvent event) {
		plugin.removePlayer(event.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent event) {
		if(isCancelled(event.getPlayer())) {
			return;
		}
		plugin.addBlockActivity(
				event.getPlayer(),
				event.getBlock().getLocation(),
				this.plugin.getConfig().getDouble("worth.blockplace"));
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event) {
		if(isCancelled(event.getPlayer())) {
			return;
		}
		plugin.addBlockActivity(
				event.getPlayer(),
				event.getBlock().getLocation(),
				this.plugin.getConfig().getDouble("worth.blockbreak"));
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onChat(AsyncPlayerChatEvent event) {
		if(isCancelled(event.getPlayer())) {
			return;
		}
		plugin.addActivity(
				event.getPlayer(),
				this.plugin.getConfig().getDouble("worth.chat"));
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		if(isCancelled(event.getPlayer())) {
			return;
		}
		plugin.addActivity(
				event.getPlayer(),
				this.plugin.getConfig().getDouble("worth.command"));
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) entity.getLastDamageCause();
			if(damageEvent.getDamager() instanceof Player) {
				Player player = (Player) damageEvent.getDamager();
				if(isCancelled(player)) {
					return;
				}
				plugin.addActivity(
						player,
						this.plugin.getConfig().getDouble("worth.kill"));
			}
		}
	}

	private boolean isCancelled(Player player) {
		return player.getGameMode() != GameMode.SURVIVAL;
	}

}