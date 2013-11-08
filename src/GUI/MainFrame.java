package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingWorker;
import javax.xml.stream.XMLStreamException;

import core.RSSCoord;
import core.RSSFeedBean;

// TODO NEXT: Observerable? Use the Observer patter to keep RSSFeedLists updated
@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	private RSSCoord coord = RSSCoord.getInstance();
	private Toolbar toolbar;
	private JSplitPane mainSplitPane;
	private RSSFeedsPanel rssFeedsPanel;
	//TODO NEXT: Implement the HTML reader panel, reads the RSS feeds
	private JPanel rssReaderPanel;

	public MainFrame() {
		super("RSS Reader");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setup();
	}

	private void setup() {
		// Setup the different panels that are displayed on the screen
		rssFeedsPanel = new RSSFeedsPanel();
		rssReaderPanel = new JPanel();
		rssReaderPanel.setBackground(Color.red);
		mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rssFeedsPanel, rssReaderPanel);
		mainSplitPane.setOneTouchExpandable(true);
		add(mainSplitPane, BorderLayout.CENTER);
		

		// Setup the toolbar
		final RSSEventListener rssEventListener = new RSSEventListener() {
			@Override
			public void rssFeedAdded(URL url) {
				rssFeedsPanel.addRSSFeed(url);
			}

			@Override
			public void rssFeedRemoved(RSSFeedList rssFeedList) {
				rssFeedsPanel.removeRSSFeed(rssFeedList);
			}
		};
		toolbar = new Toolbar();
		toolbar.setDialogShownListener(new DialogShownListener() {

			@Override
			public void dialogShown(DialogType type) {
				if(type == DialogType.ADD_RSS_FEED) {
					AddRSSDialog addDialog = new AddRSSDialog(MainFrame.this);
					addDialog.setRSSEventListener(rssEventListener);
					addDialog.setVisible(true);
				} else {
					RemoveRSSDialog removeDialog = new RemoveRSSDialog(MainFrame.this, rssFeedsPanel.rssFeeds);
					removeDialog.setRSSEventListener(rssEventListener);
					removeDialog.setVisible(true);
				}
			}
		});
		add(toolbar, BorderLayout.NORTH);

	}
	
	private class RSSFeedsPanel extends JPanel {
		private List<RSSFeedList> rssFeeds;
		
		public RSSFeedsPanel() {
			rssFeeds = new ArrayList<>();
			setLayout(new GridLayout(3, 4));
			setBackground(Color.green);
			setMinimumSize(new Dimension(400, 0));
		}
		
		private void addRSSFeed(final URL url) {
			SwingWorker<RSSFeedList, Void> worker = new SwingWorker<RSSFeedList, Void>() {
				RSSFeedBean feedBean = null;
				
				@Override
				protected void done() {
					try {
						RSSFeedList rssFeedList = get();
						if (rssFeedList != null) {
							rssFeedsPanel.add(rssFeedList);
							rssFeeds.add(rssFeedList);
							toolbar.setRemoveButtonEnabled(true);
							
							if(rssFeeds.size() > 3) {
								System.out.println("MORE THAN 2");
								setMinimumSize(new Dimension(800, 0));
								mainSplitPane.setDividerLocation(getMinimumSize().width);
							}
							
							MainFrame.this.revalidate();
							MainFrame.this.repaint();
						}
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}

				@Override
				protected RSSFeedList doInBackground() throws Exception {
					RSSFeedList rssFeedList = new RSSFeedList();
					try {
						feedBean = coord.loadRSSFeed(url);
						rssFeedList.setRssFeed(feedBean);
						rssFeedList.updateRSSMessages();
						return rssFeedList;
					} catch (XMLStreamException e) {
						JOptionPane.showMessageDialog(MainFrame.this, "RSS feed cannot be loaded", "Error", JOptionPane.ERROR_MESSAGE);
					}
					return null;
				}
			};
			worker.execute();
		}
		
		private void removeRSSFeed(RSSFeedList rssFeedList) {
			rssFeeds.remove(rssFeedList);
			remove(rssFeedList);
			
			if(rssFeeds.isEmpty()) {
				toolbar.setRemoveButtonEnabled(false);
			}
			MainFrame.this.revalidate();
			MainFrame.this.repaint();
		}
	}
}
