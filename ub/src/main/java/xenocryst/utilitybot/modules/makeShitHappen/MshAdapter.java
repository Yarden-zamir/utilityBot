package xenocryst.utilitybot.modules.makeShitHappen;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdateNameEvent;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CollaboratorService;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.jetbrains.annotations.Nullable;
import xenocryst.utilitybot.moduleSystem.config.ConfigNameSpace;
import xenocryst.utilitybot.moduleSystem.modules.Module;
import xenocryst.utilitybot.moduleSystem.modules.ModuleManager;
import xenocryst.utilitybot.moduleSystem.modules.moduleVisibility;
import xenocryst.utilitybot.modules.ModuleDiscord;
import xenocryst.utilitybot.modules.ModuleGithub;

import java.io.IOException;
import java.util.List;

/**
 * This module will connect events in discord and github together acording to the MSH design language
 */
public class MshAdapter implements Module {
	ModuleDiscord moduleDiscord;
	ModuleGithub moduleGithub;

	GitHubClient gitHubClient;
	RepositoryId gitHubRepo;
	IssueService issueService;
	RepositoryService repositoryService;
	CollaboratorService collaboratorService;

	@Override
	public Module loadModule(ConfigNameSpace cfg) throws Exception {
		moduleDiscord = (ModuleDiscord) ModuleManager.getModule(ModuleDiscord.class.getSimpleName());
		moduleGithub = (ModuleGithub) ModuleManager.getModule(ModuleGithub.class.getSimpleName());

		gitHubClient = moduleGithub.gitHubClient;
//		gitHubRepo = new RepositoryId(
//				cfg.getEntry("repoOwner", "").toString(),
//				cfg.getEntry("repo", "").toString()
//		);
//		MessageListener ml = new MessageListener(new ArrayList<>(), cfg, this);
		ListenerAdapterInstance ml = new ListenerAdapterInstance(this);
		moduleDiscord.adapter.addEventListener(ml);
		{

		}
		repositoryService = new RepositoryService(gitHubClient);
		collaboratorService = new CollaboratorService(gitHubClient);
		issueService = new IssueService(gitHubClient);

		//


		return this;
	}

	@Override
	public int getLoadOrder() {
		return 1;
	}

	@Override
	public moduleVisibility getVisibility() {
		return null;
	}


	public Repository openCluster(String clusterName) throws IOException {
		Repository repository = getLiveCluster(clusterName);
		if (repository == null) {
			repository = new Repository().setHasIssues(true).setPrivate(true);
			repository.setName(clusterName);
			repositoryService.createRepository(repository);
			repository = getLiveCluster(repository.getName());
			collaboratorService.addCollaborator(repository, "PandaBoy444");
		} else {
		}
		validateTerminal(clusterName);
		//check if has a terminal, and if not, create one

		return repository;
	}

	private Issue retrieveIssue(Repository repo, String issueName) throws IOException {
		issueName = issueName.replaceAll("-", " ");
		for (Issue I:issueService.getIssues(repo, null)){
			if (I.getTitle().equalsIgnoreCase(issueName)){
				return I;
			}
		}
		return null;
	}
	public Issue addEntryToIssue(String repoName,String issueName,String entry) throws IOException {
		Repository repo = openCluster(repoName);
		issueService.createComment(repo, retrieveIssue(repo, issueName).getNumber() ,entry );
		return null;
	}
	private void validateTerminal(String clusterName) {
		List<TextChannel> terminals;
		terminals = moduleDiscord.adapter.getTextChannelsByName("___terminal___", true);
		boolean hasTerminal = false;
		for (TextChannel terminal : terminals) {
			if (terminal.getParent().getName().equalsIgnoreCase(clusterName)) {
				hasTerminal = true;
			}
		}
		if (!hasTerminal) {
			//create terminal
			
		}
	}

	@Nullable
	public Repository getLiveCluster(String clusterName) throws IOException {
		for (Repository r : repositoryService.getRepositories()) {
			if (r.getName().equalsIgnoreCase(clusterName)) {
				return r;
			}
		}
		return null;
	}

	public void removeCluster(CategoryDeleteEvent event) throws IOException {

		for (Repository r : repositoryService.getRepositories()) {
			if (r.getName().equals(event.getCategory().getName())) {
				for (TextChannel textChannel : event.getCategory().getTextChannels()) {
					textChannel.delete();
					System.out.println("removed " + textChannel.getName());
				}
			}
		}
	}

	public Repository changeClusterName(CategoryUpdateNameEvent event) {
		System.out.println("Renamed");
		getRepoFromName(event.getOldName()).setName(event.getNewName());
		return null;
	}

	public Issue addIssue(Repository cluster, String issueName, String Entry) throws IOException {
		issueName = issueName.replaceAll("-", " ");
		Issue I = new Issue().setTitle(issueName).setBody(Entry);
		issueService.createIssue(cluster, I);
		return null;
	}

	private Repository getRepoFromName(String name) {
		try {
			for (Repository r : repositoryService.getRepositories()) {
				if (r.getName().equals(name)) {
					return r;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}

