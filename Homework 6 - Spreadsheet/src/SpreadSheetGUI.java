import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;


/**
 * GUI for a standard spreadsheet layout, provides a graphical spread sheet with the ability to run
 * calculations based on other cells in the spread sheet.
 * @author Carl Seiber
 *
 */
public class SpreadSheetGUI extends JFrame implements ActionListener {
	private static final String INVALID = "You have invalid formulas in one or more cells, please fix them to proceed!.";
	private static final String CYCLE = "A cyclic reference has been found, your previous values have been restored.";
	private static final String OUT_OF_BOUNDS = "An out of bounds cell reference has been detected, the spreadsheet was not processed!";
	private int activeRow, activeCol;
	private JMenuBar menuBar;
	private JMenu fileMenu, listMenu, updateMenu, helpMenu;
	private JMenuItem openItem, saveItem, printItem, exitItem, searchNameItem, allItem, addItem, deleteItem, modifyItem, aboutItem;
	private JLabel lblError;
	JButton btnEval, btnClear;
	private TableModel model, oldModel;
	private SpreadSheet sheet;
	private JTable tblCells;
	private Color prevColor;
	JPanel gridPanel, infoPanel, errorPanel;

	
	/**
	 * Main method to instantiate the class.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpreadSheetGUI f = new SpreadSheetGUI();
		f.setVisible(true);
		f.addWindowListener(    //Creates anonymous class
				new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
			});
	}
	
	/**
	 * Class constructor, all guid components are initialized and formatted for use in the GUI
	 * action listeners and window closing events are setup as well.
	 */
	SpreadSheetGUI()
	{
		this.setTitle("SpreadSheet");
		sheet = new SpreadSheet();
		model = new DefaultTableModel(sheet.getRows(), sheet.getColumns());
		oldModel = new DefaultTableModel(sheet.getRows(), sheet.getColumns());
		model.addTableModelListener(new TableListener());
		tblCells = new JTable(model);
		tblCells.getTableHeader().setReorderingAllowed(false);
		tblCells.setRowSelectionAllowed(true);
		tblCells.setColumnSelectionAllowed(true);
		tblCells.setRowHeight(30);
		gridPanel = new JPanel();
		infoPanel = new JPanel(new FlowLayout());
		errorPanel = new JPanel();
		lblError = new JLabel();
		lblError.setBorder(BorderFactory.createRaisedBevelBorder());
		lblError.setFont(new Font("Serif", Font.PLAIN, 20));
		lblError.setForeground(Color.RED);
		gridPanel.setLayout(new BorderLayout());
		gridPanel.setSize(300, 600);
		gridPanel.add(new JScrollPane(tblCells), BorderLayout.CENTER);
		errorPanel.add(lblError);
		tblCells.getColumnModel().getColumn(0).setCellRenderer(new RowHeaderRenderer());
		btnEval = new JButton("Evaluate SpreadSheet");
		btnEval.addActionListener(this);
		infoPanel.add(btnEval);
		btnClear = new JButton("Clear SpreadSheet");
		btnClear.addActionListener(this);
		infoPanel.add(btnClear);
		this.setLayout(new BorderLayout());
		this.setTitle("Contact Database");
		this.setSize(1200, 800);
		Container main = this.getContentPane();
		main.setLayout(new BorderLayout());
		main.add(gridPanel, BorderLayout.CENTER);
		main.add(infoPanel, BorderLayout.NORTH);
		main.add(errorPanel, BorderLayout.SOUTH);
		exitItem = new JMenuItem("Exit");
		exitItem.setToolTipText("Exits the program");
		exitItem.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent ae) {
            	System.exit(0);
            }
        });
		
		fileMenu = new JMenu("File");
		fileMenu.add(exitItem);
		
		menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		
		this.setJMenuBar(menuBar);
	}

	
	/**
	 * Checks to see if any errors were found during formula parsing
	 * If one is found the loop is instantly ended and returns true
	 * otherwise returns false
	 * @return the error flag.
	 */
    public boolean checkErrors()
    {
    	for (int i=0; i<sheet.getRows(); i++)
    	{
    		for (int j = 0; j<sheet.getColumns(); j++)
    		{
    			if(!sheet.isValid(i,j))
    			{
    				tblCells.setBackground(Color.RED);
    				return true;
    			}
    				
    		}
    	}
    	return false;   	
    }
    
    /**
     * Resets the spreadSheet, sets up a new sheet and clears any error messages
     */
    private void clear()
    {
    	sheet = new SpreadSheet();
    	setAllCellFormulas();
    	lblError.setText("");
    	tblCells.setBackground(Color.WHITE);
    }
    
    /**
     * Evaluates the spreadsheet in a logical ordering based on cell dependents then
     * displays the results to the user in each cell
     */
    private void evaluateSpreadSheet()
    {
    	tblCells.setBackground(Color.WHITE); //reset in case error previously
    	lblError.setText("");
    	setAllCellFormulas(); //grab any changes to the cell formula
    	setAllCellStates(); //setup cell variables based on the formula
    	if (checkErrors())
    		lblError.setText(INVALID);   	
    	else
    	{
    		
    		//setup the logical ordering in which the cells will be calculated.
    		sheet.TopologicalSort(activeRow, activeCol);
    		
    		//test for errors and set messages if appropriate.
        	if (sheet.outOfBounds)
        	{
        		lblError.setText(OUT_OF_BOUNDS);
        		tblCells.setBackground(Color.RED);
        		clear();
        	}
        	if (sheet.cycleFound)
        	{
        		tblCells.setBackground(Color.YELLOW);
        		lblError.setText(CYCLE);
        	}
        	
        	//runs regardless, if a cycle is found this will revert any changes
        	//if no errors then it will update the cells accordingly
    		setAllCellValues();
    	}
    	
    	//store an archive of all cell values for later testing
    	cloneTable();
    }
    
    /**
     * Gets the current value in the cell and updates the cell structure if
     * a modification was detected
     */
    private void setAllCellFormulas()
    {
    	String formula = "";
    	if (oldModel != null)
    	{
    		for (int i=0; i<sheet.getRows(); i++)
    		{
    			for (int j = 0; j<sheet.getColumns(); j++)
    			{
    				if (j == 0)
    					continue;
    				Object curValue = model.getValueAt(i, j);
    				Object oldValue = oldModel.getValueAt(i, j);

    				//check for null as well as check to see if the new value has changed from the
    				//old value, if no changes then we leave the formula underneath intact.
    				if (curValue != null && (curValue != oldValue))
    				{
    					formula = curValue.toString();
    					sheet.setFormula(i, j, formula.trim());
    				}


    			}
    		}
    	}
    	
    	//this means its our first run in the spreadsheet and so no need to
    	//test for old values.
    	else
    	{
    		for (int i=0; i<sheet.getRows(); i++)
    		{
    			for (int j = 0; j<sheet.getColumns(); j++)
    			{
    				if (j == 0)
    					continue;
    				Object curValue = model.getValueAt(i, j);

    				if (curValue != null)
    				{
    					formula = curValue.toString();
    					sheet.setFormula(i, j, formula.trim());
    				}
    			}
    		}
    	}
    }
    
    /**
     * Copies all the values of the current state of the JTable Model into an
     * archive model structure.
     */
    private void cloneTable()
    {
    	for (int i=0; i<sheet.getRows(); i++)
    	{
    		for (int j = 0; j<sheet.getColumns(); j++)
    		{
        		if (j == 0)
        			continue;    			
    			oldModel.setValueAt(model.getValueAt(i, j), i, j);
    		}
    	}
    }
    
    /**
     * Parses the formula for each cell setting up the underlying
     * cell structure for later calculations.
     */
    private void setAllCellStates()
    {
    	for (int i=0; i<sheet.getRows(); i++)
    	{
    		for (int j = 0; j<sheet.getColumns(); j++)
    		{
        		if (j == 0)
        			continue;    			
    			sheet.evaluateCell(i, j);
    		}
    	}
    }
    
    /**
     * Gets the value of all the cells and display them in their respective GUI
     * component for the user.
     */
    private void setAllCellValues()
    {
    	for (int i=0; i<sheet.getRows(); i++)
    	{
    		for (int j = 0; j<sheet.getColumns(); j++)
    		{
        		if (j == 0)
        			continue;    			
    			model.setValueAt(sheet.getValue(i, j), i, j);
    		}
    	}
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		String obj = e.getActionCommand();
		if (obj == "Evaluate SpreadSheet")
			evaluateSpreadSheet();
		else if (obj == "Clear SpreadSheet")
			clear();
	}


	static class RowHeaderRenderer extends DefaultTableCellRenderer {
	    public RowHeaderRenderer() {
	        setHorizontalAlignment(SwingConstants.CENTER);
	    }

	    @Override
		public Component getTableCellRendererComponent(JTable table,
	            Object value, boolean isSelected, boolean hasFocus, int row,
	            int column) {
	        if (table != null) {
	            JTableHeader header = table.getTableHeader();

	            if (header != null) {
	                setForeground(header.getForeground());
	                setBackground(header.getBackground());
	                setFont(header.getFont());
	                
	            }
	        }

	        if (isSelected) {
	            setFont(getFont().deriveFont(Font.BOLD));
	        }

	        setValue(Integer.toString(row));
	        return this;
	    }
	}
	
	class TableListener implements TableModelListener {

		@Override
		public void tableChanged(TableModelEvent e) {
			int row = e.getFirstRow();
	        int col = e.getColumn();
	        activeRow = row;
	        activeCol = col;
		}
		
	}
}
