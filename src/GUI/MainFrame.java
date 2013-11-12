package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingWorker;
import javax.xml.stream.XMLStreamException;

import core.RSSCoord;
import core.RSSFeedBean;
import core.RSSMessageBean;

// TODO NEXT: Observerable? Use the Observer pattern to keep RSSFeedLists updated - refresh functionality
@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	private RSSCoord coord = RSSCoord.getInstance();
	private Toolbar toolbar;
	private JSplitPane mainSplitPane;
	private RSSFeedsPanel rssFeedsPanel;
	private RSSViewerPanel rssReaderPanel;
	private List<RSSFeedBean> feedBeans;

	public MainFrame() {
		super("RSS Reader");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				List<RSSFeedBean> feeds = new ArrayList<>();
				for (RSSFeedList list : rssFeedsPanel.rssFeeds) {
					feeds.add(list.getRssFeed());
				}
				try {
					coord.saveSession(feeds);
				} catch (IOException exception) {
					JOptionPane.showMessageDialog(MainFrame.this, "Could not save session data", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		setup();
	}

	private void setup() {
		// Setup the toolbar
		final RSSEventListener rssEventListener = new RSSEventListener() {
			@Override
			public void rssFeedAdded(URL url) {
				rssFeedsPanel.addNewRSSFeed(url);
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
				if (type == DialogType.ADD_RSS_FEED) {
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
		
		// Setup the different panels that are displayed on the screen
		try {
			feedBeans = coord.loadSession();
		} catch (ClassNotFoundException | IOException e1) {
			JOptionPane.showMessageDialog(MainFrame.this, "Could not load session data", "Error", JOptionPane.ERROR_MESSAGE);
		}
		rssFeedsPanel = new RSSFeedsPanel();
		if (feedBeans != null) 
			rssFeedsPanel.setRSSFeed(feedBeans);

		rssReaderPanel = new RSSViewerPanel();
		mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rssFeedsPanel, rssReaderPanel);
		mainSplitPane.setOneTouchExpandable(true);
		add(mainSplitPane, BorderLayout.CENTER);
	}

	private class RSSFeedsPanel extends JPanel {
		//TODO NEXT B: Right click -> mark as unread 
		//TODO NEXT: read some code
		private List<RSSFeedList> rssFeeds;
		private RSSMessageSelectedListener rssMessageSelectedListener;

		public RSSFeedsPanel() {
			rssFeeds = new ArrayList<>();
			rssMessageSelectedListener = new RSSMessageSelectedListener() {
				@Override
				public void messageRead(RSSMessageBean bean) {
					rssReaderPanel.loadURL(bean.getLink());
				}
			};
			setLayout(new GridLayout(3, 4));
			setBackground(Color.green);
			setMinimumSize(new Dimension(400, 0));
		}

		/**
		 * If there are existing RSS messages (some of which may be read), these are loaded into the RSSFeedList.
		 * @param feeds
		 */
		public void setRSSFeed(List<RSSFeedBean> feeds) {
			for(RSSFeedBean bean : feeds) {
				addRSSFeedList(bean);
			}
		}

		private void addNewRSSFeed(final URL url) {
			SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
				RSSFeedBean feedBean = null;

				@Override
				protected void done() {
					try {
						Boolean feedLoaded = get();
						if (feedLoaded == true) {
							addRSSFeedList(feedBean);
						}
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}

				@Override
				protected Boolean doInBackground() throws Exception {
					try {
						feedBean = coord.loadRSSFeed(url);
						return true;
					} catch (XMLStreamException e) {
						JOptionPane.showMessageDialog(MainFrame.this, "RSS feed cannot be loaded", "Error", JOptionPane.ERROR_MESSAGE);
						return false;
					}
				}
			};
			worker.execute();
		}
		
		private void addRSSFeedList(RSSFeedBean feedBean) {
			RSSFeedList rssFeedList = new RSSFeedList();
			rssFeedList.setRssFeed(feedBean);
			rssFeedList.updateRSSMessages();
			rssFeedsPanel.add(rssFeedList);
			rssFeeds.add(rssFeedList);
			rssFeedList.setRssMessageSelectedListener(rssMessageSelectedListener);
			toolbar.setRemoveButtonEnabled(true);

			if (rssFeeds.size() > 3) {
				setMinimumSize(new Dimension(800, 0));
				mainSplitPane.setDividerLocation(getMinimumSize().width);
			}

			MainFrame.this.revalidate();
			MainFrame.this.repaint();
		}

		private void removeRSSFeed(RSSFeedList rssFeedList) {
			rssFeeds.remove(rssFeedList);
			remove(rssFeedList);

			if (rssFeeds.isEmpty()) {
				toolbar.setRemoveButtonEnabled(false);
			}
			MainFrame.this.revalidate();
			MainFrame.this.repaint();
		}
	}

	private class RSSViewerPanel extends JPanel {
		private JFXPanel jfxPanel;
		private WebEngine engine;

		public RSSViewerPanel() {
			setBackground(Color.red);
			setup();
		}

		private void setup() {
			setLayout(new BorderLayout());
			jfxPanel = new JFXPanel();
			createScene();
			add(jfxPanel, BorderLayout.CENTER);
		}

		private void createScene() {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					WebView view = new WebView();
					engine = view.getEngine();
					jfxPanel.setScene(new Scene(view));
				}
			});
		}

		public void loadURL(final String rssURL) {
			try {
				new URL(rssURL);
				// Load the url
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						engine.load(rssURL);
					}
				});
			} catch (MalformedURLException e) {
				JOptionPane.showMessageDialog(MainFrame.this, "Error", "Unable to load RSS link", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
