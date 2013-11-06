package gui;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.HORIZONTAL;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

//TODO NEXT: Give this a superclass that it can inherit from
@SuppressWarnings("serial")
public class AddRSSDialog extends RSSDialog {
	private RSSEventListener rssEventListener;
	private ButtonListener buttonListener;
	private JTextField urlFeed;
	
	public AddRSSDialog(JFrame frame) {
		super(frame, "Add a new RSS feed");
		setSize(400, 200);
		setup();
	}
	
	private void setup() {	
		gc = new GridBagConstraints();
		Utils.setGBC(gc, 1, 1, 1, 1, BOTH);
		add(new JLabel("URL:  "), gc);
		
		urlFeed = new JTextField();
		urlFeed.setPreferredSize(new Dimension(250, 20));
		Utils.setGBC(gc, 2, 1, 2, 1, BOTH);
		add(urlFeed, gc);
		
		buttonListener = new ButtonListener();
		confirmButton.addActionListener(buttonListener);
		cancelButton.addActionListener(buttonListener);
		Utils.setGBC(gc, 1, 2, 3, 1, HORIZONTAL);
		gc.insets = new Insets(20, 0, 0, 0);
		add(buttonPanel, gc);
	}
	
	/**
	 * @param rssEventListener the rssEventListener to set
	 */
	public void setRSSEventListener(RSSEventListener rssEventListener) {
		this.rssEventListener = rssEventListener;
	}

	private class ButtonListener implements ActionListener{
		private URL rssURL;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == cancelButton) {
				AddRSSDialog.this.dispose();
			} else {
				String url = urlFeed.getText();
				
				if(url.isEmpty()) {
					JOptionPane.showMessageDialog(AddRSSDialog.this, "RSS URL is empty", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						rssURL = new URL(url);
						rssEventListener.rssFeedAdded(rssURL);
						AddRSSDialog.this.dispose();
					} catch (MalformedURLException e1) {
						JOptionPane.showMessageDialog(AddRSSDialog.this, "RSS URL format is incorrect", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	}
}
