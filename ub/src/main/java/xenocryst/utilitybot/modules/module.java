package xenocryst.utilitybot.modules;

import org.pf4j.ExtensionPoint;
import xenocryst.utilitybot.config.configNameSpace;

public interface module extends ExtensionPoint {

    /**
     * The init code for the module
     *
     * @param cfg the config category for the given module
     * @return itself
     */
    public module loadModule(configNameSpace cfg);
}
