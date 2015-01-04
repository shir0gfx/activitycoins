package de.colorizedmind.activitycoins;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.colorizedmind.activitycoins.listeners.PlayerListener;

public class ActivityCoins extends JavaPlugin {
	
	public final static String PREFIX = ChatColor.GOLD + "[ActivityCoins] " + ChatColor.GRAY;
	
	private Map<UUID, Double> activities = new HashMap<UUID, Double>();
	private Economy econ = null;
	private long lastPayout;
	
	public void onEnable() {
		
		// Load config
		getConfig().addDefault("interval", 15);
		getConfig().addDefault("worth.chat", 1);
		getConfig().addDefault("worth.command", 0.1);
		getConfig().addDefault("worth.blockplace", 2);
		getConfig().addDefault("worth.blockbreak", 1);
		getConfig().addDefault("worth.interaction", 0.5);
		getConfig().addDefault("worth.max", 1000);
		getConfig().addDefault("income.min", 0);
		getConfig().addDefault("income.max", 10);
		getConfig().addDefault("logging", true);
		getConfig().addDefault("announce", true);
				
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		// Register player listener
		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new PlayerListener(this), this);
		
		// Load Vault
		if (!setupEconomy() ) {
			getLogger().warning("Disabled due to no Vault dependency found!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		// Register payout timer
		lastPayout = System.currentTimeMillis();
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				lastPayout = System.currentTimeMillis();
				for(Player online : Bukkit.getOnlinePlayers()){
					handleActivity(online);
				}
			}
		}, getConfig().getInt("interval") * 20 * 60, getConfig().getInt("interval") * 20 * 60);
	}
	
	public Map<UUID, Double> getActivities() {
		return activities;
	}
	
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("activity")) {
				
				double percent = round(getActivities().get(player.getUniqueId()), 2) / getConfig().getDouble("worth.max");
				double timeToPayout = getConfig().getInt("interval") - (System.currentTimeMillis() - lastPayout) / 60 / 1000;
				
				sender.sendMessage(ActivityCoins.PREFIX + "Aktivität: " + drawChart(percent));
				sender.sendMessage(ActivityCoins.PREFIX + "Payout in: " + (int) round(timeToPayout, 0) + " Minuten");
			}
		}
		return true;
	}

	private void handleActivity(Player player) {
		if (player.isOnline()) {
			double maxPoints = getConfig().getDouble("worth.max");
			double maxIncome = getConfig().getDouble("income.max");
			double minIncome = getConfig().getDouble("income.min");
			double points = 0;
			
			if(activities.containsKey(player.getUniqueId())) {
				points = (double) activities.get(player.getUniqueId());
			}
			
			if (maxPoints <= points) {
				payout(player.getUniqueId(), maxIncome, 1);
			} else {
				double reachedPercent = points / maxPoints;
				double reachedMoney = maxIncome * reachedPercent;
				
				if (reachedMoney < minIncome) {
					payout(player.getUniqueId(), minIncome, 0);
				} else {
					payout(player.getUniqueId(), reachedMoney, reachedPercent);
				}					
			}
			
			activities.put(player.getUniqueId(), 0.0);
		}
	}
	
	private void payout(UUID playerUUID, double amount, double percent) {
		
		Player player = getServer().getPlayer(playerUUID);
		if(player != null) {
		
			econ.depositPlayer(player, round(amount, 2));
			if(getConfig().getBoolean("announce")) {
				player.sendMessage(PREFIX + "Aktivität: " + drawChart(percent));
				player.sendMessage(PREFIX + "Payout: " + round(amount, 2) + " " + econ.currencyNamePlural());
			}
			if(getConfig().getBoolean("logging")) {
				getLogger().info("[ActivityCoins] Payout: " + round(amount, 2) + " " + econ.currencyNamePlural() + " for player " + player.getName());
			}
			
		}
	}
	
	public String drawChart(double percent) {
		String output = ChatColor.DARK_GRAY + "[";
		if(percent > 0.7) {
			output = output + ChatColor.GREEN;
		} else if(percent < 0.3) {
			output = output + ChatColor.RED;
		} else {
			output = output + ChatColor.YELLOW;
		}
		int length = (int) (20 * percent);
		for(int i = 0; i < length; i++) {
			output = output + "|";
		}
		output = output + ChatColor.DARK_GRAY;
		for(int i = 20; i >= length; i--) {
			output = output + ".";
		}
		output = output + "] " + round(percent * 100, 2) + "%";
		return output;
	}
	
	public double round(double input, double decimals) {
		if (input == 0) {
			return (double) 0;
		}
		decimals = Math.pow(10, decimals);
		return (double) Math.round(input * decimals) / decimals;
	}

}