package gui;

import java.net.URL;

/**
<<<<<<< HEAD
 * Listens for events involving the RSS feeds 
=======
 * Listens for events involving the RSS feeds (either additions or deletions).
>>>>>>> 671ea89... third commit
 * @author Charlie
 *
 */
public interface RSSEventListener {
	public void rssFeedAdded(URL url);
	public void rssFeedRemoved(RSSFeedList rssFeedList);
}
