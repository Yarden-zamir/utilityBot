package xenocryst.utilitybot;


import xenocryst.utilitybot.moduleSystem.config.ConfigManager;
import xenocryst.utilitybot.moduleSystem.modules.ModuleManager;

import java.nio.file.Paths;

public class Driver {
	public static void main(String[] args) {
		ConfigManager.put(args);
		ConfigManager.put(Paths.get("config.args"));
		ModuleManager.loadModules();
	}
}

