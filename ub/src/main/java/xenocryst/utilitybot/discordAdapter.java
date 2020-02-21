package xenocryst.utilitybot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import xenocryst.utilitybot.config.configNameSpace;
import xenocryst.utilitybot.modules.module;

import javax.security.auth.login.LoginException;

public class discordAdapter implements module {

    public module loadModule(configNameSpace cfg) {
        System.out.println("Was called");
        String token =
		        cfg.getEntry("token","nothing")
		        .getValue()
		        .toString();
	    System.out.println(token);
        try {
            JDA adapter = new JDABuilder(token).setActivity(Activity.watching("You")).build();
        } catch (LoginException e) {
            //login error
            e.printStackTrace();
        }
        return this;
    }
}
