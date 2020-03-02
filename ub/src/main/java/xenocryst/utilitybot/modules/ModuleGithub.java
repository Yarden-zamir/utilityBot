package xenocryst.utilitybot.modules;

import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import xenocryst.utilitybot.moduleSystem.config.ConfigNameSpace;
import xenocryst.utilitybot.moduleSystem.modules.Module;
import xenocryst.utilitybot.moduleSystem.modules.moduleVisibility;

public class ModuleGithub implements Module {
	public GitHubClient gitHubClient;
	RepositoryId gitHubRepo;

	@Override
	public Module loadModule(ConfigNameSpace cfg) {
		gitHubClient = new GitHubClient().setCredentials(
				cfg.getEntry("username", "").toString(),
				cfg.getEntry("password", "").toString()
		);
		gitHubRepo = new RepositoryId(
				cfg.getEntry("repoOwner", "").toString(),
				cfg.getEntry("repo", "").toString()
		);
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
