package gui;

import java.net.URL;

/**
 * Listens for events involving the RSS feeds 
 * @author Charlie
 *
 */
public interface RSSEventListener {
	public void rssFeedAdded(URL url);
	public void rssFeedRemoved(RSSFeedList rssFeedList);
}
