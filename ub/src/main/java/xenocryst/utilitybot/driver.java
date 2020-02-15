package xenocryst.utilitybot;


import com.sun.webkit.plugin.PluginManager;
import xenocryst.utilitybot.config.configManager;

public class driver {

    public static void main(String[] args) {
        new driver(args);
    }

    public driver(String[] args) {
        for (String arg : args) configManager.put(arg);
//pugin load
        String p = configManager.retrieveConfig("discord_github").getEntry("doesPoop").getValue().toString();

    }

    public void loadModules(PluginManager pl){
        //if there are different types of modules this method will call submethodes for loading those
    }
}

