package yrsg;

import org.bukkit.plugin.java.JavaPlugin;

public class YrsgMain extends JavaPlugin{
	@Override
	public void onEnable() {
		getCommand("yrsg").setExecutor(new YrsgCommand(this));
	}


}
