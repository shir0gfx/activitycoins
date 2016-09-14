package de.colorizedmind.activitycoins.controllers;

import de.colorizedmind.activitycoins.ActivityCoins;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Henning on 13.09.2016.
 */
public class ActivityController {

    private ActivityCoins plugin;
    private Economy econ;
    private Map<UUID, Double> activities = new HashMap<>();
    private Map<UUID, List<Location>> blockLocations = new HashMap<>();
    private long lastPayout;

    public ActivityController(ActivityCoins plugin, Economy econ) {
        this.plugin = plugin;
        this.econ = econ;
        this.lastPayout = System.currentTimeMillis();
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
        addPlayerIfNotAdded(player);
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
            double maxPoints = plugin.getConfig().getDouble("worth.max");
            double maxIncome = plugin.getConfig().getDouble("income.max");
            double minIncome = plugin.getConfig().getDouble("income.min");
            double points = 0;

            if (activities.containsKey(player.getUniqueId())) {
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
        if (plugin.getConfig().getBoolean("announce")) {
            player.sendMessage(plugin.PREFIX + "Activity: " + drawChart(percent));
            player.sendMessage(plugin.PREFIX + "Payout: " + econ.format(round(amount, 2)));
        }
        if (plugin.getConfig().getBoolean("logging")) {
            plugin.getLogger().info("[ActivityCoins] Payout: " + econ.format(round(amount, 2)));
        }
    }

    public String drawChart(double percent) {
        String output = ChatColor.DARK_GRAY + "[";
        if (percent > 0.67) {
            output = output + ChatColor.GREEN;
        } else if (percent < 0.33) {
            output = output + ChatColor.RED;
        } else {
            output = output + ChatColor.YELLOW;
        }
        int length = (int) (20 * percent);
        for (int i = 1; i <= length; i++) {
            output = output + "|";
        }
        output = output + ChatColor.DARK_GRAY;
        for (int i = 20; i > length; i--) {
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
        return blockLocations.get(player.getUniqueId()).contains(loc);
    }

    private void logLocation(Player player, Location loc) {
        if (blockLocations.get(player.getUniqueId()).size() >= plugin.getConfig().getInt("activityLogSize")) {
            blockLocations.get(player.getUniqueId()).remove(0);
        }
        blockLocations.get(player.getUniqueId()).add(loc);
    }

    public double getPoints(Player player) {
        double maxPoints = plugin.getConfig().getDouble("worth.max");
        double points = 0.0;

        if (activities.containsKey(player.getUniqueId())) {
            points = activities.get(player.getUniqueId());
        }

        if (maxPoints < points) {
            points = maxPoints;
        }

        return points;
    }

    public long getLastPayout() {
        return lastPayout;
    }
}
