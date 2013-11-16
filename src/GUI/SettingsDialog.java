package gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class SettingsDialog extends RSSDialog {
	private ButtonListener buttonListener;
	private JCheckBox box;
	private JPanel refreshPanel;
	private JSlider refreshSlider;
	private JLabel sliderLabel;

	public SettingsDialog(JFrame frame) {
		super(frame, "Settings");
		setSize(450, 250);
		box = new JCheckBox("Automatically update feeds");
		box.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(box.isSelected()) {
					setRefreshPanelEnabled(true);
				} else {
					setRefreshPanelEnabled(false);
				}
			}
		});
		box.setSelected(MainFrame.refreshFeeds);
		Utils.setGBC(gc, 1, 1, 1, 1, GridBagConstraints.BOTH);
		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.CENTER;
		add(box, gc);

		refreshPanel = new JPanel(new BorderLayout());
		sliderLabel = new JLabel("Minutes between automatic refreshes: " + MainFrame.refreshRate, JLabel.CENTER);
		refreshPanel.add(sliderLabel, BorderLayout.NORTH);
		refreshSlider = new JSlider(JSlider.HORIZONTAL, 0, 360, MainFrame.refreshRate);
		refreshSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slider = (JSlider) e.getSource();
				sliderLabel.setText("Minutes between automatic refreshes: " + slider.getValue());
			}
		});
		refreshSlider.setMajorTickSpacing(60);
		refreshSlider.setMinorTickSpacing(20);
		refreshSlider.setPaintTicks(true);
		refreshSlider.setPaintLabels(true);
		refreshPanel.add(refreshSlider, BorderLayout.CENTER);
		setRefreshPanelEnabled(MainFrame.refreshFeeds);
		Utils.setGBC(gc, 1, 2, 1, 1, GridBagConstraints.BOTH);
		gc.weighty = 4;
		add(refreshPanel, gc);

		buttonListener = new ButtonListener();
		cancelButton.addActionListener(buttonListener);
		confirmButton.addActionListener(buttonListener);
		Utils.setGBC(gc, 1, 3, 1, 1, GridBagConstraints.BOTH);
		gc.weighty = 1;
		add(buttonPanel, gc);
		// Add functionality to a) automatically refresh feeds or not b) setup speed of refreshes
	}
	
	private void setRefreshPanelEnabled(boolean value) {
		sliderLabel.setEnabled(value);
		refreshSlider.setEnabled(value);
	}

	public class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == confirmButton) {
				
			} else if (e.getSource() == cancelButton) {
				System.out.println("Cancelled!");
				SettingsDialog.this.dispose();
			}
			System.out.println("here 2");
		}
	}
}
