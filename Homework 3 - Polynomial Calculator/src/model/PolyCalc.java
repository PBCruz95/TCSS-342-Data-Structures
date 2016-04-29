package model;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * A polynomial calculator
 * 
 * @author TCSS 342
 * @version 1.0
 */

public class PolyCalc {

	private PolyCalcEngine engine;
	private PolyCalcUI gui;

	public PolyCalc() {
		// Construct the application
		engine = new PolyCalcEngine();
		gui = new PolyCalcUI(engine);
		JFrame frame = gui.getFrame();

		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		frame.setVisible(true);
		gui.redisplay();
	}

	public static void main(String[] args) {
		/*
		 * To test your code without using the GUI, you might want to write code
		 * like this. The output will appear on the terminal window.
		 */
		/*
		 * Polynomial a = new Polynomial();
		 * 
		 * a.zeroPolynomial(); System.out.println ( "a = " + a.print() );
		 * a.insertTerm(-3, 5); System.out.println ( "a = " + a.print() );
		 * a.insertTerm(2, 1); System.out.println ( "a = " + a.print() );
		 */

		new PolyCalc();
	}

}
