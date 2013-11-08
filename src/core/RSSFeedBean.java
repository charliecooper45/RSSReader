package core;

import java.io.Serializable;
import java.util.List;

/**
 * A bean class that represents a single RSS feed
 * @author Charlie
 */
public class RSSFeedBean implements Serializable{
	private static final long serialVersionUID = 8043599524006529389L;
	private List<RSSMessageBean> message;
	private String title;
	
	public RSSFeedBean() {}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the message
	 */
	public List<RSSMessageBean> getMessages() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(List<RSSMessageBean> message) {
		this.message = message;
	}
}
