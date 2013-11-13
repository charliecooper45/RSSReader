package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

@SuppressWarnings("serial")
public class Toolbar extends JToolBar {
	private DialogShownListener dialogShownListener;
	private ButtonListener buttonListener;
	private JButton addFeedButton;
	private JButton refreshFeedsButton;
	private JButton removeFeedButton;

	public Toolbar() {
		buttonListener = new ButtonListener();
		setFloatable(false);
		setLayout(new FlowLayout(FlowLayout.CENTER));

		addFeedButton = new JButton("Add Feed");
		addFeedButton.setIcon(Utils.createIcon("/resources/images/add-icon.png"));
		addFeedButton.setToolTipText("Add an RSS Feed");
		addFeedButton.addActionListener(buttonListener);
		add(addFeedButton);
		
		refreshFeedsButton = new JButton();
		refreshFeedsButton.setIcon(Utils.createIcon("/resources/images/refresh-icon.png"));
		refreshFeedsButton.setToolTipText("Refresh the RSS feeds");
		refreshFeedsButton.addActionListener(buttonListener);
		add(refreshFeedsButton);

		removeFeedButton = new JButton("Remove Feed");
		removeFeedButton.setIcon(Utils.createIcon("/resources/images/remove-icon.png"));
		removeFeedButton.setToolTipText("Remove an RSS Feed");
		removeFeedButton.addActionListener(buttonListener);
		removeFeedButton.setEnabled(false);
		add(removeFeedButton);

		addFeedButton.setPreferredSize(removeFeedButton.getPreferredSize());
	}

	/**
	 * @param dialogShownListener the DialogShownListener to set
	 */
	public void setDialogShownListener(DialogShownListener dialogShownListener) {
		this.dialogShownListener = dialogShownListener;
	}
	
	/**
	 * Sets if the removeFeedButton is enabled
	 * @param enabled
	 */
	public void setRemoveButtonEnabled(boolean enabled) {
		removeFeedButton.setEnabled(enabled);
	}

	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();

			if (source == addFeedButton) {
				dialogShownListener.dialogShown(DialogType.ADD_RSS_FEED);
			} else if (source == removeFeedButton){
				dialogShownListener.dialogShown(DialogType.REMOVE_RSS_FEED);
			} else if (source == refreshFeedsButton) {
				dialogShownListener.dialogShown(DialogType.REFRESH_RSS_FEEDS);
			}
		}

	}
}
