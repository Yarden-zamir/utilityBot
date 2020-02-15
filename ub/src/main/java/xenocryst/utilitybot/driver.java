package xenocryst.utilitybot;


import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import org.pf4j.PluginManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class driver {
    public static void main(String[] args) {
        for (String s : args) {
            configManager.put(s);
            System.out.println("s = " + s);
        }
        System.out.print("potatoes");
        new driver();
    }

    public driver() {
        String token = "";
        DiscordClient client = new DiscordClientBuilder(token).build();
    }
}

class userWindow extends JFrame {
    public userWindow() throws HeadlessException {

    }
}

class configManager {
    public static ArrayList<configNameSpace> nameSpaces = new ArrayList<configNameSpace>();

    public static configNameSpace put(String arg) {
        String nameSpace = null;
        String keyname = null;
        Class valueType = null;
        Object value = null;

        if (arg.isEmpty() || !arg.contains("[") || !arg.contains("]")) {
            //ERROR
        } else {
            String[] split = arg.split("\\[");

            keyname = split[0].split(":")[1];
            nameSpace = split[0].split(":")[0];


            //
            switch (split[1].split("\\]")[0].charAt(0)) {
                case 'b':
                    valueType = Boolean.TYPE;
                    if (arg.split("\\=")[1] == "true") {
                        value = true;
                    } else {
                        value = false;
                    }
                    break;
                case 's':
                    value = arg.split("\\=")[1];
                    break;
            }

            //
            for (configNameSpace ns : nameSpaces) {
                if (ns.getName().equals(nameSpace)) {
                    //there is a namespace that fits the bill, we should add the entry to that namespace
                    ns.addEntry(nameSpace, valueType, value);
                }
            }
        }
        return null;
    }

}

class configNameSpace {
    private String name;
    public ArrayList<configEntry> entries = new ArrayList<>();

    public String getName() {
        return name;
    }

    public configEntry addEntry(String name, Class type, Object value) {
        configEntry c = new configEntry(name, type, value);
        entries.add(c);
        return c;
    }


    public configNameSpace(String namespace) {

    }
}

class configEntry {
    private Class type;
    private String name;
    private Object value;

    public configEntry(String name, Class type, Object value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }
}