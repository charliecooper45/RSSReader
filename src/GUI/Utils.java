package gui;

import java.awt.GridBagConstraints;
import java.net.URL;

import javax.swing.ImageIcon;

public class Utils {
	private Utils() {};
	
	public static ImageIcon createIcon(String path) {
		URL url = System.class.getResource(path);

		if(url == null) {
			System.err.println("Unable to load image: + path");
		}
		
		ImageIcon icon = new ImageIcon(url);
		
		return icon;
	}
	
	public static void setGBC(GridBagConstraints gc, int row, int column, int width, int height, int fill) {
		gc.gridx = row;
		gc.gridy = column;
		gc.gridwidth = width;
		gc.gridheight = height;
		gc.fill = fill;
	}
}
