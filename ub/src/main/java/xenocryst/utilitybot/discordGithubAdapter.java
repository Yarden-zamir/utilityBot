package xenocryst.utilitybot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import xenocryst.utilitybot.config.configNameSpace;
import xenocryst.utilitybot.modules.module;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.util.ArrayList;

public class discordGithubAdapter implements module {

	JDA adapter;
	GitHubClient gitHubClient;
	RepositoryId gitHubRepo;
	IssueService issueService;
	public module loadModule(configNameSpace cfg) {
		initGithub(
				cfg.getEntry("username").toString(),
				cfg.getEntry("password").toString(),
				cfg.getEntry("repoOwner").toString(),
				cfg.getEntry("repo").toString()
		);
		initDiscord(
				cfg.getEntry("token").toString()
		);

		MessageListener ml = new MessageListener(new ArrayList<>(), "/",this);
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

	private void initGithub(String username, String password, String owner, String repo) {
		gitHubClient = new GitHubClient().setCredentials(username, password);
		gitHubRepo = new RepositoryId(owner, repo);
		issueService = new IssueService(gitHubClient);
	}
}

class MessageListener extends ListenerAdapter {

	private ArrayList<String> blackListCommandChannels;
	private String commandPrefix;
	private discordGithubAdapter adapter;

	public MessageListener(ArrayList<String> blackListCommandChannels, String commandPrefix,discordGithubAdapter adapter) {

		this.blackListCommandChannels = blackListCommandChannels;
		this.commandPrefix = commandPrefix;
		this.adapter = adapter;
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