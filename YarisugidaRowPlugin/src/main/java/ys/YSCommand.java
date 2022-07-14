package ys;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class YSCommand implements CommandExecutor {

	Plugin plugin;
	
	YSGame ysg;
	
	public YSCommand(Plugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args[0].equals("newgame")) {
			ysg = new YSGame(plugin);
		}else if(args[0].equals("join")) {
			
		}else if(args[0].equals("start")) {
			
		}
		return true;
	}

}
