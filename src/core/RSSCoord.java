package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

public enum RSSCoord {
	INSTANCE;
	private static final String TITLE = "title";
	private static final String ITEM = "item";
	private static final String LINK = "link";
	private static final Path SESSION_FOLDER = Paths.get(System.getProperty("user.dir") + "/session");;
	private static final Path SAVED_SESSION = Paths.get(SESSION_FOLDER + "/session.obj");
	private XMLInputFactory inputFactory;
	private XMLEventReader eventReader;
	private XMLEvent event;

	public static RSSCoord getInstance() {
		return RSSCoord.INSTANCE;
	}

	/**
	 * Creates an RSSFeedBean object that holds the data relevant to one RSS feed.
	 * @param url of the RSS feed
	 * @return the RSSFeedBean
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	public RSSFeedBean loadRSSFeed(URL url) throws IOException, XMLStreamException {
		List<RSSMessageBean> rssFeedMessages = new ArrayList<>();
		RSSFeedBean rssBean;

		// Read the XML
		synchronized (this) {
			openXMLStreams(url);
			rssBean = loadFeedHeader(eventReader);
			rssFeedMessages = readRSSMessages(url);
		}
		rssBean.setMessages(rssFeedMessages);
		rssBean.setUrl(url);

		return rssBean;
	}

	//TODO NEXT: Add method to update the RSSFeedBean with the latest feeds, this will have to use another SwingWorker that calls this method but does not create an RSSBean like the existing method 
	/**
	 * Updates a feed with the latest messages
	 * @param bean to be updated
	 * @throws IOException 
	 * @throws XMLStreamException 
	 */
	public void updateRSSFeed(RSSFeedBean bean) throws XMLStreamException, IOException {
		List<RSSMessageBean> updatedMessages;
		List<RSSMessageBean> currentMessages = bean.getMessages();
		URL url = bean.getUrl();
		
		synchronized (this) {
			openXMLStreams(url);
			updatedMessages = readRSSMessages(url);
		}
		updatedMessages.removeAll(currentMessages);
		if(!updatedMessages.isEmpty()) {
			System.out.println("New messages added");
			currentMessages.addAll(0, updatedMessages);
		}
	}

	private List<RSSMessageBean> readRSSMessages(URL url) throws XMLStreamException, IOException {
		List<RSSMessageBean> rssFeedMessages = new ArrayList<>();
		RSSMessageBean messageBean;
		String title = "";
		String link = "";

		while (eventReader.hasNext()) {
			event = eventReader.nextEvent();
			if (event.isStartElement()) {
				String localPart = event.asStartElement().getName().getLocalPart();
				switch (localPart) {
				case TITLE:
					title = readCharacterData(eventReader);
					break;
				case LINK:
					link = readCharacterData(eventReader);
					break;
				}

			} else if (event.isEndElement()) {
				if (event.asEndElement().getName().getLocalPart() == ITEM) {
					messageBean = new RSSMessageBean();
					messageBean.setTitle(title);
					messageBean.setLink(link);
					rssFeedMessages.add(messageBean);
				}
			}
		}

		return rssFeedMessages;
	}

	/**
	 * Saves all the current RSS feeds and their state (whether messages are read or not) to the disk
	 * @param feeds to be saved
	 * @throws IOException 
	 */
	public void saveSession(List<RSSFeedBean> feeds) throws IOException {
		if (!Files.exists(SESSION_FOLDER)) {
			Files.createDirectory(SESSION_FOLDER);
		}

		ObjectOutputStream os = new ObjectOutputStream(Files.newOutputStream(SAVED_SESSION));
		os.writeObject(feeds);
		os.close();
	}

	/**
	 * Checks for a previously saved session and if it is found load it into the program.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws XMLStreamException 
	 */
	@SuppressWarnings("unchecked")
	public List<RSSFeedBean> loadSession() throws IOException, ClassNotFoundException, XMLStreamException {
		List<RSSFeedBean> feeds = null;
		if (Files.exists(SAVED_SESSION)) {
			// Load the file
			ObjectInputStream is = new ObjectInputStream(Files.newInputStream(SAVED_SESSION));
			feeds = (List<RSSFeedBean>) is.readObject();
			// Update all the feeds with the latest messages
			for(RSSFeedBean bean : feeds) {
				updateRSSFeed(bean);
			}
			is.close();
		}
		return feeds;
	}

	private void openXMLStreams(URL url) throws XMLStreamException, IOException {
		inputFactory = XMLInputFactory.newInstance();
		InputStream in = url.openStream();
		eventReader = inputFactory.createXMLEventReader(in);
	}

	private RSSFeedBean loadFeedHeader(XMLEventReader eventReader) throws XMLStreamException {
		event = eventReader.nextEvent();
		String localPart = "";
		RSSFeedBean bean = null;

		while (!localPart.equals("item")) {
			if (event.isStartElement()) {
				localPart = event.asStartElement().getName().getLocalPart();
				// Read the title of the header
				if (localPart.equals(TITLE)) {
					String feedTitle = readCharacterData(eventReader);
					bean = new RSSFeedBean();
					bean.setTitle(feedTitle);
				}
			}
			event = eventReader.nextEvent();
		}

		return bean;
	}

	private String readCharacterData(XMLEventReader eventReader) throws XMLStreamException {
		String data = "";
		event = eventReader.nextEvent();

		if (event instanceof Characters) {
			data = event.asCharacters().getData();
		}
		return data;
	}
}
