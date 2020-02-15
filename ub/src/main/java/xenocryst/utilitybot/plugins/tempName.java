package xenocryst.utilitybot.plugins;

import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import xenocryst.utilitybot.config.configNameSpace;
import xenocryst.utilitybot.modules.module;

public class tempName extends Plugin {

    /**
     * Constructor to be used by plugin manager for plugin instantiation.
     * Your plugins have to provide constructor with this exact signature to
     * be successfully loaded by manager.
     *
     * @param wrapper
     */
    public tempName(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class githubBase implements module {

        @Override
        public module loadModule(configNameSpace cfg) {
            return this;
        }
    }
}
