package GUI;

import java.io.IOException;
import java.net.URL;

import javax.swing.JFrame;
import javax.xml.stream.XMLStreamException;

import core.RSSCoord;
import core.RSSFeedBean;

@SuppressWarnings("serial")
public class MainFrame extends JFrame{
	private RSSCoord coord = RSSCoord.getInstance();
	private RSSFeedList list;
	
	//TODO NEXT: GridLayout for RSSFeedLists
	
	public MainFrame() {
		super("RSS Reader");
		setSize(400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setup();
	}
	
	private void setup() {
		list = new RSSFeedList();
		add(list);
		
		//TODO NEXT: Add this to a SwingWorker thread
		try {
			RSSFeedBean feed = coord.loadRSSFeed(new URL("http://www.drdobbs.com/articles/jvm/rss"));
		} catch (IOException | XMLStreamException e) {
			e.printStackTrace();
		}
	}
}
