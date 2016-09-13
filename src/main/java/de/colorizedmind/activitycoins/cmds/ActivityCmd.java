package de.colorizedmind.activitycoins.cmds;

import de.colorizedmind.activitycoins.ActivityCoins;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Henning on 13.09.2016.
 */
public class ActivityCmd implements CommandExecutor {

    private ActivityCoins plugin;

    public ActivityCmd(ActivityCoins plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player && cmd.getName().equalsIgnoreCase("activity")) {

            Player player = (Player) sender;

            double maxPoints = plugin.getConfig().getDouble("worth.max");
            double points = plugin.getPoints(player);
            double reachedPercent = points / maxPoints;
            double timeToPayout = plugin.getConfig().getInt("interval") - (System.currentTimeMillis() - plugin.getLastPayout()) / 60 / 1000;

            sender.sendMessage(ActivityCoins.PREFIX + "Activity: " + plugin.drawChart(reachedPercent));
            sender.sendMessage(String.format(ActivityCoins.PREFIX + "Payout in: %.2f minutes", timeToPayout));

            return true;
        }
        return false;
    }

}
