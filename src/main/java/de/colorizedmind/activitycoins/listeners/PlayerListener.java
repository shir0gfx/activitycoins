package de.colorizedmind.activitycoins.listeners;

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
	public void onPlayerJoin(PlayerJoinEvent e) {
		plugin.addPlayer(e.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent e) {
		plugin.removePlayer(e.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerKick(PlayerKickEvent e) {
		if (e.isCancelled()) return;
		plugin.removePlayer(e.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent e) {
		if (e.isCancelled()) return;
		plugin.addBlockActivity(
				e.getPlayer(),
				e.getBlock().getLocation(),
				this.plugin.getConfig().getDouble("worth.blockplace"));
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.isCancelled()) return;
		plugin.addBlockActivity(
				e.getPlayer(),
				e.getBlock().getLocation(),
				this.plugin.getConfig().getDouble("worth.blockbreak"));
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onChat(AsyncPlayerChatEvent e) {
		if (e.isCancelled()) return;
		plugin.addActivity(
				e.getPlayer(),
				this.plugin.getConfig().getDouble("worth.chat"));
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		if (e.isCancelled()) return;
		plugin.addActivity(
				e.getPlayer(),
				this.plugin.getConfig().getDouble("worth.command"));
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onEntityDeath(EntityDeathEvent e) {
		Entity entity = e.getEntity();
		if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) entity.getLastDamageCause();
			if(damageEvent.getDamager() instanceof Player) {
				Player player = (Player) damageEvent.getDamager();
				plugin.addActivity(
						player,
						this.plugin.getConfig().getDouble("worth.kill"));
			}
		}
	}

}