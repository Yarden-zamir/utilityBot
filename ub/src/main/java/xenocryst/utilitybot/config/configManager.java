package xenocryst.utilitybot.config;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class configManager {
    public static ArrayList<configNameSpace> nameSpaces = new ArrayList<configNameSpace>();

    @Nullable
    public static configNameSpace retrieveConfig(String namespaceName) {
        for (configNameSpace c : nameSpaces) {
            if (c.getName().equals(namespaceName)) {
                return c;
            }
        }
        return null;
    }

    @Nullable
    public static ArrayList<configNameSpace> put(String arg) {
//        System.out.println("loading argument = " + arg);
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
                    valueType = boolean.class;
                    if (arg.split("\\=")[1].contains("true")) {
                        value = true;
                    } else {
                        value = false;
                    }
                    break;
                case 's':
                    valueType = "".getClass();
                    value = arg.split("\\=")[1];
                    break;
            }

            //
            boolean didFindHome = false;
            for (configNameSpace ns : nameSpaces) {
                if (ns.getName().equals(nameSpace)) {
                    //there is a namespace that fits the bill, we should add the entry to that namespace
                    ns.addEntry(keyname, valueType, value);
                    didFindHome = true;
                }
            }
            if (!didFindHome) {
                configNameSpace newSpace = new configNameSpace(nameSpace);
                newSpace.addEntry(keyname, valueType, value);
                nameSpaces.add(newSpace);
            }
        }

        //
        addPublicEntries();

        return nameSpaces;
    }

    private static void addPublicEntries() {
        for (configNameSpace space : nameSpaces) {
            if (space.getName().equals("*")) {
                for (configNameSpace space2 : nameSpaces) {
                    space2.addEntries(space.entries);
                }
            }
        }
    }
}
