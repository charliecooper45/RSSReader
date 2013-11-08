package gui;

import java.awt.BorderLayout;

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
	private JLabel titleLabel;
	private JList<RSSMessageBean> messageList;
	private DefaultListModel<RSSMessageBean> rssModel;
	
	public RSSFeedList() {
		setup();
	}
	
	private void setup() {
		setLayout(new BorderLayout());
		titleLabel = new JLabel();
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		add(titleLabel, BorderLayout.NORTH);
		messageList = new JList<>();
		messageList.setBorder(BorderFactory.createEtchedBorder());
		rssModel = new DefaultListModel<>();
		messageList.setModel(rssModel);
		add(messageList, BorderLayout.CENTER);
		setBorder(BorderFactory.createEtchedBorder());
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
		for(int i = 0; i < rssFeed.getMessages().size(); i++) {
			rssModel.addElement(rssFeed.getMessages().get(i));
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return rssFeed.getTitle();
	}
		
	
}
