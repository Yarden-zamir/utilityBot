package xenocryst.utilitybot.modules;

import xenocryst.utilitybot.config.configNameSpace;

public interface module{

    /**
     * The init code for the module
     *
     * @param cfg the config category for the given module
     * @return itself
     */
    public module loadModule(configNameSpace cfg);
}
