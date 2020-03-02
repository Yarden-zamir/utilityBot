package xenocryst.utilitybot.modules;

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

		gitHubClient=moduleGithub.gitHubClient;
		gitHubRepo=moduleGithub.gitHubRepo;
		MessageListener ml = new MessageListener(new ArrayList<>(), "/", this);
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
	private String commandPrefix;
	private DiscordGithubAdapter adapter;

	public MessageListener(ArrayList<String> blackListCommandChannels, String commandPrefix, DiscordGithubAdapter adapter) {

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
		System.out.println("Added issue");
	}
}