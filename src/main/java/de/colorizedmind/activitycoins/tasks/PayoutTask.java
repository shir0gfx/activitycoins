package de.colorizedmind.activitycoins.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.colorizedmind.activitycoins.ActivityCoins;

public class PayoutTask extends BukkitRunnable {
	
	private ActivityCoins plugin;
	
	public PayoutTask(ActivityCoins plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		plugin.setLastPayout(System.currentTimeMillis());
		for(Player player : Bukkit.getOnlinePlayers()){
			plugin.handleActivity(player);
		}
	}

}
