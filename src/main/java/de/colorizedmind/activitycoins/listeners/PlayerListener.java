package de.colorizedmind.activitycoins.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
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
		if (!this.plugin.getActivities().containsKey(e.getPlayer().getUniqueId())) {
			this.plugin.getActivities().put(e.getPlayer().getUniqueId(), (double) 0);
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent e) {
		this.plugin.getActivities().remove(e.getPlayer().getUniqueId());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerKick(PlayerKickEvent e) {
		this.plugin.getActivities().remove(e.getPlayer().getUniqueId());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent e) {
		if (e.isCancelled()) {
			return;
		}
		
		if (!this.plugin.getActivities().containsKey(e.getPlayer().getUniqueId())) {
			this.plugin.getActivities().put(e.getPlayer().getUniqueId(), (double) 0);
		}
		
		if (e.getPlayer() instanceof Player) {
			double getCurrent = this.plugin.getActivities().get(e.getPlayer().getUniqueId());
			getCurrent = getCurrent + this.plugin.getConfig().getDouble("worth.blockplace");
			this.plugin.getActivities().put(e.getPlayer().getUniqueId(), getCurrent);
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.isCancelled()) {
			return;
		}
		
		if (!this.plugin.getActivities().containsKey(e.getPlayer().getUniqueId())) {
			this.plugin.getActivities().put(e.getPlayer().getUniqueId(), (double) 0);
		}
		
		if (e.getPlayer() instanceof Player) {
			double getCurrent = this.plugin.getActivities().get(e.getPlayer().getUniqueId());
			getCurrent = getCurrent + this.plugin.getConfig().getDouble("worth.blockbreak");
			this.plugin.getActivities().put(e.getPlayer().getUniqueId(), getCurrent);
		}
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onChat(AsyncPlayerChatEvent e) {
		if (e.isCancelled()) {
			return;
		}
		
		if (!this.plugin.getActivities().containsKey(e.getPlayer().getUniqueId())) {
			this.plugin.getActivities().put(e.getPlayer().getUniqueId(), (double) 0);
		}
		
		if (e.getPlayer() instanceof Player) {
			double getCurrent = this.plugin.getActivities().get(e.getPlayer().getUniqueId());
			getCurrent = getCurrent + this.plugin.getConfig().getDouble("worth.chat");
			this.plugin.getActivities().put(e.getPlayer().getUniqueId(), getCurrent);
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		if (e.isCancelled()) {
			return;
		}
		
		if (!this.plugin.getActivities().containsKey(e.getPlayer().getUniqueId())) {
			this.plugin.getActivities().put(e.getPlayer().getUniqueId(), (double) 0);
		}
		
		if (e.getPlayer() instanceof Player) {
			double getCurrent = this.plugin.getActivities().get(e.getPlayer().getUniqueId());
			getCurrent = getCurrent + this.plugin.getConfig().getDouble("worth.command");
			this.plugin.getActivities().put(e.getPlayer().getUniqueId(), getCurrent);
		}
	}

}