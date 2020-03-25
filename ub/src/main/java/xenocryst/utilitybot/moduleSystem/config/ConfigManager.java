package xenocryst.utilitybot.moduleSystem.config;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;

public class ConfigManager {
	public static ArrayList<ConfigNameSpace> nameSpaces = new ArrayList<ConfigNameSpace>();

	public static ConfigNameSpace retrieveConfig(String namespaceName) {
		for (ConfigNameSpace c : nameSpaces) {
			if (c.getName().equals(namespaceName)) {
				return c;
			}
		}
		System.out.println("Cannot find namespace " + namespaceName + " creating one");
		ConfigNameSpace newNameSpace = new ConfigNameSpace(namespaceName);
		nameSpaces.add(newNameSpace);
		return newNameSpace;
	}

	public static ConfigNameSpace retrieveConfig(Class c) {
		return retrieveConfig(c.getSimpleName());
	}

	public static void put(String[] args) {
		for (String arg : args) put(arg);
	}

	public static void put(Path configFile) {
		try (Stream<String> stream = Files.lines(configFile)) {
			stream.forEach(line -> put(line));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void put(String namespace, String keyname, Object value) {
		retrieveConfig(namespace).addEntry(keyname, value);
	}

	public static ArrayList<ConfigNameSpace> put(String arg) {
//        System.out.println("loading argument = " + arg);
		String nameSpace = null;
		String keyname = null;
		Object value = "";

		//todo validate
		String[] split = arg.split("\\=");

		keyname = split[0].split(":")[1];
		nameSpace = split[0].split(":")[0];
		if (split.length > 1)
			value = split[1];
		put(nameSpace, keyname, value);

		//
		addPublicEntries();

		return nameSpaces;
	}

	private static void addPublicEntries() {
		for (ConfigNameSpace space : nameSpaces) {
			if (space.getName().equals("*")) {
				for (ConfigNameSpace space2 : nameSpaces) {
					space2.addEntries(space.entries);
				}
			}
		}
	}
}
