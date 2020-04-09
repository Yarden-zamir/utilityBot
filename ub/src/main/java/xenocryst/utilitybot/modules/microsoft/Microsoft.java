package xenocryst.utilitybot.modules.microsoft;

import com.microsoft.graph.models.extensions.User;
import xenocryst.utilitybot.moduleSystem.config.ConfigNameSpace;
import xenocryst.utilitybot.moduleSystem.modules.Module;
import xenocryst.utilitybot.moduleSystem.modules.moduleVisibility;

public class Microsoft implements Module {
	@Override
	public Module loadModule(ConfigNameSpace cfg) throws Exception {

		if (false){
			String appid = "8d9cecd1-c136-4566-85e6-f998cce1a35f";
			String[] appscopes = new String[]{"User.Read", "Tasks.Read", "Tasks.Read.Shared", "Tasks.ReadWrite", "Tasks.ReadWrite.Shared"};
			Authentication.initialize(appid);
			String token = Authentication.getUserAccessToken(appscopes);
			User user = graph.getUser(token);
			graph.test();
			return null;
		}
		return this;
	}

	@Override
	public int getLoadOrder() {
		return -2;
	}

	@Override
	public moduleVisibility getVisibility() {
		return null;
	}
}
