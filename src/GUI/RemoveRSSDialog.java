package gui;

import static java.awt.GridBagConstraints.BOTH;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class RemoveRSSDialog extends RSSDialog {
	private ButtonListener buttonListener;
	private List<RSSFeedList> feeds;
	private JComboBox<Object> feedsCombo;

	public RemoveRSSDialog(JFrame frame, List<RSSFeedList> feeds) {
		super(frame, "Remove a RSS Feed");
		this.feeds = feeds;
		setup();
	}
	
	private void setup() {
		Utils.setGBC(gc, 1, 1, 1, 1, BOTH);
		feedsCombo = new JComboBox<>(feeds.toArray());
		add(feedsCombo, gc);
		
		Utils.setGBC(gc, 1, 2, 1, 2, BOTH);
		gc.insets = new Insets(20, 0, 0, 0);
		buttonListener = new ButtonListener();
		confirmButton.addActionListener(buttonListener);
		cancelButton.addActionListener(buttonListener);
		add(buttonPanel, gc);
	}

	private class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == cancelButton) {
				RemoveRSSDialog.this.dispose();
			} else {
				RSSFeedList selectedList = (RSSFeedList) feedsCombo.getSelectedItem();
				int answer = JOptionPane.showConfirmDialog(RemoveRSSDialog.this, "Remove " + selectedList, "Confirm", JOptionPane.YES_NO_OPTION);
				if(answer == JOptionPane.YES_OPTION) {
					rssEventListener.rssFeedRemoved(selectedList);
					RemoveRSSDialog.this.dispose();
				} 
			}
		}
	}
}
