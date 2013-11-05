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

	public static RSSCoord getInstance() {
		return RSSCoord.INSTANCE;
	}

	public RSSFeedBean loadRSSFeed(URL url) throws IOException, XMLStreamException {
		List<RSSFeedBean> rssFeedMessages = new ArrayList<>();
		RSSFeedBean rssFeed;
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		InputStream in = url.openStream();
		XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

		// Read the XML
		rssFeed = loadFeedHeader(eventReader);
		System.out.println(rssFeed.getTitle());

		while (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();
			if(event.isStartElement()) {
				String localPart = event.asStartElement().getName().getLocalPart();
				// TODO NEXT: Read the title/links for all the rss feed messages and add them to the rssFeed bean
			} else if(event.isEndElement()) {
			}
		}

		return rssFeed;
	}

	private RSSFeedBean loadFeedHeader(XMLEventReader eventReader) throws XMLStreamException {
		XMLEvent event = eventReader.nextEvent();
		String localPart = "";
		RSSFeedBean bean = null;

		while (!localPart.equals("item")) {
			if (event.isStartElement()) {
				localPart = event.asStartElement().getName().getLocalPart();
				// Read the title of the header
				if (localPart.equals("title")) {
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
		XMLEvent event = eventReader.nextEvent();
		
		if(event instanceof Characters) {
			data = event.asCharacters().getData();
		}
		return data;
	}
}
