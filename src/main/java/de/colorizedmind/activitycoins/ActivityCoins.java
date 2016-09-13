package de.colorizedmind.activitycoins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.colorizedmind.activitycoins.listeners.PlayerListener;
import de.colorizedmind.activitycoins.tasks.PayoutTask;

public class ActivityCoins extends JavaPlugin {
	
	public final static String PREFIX = "§8[§6ActivityCoins§8] §7";
	
	private Map<UUID, Double> activities = new HashMap<>();
	private Map<UUID, List<Location>> blockLocations = new HashMap<>();
	private Economy econ;
	private long lastPayout;
	
	public void onEnable() {

		initializeConfig();
		initializeListeners();
		initializeEconomy();
		initializeTasks();

		lastPayout = System.currentTimeMillis();
	}

	private void initializeConfig() {
		getConfig().addDefault("interval", 15);
		getConfig().addDefault("activityLogSize", 5);
		getConfig().addDefault("worth.chat", 1);
		getConfig().addDefault("worth.command", 0.1);
		getConfig().addDefault("worth.blockplace", 2);
		getConfig().addDefault("worth.blockbreak", 1);
		getConfig().addDefault("worth.kill", 1);
		getConfig().addDefault("worth.fishing", 1);
		getConfig().addDefault("worth.max", 1000);
		getConfig().addDefault("income.min", 0);
		getConfig().addDefault("income.max", 500);
		getConfig().addDefault("logging", true);
		getConfig().addDefault("announce", true);
		getConfig().addDefault("multiplier.survival", 1);
		getConfig().addDefault("multiplier.creative", 0.5);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	private void initializeListeners() {
		this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
	}

	private void initializeEconomy() {
		if (!setupEconomy()) {
			getLogger().warning("Disabled due to no Vault dependency found!");
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	private void initializeTasks() {
		int interval = getConfig().getInt("interval") * 20 * 60;
		new PayoutTask(this).runTaskTimerAsynchronously(this, interval, interval);
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
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player && cmd.getName().equalsIgnoreCase("activity")) {
			Player player = (Player) sender;
			double maxPoints = getConfig().getDouble("worth.max");
			double points = 0;
			
			if(activities.containsKey(player.getUniqueId())) {
				points = activities.get(player.getUniqueId());
			}
			
			if (maxPoints < points) {
				points = maxPoints;
			}
			
			double reachedPercent = points / maxPoints;
			double timeToPayout = getConfig().getInt("interval") - (System.currentTimeMillis() - lastPayout) / 60 / 1000;
			
			sender.sendMessage(ActivityCoins.PREFIX + "Activity: " + drawChart(reachedPercent));
			sender.sendMessage(ActivityCoins.PREFIX + "Payout in: " + (int) round(timeToPayout, 0) + " minutes");
			
			return true;
		}
		return false;
	}
	
	public void addPlayerIfNotAdded(Player player) {
		if (!activities.containsKey(player.getUniqueId())) {
			activities.put(player.getUniqueId(), 0.0);
		}
		if (!blockLocations.containsKey(player.getUniqueId())) {
			blockLocations.put(player.getUniqueId(), new ArrayList<Location>());
		}
	}
	
	public void removePlayer(Player player) {
		activities.remove(player.getUniqueId());
		blockLocations.remove(player.getUniqueId());
	}
	
	public void addBlockActivity(Player player, Location loc, Double worth) {
		if (interactsWithPreviousBlocks(player, loc)) {
			return;
		}
        logLocation(player, loc);
		addActivity(player, worth);
	}
	
	public void addActivity(Player player, Double worth) {
		addPlayerIfNotAdded(player);
		double getCurrent = activities.get(player.getUniqueId());
		getCurrent = getCurrent + worth;
		activities.put(player.getUniqueId(), getCurrent);
	}

	public void handleActivity(Player player) {
		if (player.isOnline()) {
			double maxPoints = getConfig().getDouble("worth.max");
			double maxIncome = getConfig().getDouble("income.max");
			double minIncome = getConfig().getDouble("income.min");
			double points = 0;
			
			if(activities.containsKey(player.getUniqueId())) {
				points = activities.get(player.getUniqueId());
			}
			
			if (maxPoints < points) {
				points = maxPoints;
			}
			
			double reachedPercent = points / maxPoints;
			double reachedMoney = maxIncome * reachedPercent;
			
			if (reachedMoney < minIncome) {
				payout(player, minIncome, reachedPercent);
			} else {
				payout(player, reachedMoney, reachedPercent);
			}
			
			activities.put(player.getUniqueId(), 0.0);
		} else {
			removePlayer(player);
		}
	}
	
	private void payout(Player player, double amount, double percent) {
		econ.depositPlayer(player, round(amount, 2));
		if(getConfig().getBoolean("announce")) {
			player.sendMessage(PREFIX + "Activity: " + drawChart(percent));
			player.sendMessage(PREFIX + "Payout: " + econ.format(round(amount, 2)));
		}
		if(getConfig().getBoolean("logging")) {
			getLogger().info("[ActivityCoins] Payout: " + econ.format(round(amount, 2)));
		}
	}
	
	private String drawChart(double percent) {
		String output = ChatColor.DARK_GRAY + "[";
		if(percent > 0.67) {
			output = output + ChatColor.GREEN;
		} else if(percent < 0.33) {
			output = output + ChatColor.RED;
		} else {
			output = output + ChatColor.YELLOW;
		}
		int length = (int) (20 * percent);
		for(int i = 1; i <= length; i++) {
			output = output + "|";
		}
		output = output + ChatColor.DARK_GRAY;
		for(int i = 20; i > length; i--) {
			output = output + ".";
		}
		output = output + "] " + round(percent * 100, 2) + "%";
		return output;
	}
	
	private double round(double input, double decimals) {
		if (input == 0) {
			return (double) 0;
		}
		decimals = Math.pow(10, decimals);
		return (double) Math.round(input * decimals) / decimals;
	}

	public void setLastPayout(long lastPayout) {
		this.lastPayout = lastPayout;
	}

	private boolean interactsWithPreviousBlocks(Player player, Location loc) {
        return blockLocations.containsKey(player.getUniqueId()) && blockLocations.get(player.getUniqueId()).contains(loc);
    }

    private void logLocation(Player player, Location loc) {
        if(blockLocations.get(player.getUniqueId()).size() >= this.getConfig().getInt("activityLogSize")) {
            blockLocations.get(player.getUniqueId()).remove(0);
        }
        blockLocations.get(player.getUniqueId()).add(loc);
    }

}