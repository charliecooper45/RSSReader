package main;
import gui.MainFrame;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.xml.stream.XMLStreamException;


/**
 * Coded with help from www.vogella.com
 * @author Charlie/Lars Vogel
 *
 */
public class Main {
	public static void main(String[] args) throws IOException, XMLStreamException {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JFrame frame = new MainFrame();
				frame.setVisible(true);
			}
		});
	}
	
	private static void readXML(URL myURL) throws IOException {
		Scanner scan = new Scanner(myURL.openStream());
		
		while(scan.hasNextLine()) {
			System.out.println(scan.nextLine());
		}
		scan.close();
	}
}
