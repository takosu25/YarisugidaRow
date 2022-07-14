package yrsg;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class YrsgCommand implements CommandExecutor {

	Plugin mainPlugin;
	YrsgGame yg;

	public YrsgCommand(Plugin plugin) {
		mainPlugin = plugin;

	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String args2, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player)sender;
			if(args[0].equals("newgame")) {
				yg = new YrsgGame(mainPlugin);
			}else if(args[0].equals("join")) {
				if(args[1].equals("@a")) {
					for(Player p:player.getWorld().getPlayers()) {
						//yg.JoinPlayer(p);
					}
				}
			}else if(args[0].equals("start")) {
				yg.Start();
			}else if(args[0].equals("max")) {
				yg.max = Integer.parseInt(args[1]);
			}
		}
		return true;
	}

}
