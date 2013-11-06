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
	
	public Toolbar() {
		buttonListener = new ButtonListener();
		setFloatable(false);
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		addFeedButton = new JButton("Add Feed");
		addFeedButton.setIcon(Utils.createIcon("/resources/images/add-icon.png"));
		addFeedButton.setToolTipText("Add an RSS Feed");
		addFeedButton.addActionListener(buttonListener);
		add(addFeedButton);
	}
	
	/**
	 * @param dialogShownListener the DialogShownListener to set
	 */
	public void setDialogShownListener(DialogShownListener dialogShownListener) {
		this.dialogShownListener = dialogShownListener;
	}

	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			dialogShownListener.dialogShown();
		}
		
	}
}
