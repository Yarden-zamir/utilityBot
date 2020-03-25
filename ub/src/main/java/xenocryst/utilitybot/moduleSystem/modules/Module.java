package xenocryst.utilitybot.moduleSystem.modules;

import xenocryst.utilitybot.moduleSystem.config.ConfigNameSpace;
public interface Module {

    /**
     * The init code for the module
     *
     * @param cfg the config category for the given module
     * @return itself
     */
    Module loadModule(ConfigNameSpace cfg) throws Exception;
    int getLoadOrder();
    moduleVisibility getVisibility();
}
