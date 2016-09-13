package de.colorizedmind.activitycoins.tasks;

import de.colorizedmind.activitycoins.controllers.ActivityController;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PayoutTask extends BukkitRunnable {
	
	private ActivityController activityController;
	
	public PayoutTask(ActivityController plugin) {
		this.activityController = plugin;
	}

	@Override
	public void run() {
		activityController.setLastPayout(System.currentTimeMillis());
		for(Player player : Bukkit.getOnlinePlayers()){
			activityController.handleActivity(player);
		}
	}

}
