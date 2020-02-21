package xenocryst.utilitybot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xenocryst.utilitybot.config.configNameSpace;
import xenocryst.utilitybot.modules.module;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.util.ArrayList;

public class discordGithubAdapter implements module {

    public module loadModule(configNameSpace cfg) {
        String token = cfg.getEntry("token","nothing").getValue().toString();
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

class MessageListener extends ListenerAdapter{

	public MessageListener(ArrayList<String> blackListCommandChannels) {

	}

	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
	}
}