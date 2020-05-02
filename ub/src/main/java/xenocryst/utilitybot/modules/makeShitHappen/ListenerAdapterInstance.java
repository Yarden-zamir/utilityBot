package xenocryst.utilitybot.modules.makeShitHappen;

import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.eclipse.egit.github.core.Repository;

import javax.annotation.Nonnull;
import java.io.IOException;

class ListenerAdapterInstance extends ListenerAdapter {

	private MshAdapter adapter;

	public ListenerAdapterInstance(MshAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public void onCategoryDelete(@Nonnull CategoryDeleteEvent event) {
		if (false)
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
			adapter.openCluster(event.getCategory().getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.onCategoryCreate(event);
	}

	@Override
	public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
		if (event.getChannel().getName().equalsIgnoreCase("___terminal___")) {
			//create issue or run special command
			if (event.getMessage().getContentRaw().equalsIgnoreCase("update")){

			}
		} else {
			//add entry to issue
			try {
				adapter.addEntryToIssue(
						event.getTextChannel().getParent().getName()
						, event.getTextChannel().getName()
						, event.getMessage().getContentRaw()
				);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		super.onMessageReceived(event);
	}

	@Override
	public void onTextChannelCreate(@Nonnull TextChannelCreateEvent event) {
		if (event.getChannel().getName().equalsIgnoreCase("___terminal___")) {

		} else
			try {
				Repository repository = adapter.openCluster(event.getChannel().getParent().getName());
				System.out.println(repository.getName());
				adapter.addIssue(repository, event.getChannel().getName(), "");
			} catch (IOException e) {
				e.printStackTrace();
			}
		super.onTextChannelCreate(event);
	}
}
