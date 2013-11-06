package gui;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.xml.stream.XMLStreamException;

import core.RSSCoord;
import core.RSSFeedBean;

// TODO NEXT: Observerable? Use the Observer patter to keep RSSFeedLists updated
@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	private RSSCoord coord = RSSCoord.getInstance();
	private Map<PanelType, JDialog> dialogs;
	private Toolbar toolbar;
	private RSSFeedList list;

	//TODO NEXT: GridLayout for RSSFeedLists

	public MainFrame() {
		super("RSS Reader");
		dialogs = new EnumMap<>(PanelType.class);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setup();
	}

	private void setup() {
		// Setup the different panels that are displayed on the screen
		AddRSSDialog addDialog = new AddRSSDialog(this);
		addDialog.setRSSEventListener(new RSSEventListener() {
			
			@Override
			public void rssFeedAdded(URL url) {
				System.out.println("Feed added! " + url);
			}
		});
		dialogs.put(PanelType.ADD_RSS_FEED, addDialog);

		toolbar = new Toolbar();
		toolbar.setDialogShownListener(new DialogShownListener() {

			@Override
			public void dialogShown() {
				dialogs.get(PanelType.ADD_RSS_FEED).setVisible(true);
			}
		});

		//TODO NEXT: Add this to a SwingWorker thread
		list = new RSSFeedList();
		try {
			RSSFeedBean feedBean = coord.loadRSSFeed(new URL("http://www.drdobbs.com/articles/jvm/rss"));
			list.setRssFeed(feedBean);
			list.updateRSSMessages();
		} catch (IOException | XMLStreamException e) {
			e.printStackTrace();
		}
		
		add(toolbar, BorderLayout.NORTH);
		add(list, BorderLayout.CENTER);
	}
}
