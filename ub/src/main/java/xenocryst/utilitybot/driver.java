package xenocryst.utilitybot;


import org.reflections.Reflections;
import xenocryst.utilitybot.config.configManager;
import xenocryst.utilitybot.config.configNameSpace;
import xenocryst.utilitybot.modules.module;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Stream;

public class driver {

	public static void main(String[] args) {
		for (String arg : args) configManager.put(arg);
		try (Stream<String> stream = Files.lines(Paths.get("config.args"))) {
			stream.forEach(System.out::println);
			stream.forEach( line -> configManager.put(line));
		} catch (IOException e) {
			e.printStackTrace();
		}
		loadModules();
	}

	public static void loadModules() {
		Reflections ref = new Reflections("xenocryst");
		Set<Class<? extends module>> modules = ref.getSubTypesOf(module.class);
		for (Class m : modules)
			try {
				Method method = m.getDeclaredMethod("loadModule", configNameSpace.class);
				method.invoke(m.newInstance(), configManager.retrieveConfig(m));
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
	}
}

