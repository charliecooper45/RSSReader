package gui;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import core.RSSFeedBean;
import core.RSSMessageBean;

/**
 * Displays the list of news stories for an RSS feed in a JList
 * @author Charlie
 *
 */
@SuppressWarnings("serial")
public class RSSFeedList extends JPanel {
	private RSSFeedBean rssFeed;
	// TODO NEXT: Implement this title label
	private JLabel titleLabel;
	private JList<RSSMessageBean> messageList;
	private DefaultListModel<RSSMessageBean> rssModel;
	private static final int NUMBER_OF_FEEDS = 10;
	
	public RSSFeedList() {
		setup();
	}
	
	private void setup() {
		titleLabel = new JLabel();
		add(titleLabel);
		
		messageList = new JList<>();
		messageList.setBorder(BorderFactory.createEtchedBorder());
		rssModel = new DefaultListModel<>();
		messageList.setModel(rssModel);
		add(messageList);
	}
	
	/**
	 * @param rssFeed the rssFeed to set
	 */
	public void setRssFeed(RSSFeedBean rssFeed) {
		this.rssFeed = rssFeed;
		titleLabel.setText(rssFeed.getTitle());
	}

	/**
	 * Updates the RSS messages displayed in the list
	 */
	public void updateRSSMessages() {
		rssModel.clear();
		
		// Add all the list data
		for(int i = 0; i < NUMBER_OF_FEEDS; i++) {
			rssModel.addElement(rssFeed.getMessages().get(i));
		}
	}
}
