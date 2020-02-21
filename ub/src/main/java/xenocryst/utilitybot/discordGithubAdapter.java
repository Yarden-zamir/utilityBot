package xenocryst.utilitybot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.eclipse.egit.github.core.client.GitHubClient;
import xenocryst.utilitybot.config.configNameSpace;
import xenocryst.utilitybot.modules.module;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.util.ArrayList;

public class discordGithubAdapter implements module {

	JDA adapter;
	GitHubClient gitHubClient;
	public module loadModule(configNameSpace cfg) {
		initDiscord(cfg.getEntry("token", "nothing").toString());
		initGithub(
				cfg.getEntry("username", "nothing").toString(),
				cfg.getEntry("password", "nothing").toString()
		);
		MessageListener ml = new MessageListener(new ArrayList<>(), "/");
		return this;

	}

	private void initDiscord(String token) {
		System.out.println(token);
		try {
			adapter = new JDABuilder(token).setActivity(Activity.watching("You")).build();
		} catch (LoginException e) {
			//login error
			e.printStackTrace();
		}
	}

	private void initGithub(String username, String password) {
		gitHubClient = new GitHubClient().setCredentials(username, password);
	}
}

class MessageListener extends ListenerAdapter {

	private ArrayList<String> blackListCommandChannels;
	private String commandPrefix;

	public MessageListener(ArrayList<String> blackListCommandChannels, String commandPrefix) {

		this.blackListCommandChannels = blackListCommandChannels;
		this.commandPrefix = commandPrefix;
	}

	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		if (event.getMessage().getContentRaw().startsWith(commandPrefix)) {
			if (!blackListCommandChannels.contains(event.getChannel())) {
				//run event
				runCommand(event.getMessage().getContentRaw().replaceFirst(commandPrefix, ""));
			}
		}
	}

	private void runCommand(String command) {
		if (command.startsWith("issue")) {
			addIssue(command.replaceFirst("issue", ""));
		}
	}

	private void addIssue(String issue) {

	}
}