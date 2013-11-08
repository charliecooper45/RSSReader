package core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
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
	public synchronized RSSFeedBean loadRSSFeed(URL url) throws IOException, XMLStreamException {
		List<RSSMessageBean> rssFeedMessages = new ArrayList<>();
		RSSFeedBean rssBean;
		RSSMessageBean messageBean;
		String title = "";
		
		openXMLStreams(url);

		// Read the XML
		rssBean = loadFeedHeader(eventReader);

		while (eventReader.hasNext()) {
			event = eventReader.nextEvent();
			if (event.isStartElement()) {
				String localPart = event.asStartElement().getName().getLocalPart();
				switch (localPart) {
				case TITLE:
					title = readCharacterData(eventReader);
					break;
				}
			} else if (event.isEndElement()) {
				if(event.asEndElement().getName().getLocalPart() == ITEM) {
					messageBean = new RSSMessageBean();
					messageBean.setTitle(title);
					
					rssFeedMessages.add(messageBean);
				}
			}
		}
		rssBean.setMessage(rssFeedMessages);

		return rssBean;
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
