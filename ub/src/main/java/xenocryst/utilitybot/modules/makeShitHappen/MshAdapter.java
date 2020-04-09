package xenocryst.utilitybot.modules.makeShitHappen;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CollaboratorService;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.RepositoryService;
import xenocryst.utilitybot.moduleSystem.config.ConfigNameSpace;
import xenocryst.utilitybot.moduleSystem.modules.Module;
import xenocryst.utilitybot.moduleSystem.modules.ModuleManager;
import xenocryst.utilitybot.moduleSystem.modules.moduleVisibility;
import xenocryst.utilitybot.modules.ModuleDiscord;
import xenocryst.utilitybot.modules.ModuleGithub;

import javax.annotation.Nonnull;
import java.io.IOException;

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

	public Repository createNewCluster(CategoryCreateEvent event) throws IOException {
		Repository repository = new Repository().setHasIssues(true).setPrivate(true);
		repository.setName(event.getCategory().getName());
		repositoryService.createRepository(repository);

		for (Repository r : repositoryService.getRepositories()) {
			if (r.getName().equals(repository.getName())) {
				collaboratorService.addCollaborator(r, "PandaBoy444");
			}
		}
		return null;
	}

	public void removeCluster(CategoryDeleteEvent event) throws IOException{
		for (Repository r : repositoryService.getRepositories()) {
			if (r.getName().equals(event.getCategory().getName())){
				for (TextChannel textChannel:event.getCategory().getTextChannels()){
					textChannel.delete();
				}
			}
		}
	}
	public Repository changeClusterName(CategoryUpdateNameEvent event){
		return null;
	}

}

class ListenerAdapterInstance extends ListenerAdapter {

	private MshAdapter adapter;

	public ListenerAdapterInstance(MshAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public void onCategoryDelete(@Nonnull CategoryDeleteEvent event) {
		try {
			adapter.removeCluster(event);
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.onCategoryDelete(event);
	}

	@Override
	public void onCategoryUpdateName(@Nonnull CategoryUpdateNameEvent event) {
		adapter.changeClusterName(event);
		super.onCategoryUpdateName(event);
	}

	@Override
	public void onCategoryCreate(@Nonnull CategoryCreateEvent event) {
		try {
			adapter.createNewCluster(event);
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.onCategoryCreate(event);
	}
}
