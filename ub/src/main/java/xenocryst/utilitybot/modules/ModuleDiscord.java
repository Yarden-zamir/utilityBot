package xenocryst.utilitybot.modules;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import xenocryst.utilitybot.moduleSystem.config.ConfigNameSpace;
import xenocryst.utilitybot.moduleSystem.modules.Module;
import xenocryst.utilitybot.moduleSystem.modules.moduleVisibility;

import javax.security.auth.login.LoginException;

public class ModuleDiscord implements Module {
	public JDA adapter;
	public Guild guild;

	@Override
	public Module loadModule(ConfigNameSpace cfg) throws Exception{
		String token = cfg.getEntry("token").getStringValue();
		try {
			adapter = new JDABuilder(token).setActivity(Activity.watching("You")).build();
		} catch (LoginException e) {
			e.printStackTrace();
		}
		guild = adapter.getGuildById("663846155179786242");
		return this;
	}

	@Override
	public int getLoadOrder() {
		return 0;
	}

	@Override
	public moduleVisibility getVisibility() {
		return moduleVisibility.PUBLIC;
	}
}
