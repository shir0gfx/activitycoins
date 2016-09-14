package de.colorizedmind.activitycoins;

import de.colorizedmind.activitycoins.cmds.ActivityCmd;
import de.colorizedmind.activitycoins.controllers.ActivityController;
import de.colorizedmind.activitycoins.listeners.ActivityListener;
import de.colorizedmind.activitycoins.tasks.PayoutTask;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class ActivityCoins extends JavaPlugin {

    public final static String PREFIX = "§8[§6ActivityCoins§8] §7";
    private Economy econ;
    private ActivityController activityController;

    public void onEnable() {
        initializeConfig();
        initializeEconomy();
        initializeControllers();
        initializeListeners();
        initializeTasks();
        initializeCmds();
    }

    private void initializeConfig() {
        getConfig().addDefault("interval", 15);
        getConfig().addDefault("activityLogSize", 5);
        getConfig().addDefault("worth.chat", 1);
        getConfig().addDefault("worth.command", 0.1);
        getConfig().addDefault("worth.blockPlace", 2);
        getConfig().addDefault("worth.blockBreak", 1);
        getConfig().addDefault("worth.kill", 4);
        getConfig().addDefault("worth.fishing", 70);
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

    private void initializeControllers() {
        this.activityController = new ActivityController(this, econ);
    }

    private void initializeListeners() {
        this.getServer().getPluginManager().registerEvents(new ActivityListener(this, activityController), this);
    }

    private void initializeEconomy() {
        if (!setupEconomy()) {
            getLogger().warning("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void initializeTasks() {
        int interval = getConfig().getInt("interval") * 20 * 60;
        new PayoutTask(activityController).runTaskTimerAsynchronously(this, interval, interval);
    }

    private void initializeCmds() {
        this.getCommand("activity").setExecutor(new ActivityCmd(this, activityController));
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
}