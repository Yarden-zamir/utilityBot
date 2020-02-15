package xenocryst.utilitybot;


import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import xenocryst.utilitybot.config.configManager;
import xenocryst.utilitybot.modules.module;

import java.util.List;

public class driver {
    PluginManager pluginManager = new DefaultPluginManager();

    public static void main(String[] args) {
        new driver(args);
    }

    public driver(String[] args) {
        for (String arg : args) configManager.put(arg);
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
        String p = configManager.retrieveConfig("discord_github").getEntry("doesPoop").getValue().toString();
        System.out.println(p);
    }

    public void loadModules(PluginManager pl){
        //if there are different types of modules this method will call submethodes for loading those
        List<module> mainModules = pl.getExtensions(module.class);
        System.out.println("There are "+ mainModules.size() + " main modules");
        mainModules.stream().map((module -> {
            module.loadModule(configManager.retrieveConfig(module.getClass().getCanonicalName()));
            return module;
        })).forEachOrdered(module -> {
            System.out.println(">>> Finished loading " + module.getClass().getCanonicalName() + "\n");
        });
    }
}

