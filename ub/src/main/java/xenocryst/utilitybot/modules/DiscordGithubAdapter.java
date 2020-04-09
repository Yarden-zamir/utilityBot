package xenocryst.utilitybot.modules;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.jetbrains.annotations.NotNull;
import xenocryst.utilitybot.moduleSystem.config.ConfigNameSpace;
import xenocryst.utilitybot.moduleSystem.modules.Module;
import xenocryst.utilitybot.moduleSystem.modules.ModuleManager;
import xenocryst.utilitybot.moduleSystem.modules.moduleVisibility;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class DiscordGithubAdapter implements Module {
	ModuleDiscord moduleDiscord;
	ModuleGithub moduleGithub;

	GitHubClient gitHubClient;
	RepositoryId gitHubRepo;
	IssueService issueService;

	@Override
	public boolean enabled() {
		return false;
	}

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
		RepositoryService s = new RepositoryService(gitHubClient);
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

	private String commandChannelCommandWrap;
	private ArrayList<String> blackListCommandChannels;
	private String commandPrefix = "/";
	private String issuePrefix = "!";
	private ConfigNameSpace cfg;
	private DiscordGithubAdapter adapter;
	private String issueChannelNewIssuePrefix;
	private String issueIDIdentifierPrefix;
	private IssueService issueService;
	private GitHubClient gitHubClient;
	private RepositoryId repo;
	private HashMap<User, String> userReferanceMap = new HashMap<>();


	public MessageListener(ArrayList<String> blackListCommandChannels, ConfigNameSpace cfg, DiscordGithubAdapter adapter) {

		this.issueChannelNewIssuePrefix = String.valueOf(cfg.getEntry("issueChannelNewIssuePrefix", "!"));
		this.issueIDIdentifierPrefix = String.valueOf(cfg.getEntry("issueIDIdentifierPrefix", "#"));
		this.commandChannelCommandWrap = String.valueOf(cfg.getEntry("commandChannelCommandWrap", "---"));
		this.blackListCommandChannels = blackListCommandChannels;
		this.cfg = cfg;
		this.adapter = adapter;
		this.gitHubClient = adapter.gitHubClient;
		issueService = new IssueService(gitHubClient);
		repo = new RepositoryId(
				String.valueOf(cfg.getEntry("repoOwner", "#")),
				String.valueOf(cfg.getEntry("repo", "#"))
		);
	}


	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		if (!blackListCommandChannels.contains(event.getChannel())) {
			tryCommand(event.getMessage());
		}
	}

	private String parseTopicCommandShortcuts(String topic, String commandStr, String message) {
		if (topic.split(commandChannelCommandWrap)[1].length() > 2) {//is correctly formatted with start and end wrap
			commandStr = topic.split(commandChannelCommandWrap)[1];
			for (String line : commandStr.split("\n")) {
				if (line.contains("=")) {
					String prefix = line.split("=")[0];
					String command = line.split("=")[1];
					//go through lines and check for trigger
					//first line that triggers should be extracted;
					if (message.startsWith(prefix)) {
						commandStr = command;
						message = message.replaceFirst(prefix, "");
						break;
					}
				}
			}
			if (commandStr.contains("[message]")) {//todo replace this with containes element from element list which containes message;
				commandStr = commandStr.replaceAll("\\[message\\]", message);
			}
		}
		return commandStr;
	}

	private void tryCommand(@NotNull Message message) {
		//see if a command should be run
		String commandStr = message.getContentDisplay();
		if (message.getTextChannel().getTopic() != null)
			if (message.getTextChannel().getTopic().contains(commandChannelCommandWrap)) {
				//see if we are in a command channel
				commandStr = parseTopicCommandShortcuts(message.getTextChannel().getTopic(), commandStr, message.getContentDisplay());
			}
		if (commandStr.startsWith(commandPrefix)) {
			//see if we are using a command prefix
			for (Method m : getClass().getDeclaredMethods()) {
				if (m.isAnnotationPresent(Runnable.class)) {
					String commandName = commandStr.split(commandPrefix)[1].split(" ")[0];
					for (String possibleCommandEvo : m.getAnnotation(Runnable.class).command()) {
						if (possibleCommandEvo.equalsIgnoreCase(commandName)) {
							System.out.println("Running command " + commandStr);
							//see if the command exists
							try {
								m.invoke(this, message, commandStr.replaceFirst(commandPrefix + commandName, "").trim());
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @param m
	 * @return The github username of the person that wrote the message, if none are in the database then it will return the discord name
	 */
	private String getUser(Message m) {
		String user;
		if (userReferanceMap.containsKey(m.getAuthor())) {
			user = userReferanceMap.get(m.getAuthor());
		} else {
			user = m.getMember().getNickname();
			if (user == null)
				user = m.getAuthor().getName();
		}
		return user;
	}

	@Runnable(command = "test")
	private void testCommand(Message m, String commandStr) {
		System.out.println("Test command ran " + commandStr);
	}


	@Runnable(command = {"issue", "newIssue", "addIssue", "postIssue", "report"})
	private void addIssue(Message issueMessage, String commandStr) {
		System.out.println("Added issue!");
		try {
			///issue potato
			String message = issueMessage.getContentDisplay();
			issueService.createIssue(repo, new Issue()
					.setTitle(message.split("\n")[0])
					.setBody(
							"@" + getUser(issueMessage) + ": " +
									message.split("\n")[0]));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
