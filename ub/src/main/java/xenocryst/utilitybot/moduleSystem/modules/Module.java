package xenocryst.utilitybot.moduleSystem.modules;

import xenocryst.utilitybot.moduleSystem.config.ConfigNameSpace;
public interface Module {

    /**
     * The init code for the module
     *
     * @param cfg the config category for the given module
     * @return itself
     */
    public Module loadModule(ConfigNameSpace cfg) throws Exception;
    public int getLoadOrder();
    public moduleVisibility getVisibility();
}
