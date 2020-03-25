package xenocryst.utilitybot.modules;

import com.google.gson.JsonObject;
import com.microsoft.graph.logger.DefaultLogger;
import com.microsoft.graph.logger.LoggerLevel;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.User;
import com.microsoft.graph.requests.extensions.GraphServiceClient;

public class graph {
	private static IGraphServiceClient graphClient = null;
	private static SimpleAuthProvider authProvider = null;

	private static void ensureGraphClient(String accessToken) {
		if (graphClient == null) {
			// Create the auth provider
			authProvider = new SimpleAuthProvider(accessToken);

			// Create default logger to only log errors
			DefaultLogger logger = new DefaultLogger();
			logger.setLoggingLevel(LoggerLevel.ERROR);

			// Build a Graph client
			graphClient = GraphServiceClient.builder()
					.authenticationProvider(authProvider)
					.logger(logger)
					.buildClient();
		}

	}

	public static User getUser(String accessToken) {
		ensureGraphClient(accessToken);
		// GET /me to get authenticated user
		User me = graphClient
				.me()
				.buildRequest()
				.get();
		return me;
	}
	public static void test(){
		graphClient.setServiceRoot("https://graph.microsoft.com/beta");
		JsonObject s = graphClient.customRequest("/me/outlook/tasks").buildRequest().get();
		System.out.println(s.get("value").toString());
	}
}
