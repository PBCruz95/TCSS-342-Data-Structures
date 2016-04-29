package model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * The user interface for the polynomial calculator.
 * 
 * @author TCSS 342
 * @version 1.0
 */
public class PolyCalcUI implements ActionListener {
	// frame and content pane
	private JFrame frame;
	private JPanel contentPane;
	private JLabel statusBar;

	// main panel
	private JTextField aPolyDisplay;
	private JTextField bPolyDisplay;
	private JTextField newTermCoefDisplay;
	private JTextField newTermExpDisplay;

	// The class that performs the calculations
	private PolyCalcEngine calc;

	// Construct the frame
	public PolyCalcUI(PolyCalcEngine engine) {
		jbInit();
		this.calc = engine;
	}

	// Component initialization
	private void jbInit() {
		this.frame = new JFrame("Polynomial Calculator");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.contentPane = (JPanel) this.frame.getContentPane();
		this.contentPane.setLayout(new BorderLayout(8, 8));
		this.contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));

		// menu and menu items
		JMenuBar jMenuBar = new JMenuBar();
		JMenu jMenuFile = new JMenu();
		JMenuItem jMenuFileExit = new JMenuItem();
		JMenu jMenuHelp = new JMenu();
		JMenuItem jMenuHelpAbout = new JMenuItem();

		jMenuFile.setText("File");
		jMenuFileExit.setText("Exit");
		jMenuFileExit.addActionListener(this);
		jMenuHelp.setText("Help");
		jMenuHelpAbout.setText("About");
		jMenuHelpAbout.addActionListener(this);

		jMenuFile.add(jMenuFileExit);
		jMenuHelp.add(jMenuHelpAbout);
		jMenuBar.add(jMenuFile);
		jMenuBar.add(jMenuHelp);
		this.frame.setJMenuBar(jMenuBar);

		// panel for polynomials and new term
		JPanel displayPanel = new JPanel(new GridLayout(2, 1));

		// polynomials A and B display
		JPanel polyPanel = new JPanel(new GridLayout(4, 1));

		JPanel aPolyPanel = new JPanel(new BorderLayout());
		aPolyPanel.add(new JLabel("A: "), BorderLayout.WEST);
		aPolyDisplay = new JTextField();
		aPolyDisplay.setBackground(Color.white);
		aPolyDisplay.setEditable(false);
		aPolyDisplay.setHorizontalAlignment(JTextField.RIGHT);
		aPolyPanel.add(aPolyDisplay, BorderLayout.CENTER);
		polyPanel.add(aPolyPanel);

		// second row of polyPanel
		polyPanel.add(new JLabel());

		JPanel bPolyPanel = new JPanel(new BorderLayout());
		bPolyPanel.add(new JLabel("B: "), BorderLayout.WEST);
		bPolyDisplay = new JTextField();
		bPolyDisplay.setBackground(Color.white);
		bPolyDisplay.setEditable(false);
		bPolyDisplay.setHorizontalAlignment(JTextField.RIGHT);
		bPolyPanel.add(bPolyDisplay, BorderLayout.CENTER);
		polyPanel.add(bPolyPanel);

		// fourth row of polyPanel
		polyPanel.add(new JLabel());

		displayPanel.add(polyPanel);

		// new term display
		JPanel newTermPanel = new JPanel(new FlowLayout());

		newTermPanel.add(new JLabel("New Term: "));
		newTermCoefDisplay = new JTextField(6);
		newTermCoefDisplay.setBackground(Color.white);
		newTermCoefDisplay.setEditable(true);
		newTermCoefDisplay.setHorizontalAlignment(JTextField.RIGHT);
		newTermPanel.add(newTermCoefDisplay);

		newTermPanel.add(new JLabel("x^"));
		newTermExpDisplay = new JTextField(6);
		newTermExpDisplay.setBackground(Color.white);
		newTermExpDisplay.setEditable(true);
		newTermExpDisplay.setHorizontalAlignment(JTextField.LEFT);
		newTermPanel.add(newTermExpDisplay);

		displayPanel.add(newTermPanel);

		contentPane.add(displayPanel, BorderLayout.NORTH);

		// panel of buttons
		JPanel buttonPanel = new JPanel(new GridLayout(4, 3));
		addButton(buttonPanel, "A <-> B");
		addButton(buttonPanel, "A = 0");
		addButton(buttonPanel, "A = A + B");
		addButton(buttonPanel, "A = A + T");
		addButton(buttonPanel, "B = 0");
		addButton(buttonPanel, "A = A - B");
		addButton(buttonPanel, "B = B + T");
		addButton(buttonPanel, "A = -A");
		addButton(buttonPanel, "A = A * B");
		addButton(buttonPanel, "B = Deriv(A)");
		addButton(buttonPanel, "B = -B");
		addButton(buttonPanel, "B = A + B");
		contentPane.add(buttonPanel, BorderLayout.CENTER);

		// the status bar (at the bottom of frame)
		statusBar = new JLabel("Version 1.0");
		contentPane.add(statusBar, BorderLayout.SOUTH);

		frame.pack();
	}

	// File | Exit action performed
	public void jMenuFileExit_actionPerformed(ActionEvent e) {
		System.exit(0);
	}

	// Help | About action performed
	public void jMenuHelpAbout_actionPerformed(ActionEvent e, PolyCalcEngine engine) {
	}

	private void addButton(Container panel, String buttonText) {
		JButton button = new JButton(buttonText);

		button.setBackground(Color.lightGray);
		button.addActionListener(this);
		panel.add(button);
	}

	/**
	 * Main event dispatching method of the calculator.
	 * 
	 * @param e
	 *            the ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (command.equals("About")) {
			jMenuHelpAbout_actionPerformed(null, calc);
		} else if (command.equals("Exit")) {
			jMenuFileExit_actionPerformed(null);
		} else if (command.equals("A = 0")) {
			calc.zeroA();
		} else if (command.equals("B = 0")) {
			calc.zeroB();
		} else if (command.equals("A = -A")) {
			calc.negateA();
		} else if (command.equals("B = -B")) {
			calc.negateB();
		} else if (command.equals("A = A + T")) {
			try {
				calc.addTermA(Integer.valueOf(newTermCoefDisplay.getText()).intValue(),
						Integer.valueOf(newTermExpDisplay.getText()).intValue());
			} catch (NumberFormatException exception) {
				// do nothing
			}
		} else if (command.equals("B = B + T")) {
			try {
				calc.addTermB(Integer.valueOf(newTermCoefDisplay.getText()).intValue(),
						Integer.valueOf(newTermExpDisplay.getText()).intValue());
			} catch (NumberFormatException exception) {
				// do nothing
			}
		} else if (command.equals("A = A + B")) {
			calc.addAAB();
		} else if (command.equals("B = A + B")) {
			calc.addBAB();
		} else if (command.equals("A = A - B")) {
			calc.subtractAB();
		} else if (command.equals("A = A * B")) {
			calc.multiplyAB();
		} else if (command.equals("B = Deriv(A)")) {
			calc.derivativeA();
		} else if (command.equals("A <-> B")) {
			calc.swapAB();
		}
		redisplay();
	}

	public void redisplay() {
		this.aPolyDisplay.setText(calc.getAPolyValue());
		this.bPolyDisplay.setText(calc.getBPolyValue());
	}

	public JFrame getFrame() {
		return this.frame;
	}

	private void errorMessage(String s) {
		statusBar.setText(s);
	}
}
