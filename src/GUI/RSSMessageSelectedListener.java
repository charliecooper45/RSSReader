package gui;

import core.RSSMessageBean;

/**
 * Sent to the MainFrame when a RSS message has been selected from a feed and needs to be displayed on the RSSViewerPanel
 * @author Charlie
 *
 */
public interface RSSMessageSelectedListener {
	public void messageRead(RSSMessageBean bean);
}
