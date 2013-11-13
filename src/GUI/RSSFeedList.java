package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import core.RSSFeedBean;
import core.RSSMessageBean;

/**
 * Displays the list of news stories for an RSS feed in a JList
 * @author Charlie
 *
 */
@SuppressWarnings("serial")
public class RSSFeedList extends JPanel {
	private RSSFeedBean rssFeed;
	private JLabel titleLabel;
	private JList<RSSMessageBean> messageList;
	private DefaultListModel<RSSMessageBean> rssModel;
	private RSSMessageSelectedListener rssMessageSelectedListener;

	public RSSFeedList() {
		setup();
	}

	private void setup() {
		setLayout(new BorderLayout());
		titleLabel = new JLabel();
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		add(titleLabel, BorderLayout.NORTH);
		messageList = new JList<>();
		messageList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					RSSMessageBean selectedValue = messageList.getSelectedValue();
					if (selectedValue != null) {
						rssMessageSelectedListener.messageRead(selectedValue);
						selectedValue.setRead(true);
					}
				}
			}
		});
		messageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		messageList.setCellRenderer(new ListRenderer());
		messageList.setBorder(BorderFactory.createEtchedBorder());
		rssModel = new DefaultListModel<>();
		messageList.setModel(rssModel);
		add(messageList, BorderLayout.CENTER);
		setBorder(BorderFactory.createEtchedBorder());
	}

	/**
	 * @param rssFeed the rssFeed to set
	 */
	public void setRssFeed(RSSFeedBean rssFeed) {
		this.rssFeed = rssFeed;
		titleLabel.setText(rssFeed.getTitle());
	}

	/**
	 * @return the rssFeed
	 */
	public RSSFeedBean getRssFeed() {
		return rssFeed;
	}

	/**
	 * Updates the RSS messages displayed in the list
	 */
	public void updateRSSMessages() {
		rssModel.clear();

		// Add all the list data
		for (int i = 0; i < rssFeed.getMessages().size(); i++) {
			rssModel.addElement(rssFeed.getMessages().get(i));
		}
	}

	/**
	 * @param rssMessageSelectedListener the rssMessageSelectedListener to set
	 */
	public void setRssMessageSelectedListener(RSSMessageSelectedListener rssMessageSelectedListener) {
		this.rssMessageSelectedListener = rssMessageSelectedListener;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return rssFeed.getTitle();
	}

	private class ListRenderer implements ListCellRenderer<RSSMessageBean> {
		private JLabel label;
		private Font boldFont;
		private Font plainFont;

		public ListRenderer() {
			label = new JLabel();
			boldFont = label.getFont();
			plainFont = boldFont.deriveFont(Font.PLAIN);
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends RSSMessageBean> list, RSSMessageBean bean, int index, boolean isSelected, boolean cellHasFocus) {
			label.setText(bean.getTitle());
			label.setForeground(bean.isSelected() ? Color.red : Color.black);

			if (bean.isRead()) {
				label.setFont(plainFont);
			} else {
				label.setFont(boldFont);
			}
			return label;
		}

	}
}
