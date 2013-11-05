package GUI;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * Displays the list of news stories for an RSS feed in a JList
 * @author Charlie
 *
 */
@SuppressWarnings("serial")
public class RSSFeedList extends JPanel {
	private JList<String> feedList;
	private DefaultListModel<String> rssModel;
	private static final int NUMBER_OF_FEEDS = 10;
	
	public RSSFeedList() {
		feedList = new JList<>();
		feedList.setBorder(BorderFactory.createEtchedBorder());
		rssModel = new DefaultListModel<>();
		feedList.setModel(rssModel);
		add(feedList);
	}
	
	/**
	 * Sets the rss feed titles to be displayed in the list
	 * @param titles to be displayed
	 */
	public void setRSSTitles(List<String> titles) {
		rssModel.clear();
		
		// Add all the list data
		for(int i = 0; i < NUMBER_OF_FEEDS; i++) {
			rssModel.addElement(titles.get(i));
		}
	}
}
