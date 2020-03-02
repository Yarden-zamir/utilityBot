package xenocryst.utilitybot.moduleSystem.modules;

import org.reflections.Reflections;
import xenocryst.utilitybot.moduleSystem.config.ConfigManager;
import xenocryst.utilitybot.moduleSystem.config.ConfigNameSpace;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

public class ModuleManager {
	public static Comparator<Class> compareLoadOrder = (firstModule, secondModule) -> {

		try {
			return ((Integer) ((Module) firstModule.newInstance()).getLoadOrder()).compareTo(
					(Integer) ((Module) secondModule.newInstance()).getLoadOrder());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return 0;
	};
	private static ArrayList<Module> modules = new ArrayList<>();

	public static void loadModules() {
		Reflections ref = new Reflections("xenocryst");
		Set<Class<? extends Module>> modulesSet = ref.getSubTypesOf(Module.class);
		ArrayList<Class<? extends Module>> modulesArr = new ArrayList<Class<? extends Module>>();
		for (Class<? extends Module> moduleClass : modulesSet) modulesArr.add(moduleClass);
		Collections.sort(modulesArr, compareLoadOrder);
		for (Class moduleClass : modulesArr) {
			System.out.println(moduleClass.getSimpleName() + " is starting");
			try {
				Method method = moduleClass.getDeclaredMethod("loadModule", ConfigNameSpace.class);
				Module m = (Module) moduleClass.newInstance();
				method.invoke(m, ConfigManager.retrieveConfig(moduleClass));
				modules.add(m);
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}

	public static Module getModule(String name) {
		for (Module m : modules) {
			if (m.getClass().getSimpleName().equals(name)) {
				if (m.getVisibility().equals(moduleVisibility.PUBLIC)) {
					return m;
				} else {
					try {
						throw new AccessDeniedException("The module requested is set to PRIVATE");
					} catch (AccessDeniedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		throw new NullPointerException("The module was not found or is set to HIDDEN");
	}
}
