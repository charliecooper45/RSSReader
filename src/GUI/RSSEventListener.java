package gui;

import java.net.URL;

/**
 * Listens for events involving the RSS feeds (either new additions or deletions or old ones).
 * @author Charlie
 *
 */
public interface RSSEventListener {
	public void rssFeedAdded(URL url);
}
