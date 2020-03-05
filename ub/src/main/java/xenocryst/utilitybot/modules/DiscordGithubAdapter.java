package xenocryst.utilitybot.modules;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import xenocryst.utilitybot.moduleSystem.config.ConfigNameSpace;
import xenocryst.utilitybot.moduleSystem.modules.Module;
import xenocryst.utilitybot.moduleSystem.modules.ModuleManager;
import xenocryst.utilitybot.moduleSystem.modules.moduleVisibility;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class DiscordGithubAdapter implements Module {
	ModuleDiscord moduleDiscord;
	ModuleGithub moduleGithub;

	GitHubClient gitHubClient;
	RepositoryId gitHubRepo;
	IssueService issueService;


	@Override
	public Module loadModule(ConfigNameSpace cfg) {
		moduleDiscord = (ModuleDiscord) ModuleManager.getModule(ModuleDiscord.class.getSimpleName());
		moduleGithub = (ModuleGithub) ModuleManager.getModule(ModuleGithub.class.getSimpleName());

		gitHubClient = moduleGithub.gitHubClient;
		gitHubRepo = new RepositoryId(
				cfg.getEntry("repoOwner", "").toString(),
				cfg.getEntry("repo", "").toString()
		);
		MessageListener ml = new MessageListener(new ArrayList<>(), cfg, this);
		moduleDiscord.adapter.addEventListener(ml);
		return this;

	}

	@Override
	public int getLoadOrder() {
		return 1;
	}

	@Override
	public moduleVisibility getVisibility() {
		return moduleVisibility.PRIVATE;
	}
}

class MessageListener extends ListenerAdapter {

	private ArrayList<String> blackListCommandChannels;
	private String commandPrefix = "/";
	private String issuePrefix = "!";
	private ConfigNameSpace cfg;
	private DiscordGithubAdapter adapter;
	private String issueChannelNewIssuePrefix;
	private String issueIDIdentifierPrefix;
	private IssueService issueService;
	private GitHubClient gitHubClient;

	public MessageListener(ArrayList<String> blackListCommandChannels, ConfigNameSpace cfg, DiscordGithubAdapter adapter) {

		this.issueChannelNewIssuePrefix = String.valueOf(cfg.getEntry("issueChannelNewIssuePrefix", "!"));
		this.issueIDIdentifierPrefix = String.valueOf(cfg.getEntry("issueIDIdentifierPrefix", "#"));
		this.blackListCommandChannels = blackListCommandChannels;
		this.cfg = cfg;
		this.adapter = adapter;
		this.gitHubClient=adapter.gitHubClient;
		issueService = new IssueService(gitHubClient);
	}

	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		if (event.getMessage().getContentRaw().startsWith(commandPrefix)) {//universal commands
			if (!blackListCommandChannels.contains(event.getChannel())) {
				//run event
				runCommand(event.getMessage().getContentRaw().replaceFirst(commandPrefix, ""));
			}
		}
		if (event.getChannel().getParent().getName().equals("Milestones")) { //add an issue to milestones
			String milestone = event.getChannel().getName();

		}
		if (event.getChannel().getName().equalsIgnoreCase("issues")) {
			handleIssue(event.getMessage());
		}

	}

	private void handleIssue(Message m) {
		if (m.getContentDisplay().startsWith(issueChannelNewIssuePrefix)) {
			//add issue
			issueService.
		} else if (m.getContentDisplay().startsWith(issueIDIdentifierPrefix)) {
			//comment on issue
		} else {
			//comment on last issue
		}
	}

	private void runCommand(String command) {
		if (command.startsWith("issue")) {
			addIssue(command.replaceFirst("issue", ""));
		}
	}

	private void addIssue(String issue) {
		System.out.println("Added issue");
	}
}