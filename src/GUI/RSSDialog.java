package gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class RSSDialog extends JDialog {
	protected GridBagConstraints gc;
	protected JButton confirmButton;
	protected JButton cancelButton;
	protected JPanel buttonPanel;
	
	public RSSDialog(JFrame frame, String title) {
		super(frame, title, true);
		setLayout(new GridBagLayout());
		setLocationRelativeTo(frame);
		setResizable(false);
		
		// Set up the button panel 
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 0));
		confirmButton = new JButton("Confirm");
		cancelButton = new JButton("Cancel");
		cancelButton.setPreferredSize(confirmButton.getPreferredSize());
		buttonPanel.add(confirmButton);
		buttonPanel.add(cancelButton);
	}
}
