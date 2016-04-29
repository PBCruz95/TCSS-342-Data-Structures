import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * This class contains a 2d array of cells and calculates them in topoligcial
 * order as long as they are acyclic in their graphical representation.
 * 
 * @author Patrick Cruz
 * @author Carl Seiber
 *
 */
public class SpreadSheet {

	// The dimensions of the spreadsheet.
	static final int rows = 4, columns = 4;

	// The cells in the spreadsheet that contain a value or formula.
	int activeCells;

	// The 2d array of cells.
	private Cell[][] cellsArray;

	// The graph that contains cell dependencies.
	private Graph cellGraph;

	// Self-explanatory booleans.
	boolean sortNeeded, cycleFound, outOfBounds;

	/**
	 * Initializes a Spreadsheet.
	 */
	SpreadSheet() {
		cellsArray = new Cell[rows][columns];
		activeCells = 0;
		cellGraph = new Graph();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				cellsArray[i][j] = new Cell();
				cellsArray[i][j].setLocation(i, j);
			}
		}
	}

	/**
	 * Sets the edges in the dependency graph.
	 */
	public void setEdges() {
		sortNeeded = false;
		Cell curCell, depCell;
		Iterator<Cell> itr;
		List<Cell> dependents;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (j == 0)
					continue;
				curCell = cellsArray[i][j];
				dependents = curCell.getDependents();

				if (dependents.size() != 0) {
					sortNeeded = true;
					itr = dependents.iterator();

					while (itr.hasNext()) {
						depCell = itr.next();
						int dRow = depCell.getRow();
						int dCol = depCell.getCol();
						cellGraph.addEdge("(" + dRow + "," + dCol + ")", dRow, dCol, "(" + i + "," + j + ")", i, j);
					}
				}
			}
		}
	}

	/**
	 * Calculates the cells' formulas in a topological order based on cell
	 * dependency. If the graph is acyclic, then all cells are returned their
	 * orignal values.
	 * 
	 * @param row
	 *            the cell's row
	 * @param col
	 *            the cell's column
	 */
	public void TopologicalSort(int row, int col) {
		// This queue will store cells and be used only if a cycle is found.
		Queue<Cell> inCaseOfCycle = new LinkedList<Cell>();
		try {
			setEdges();

			if (sortNeeded) {
				cellGraph.acyclic("(" + row + "," + col + ")");
				Queue<Vertex> topOrder = cellGraph.getOrdering();

				while (!topOrder.isEmpty()) {
					Vertex v = topOrder.remove();
					Cell c = cellsArray[v.row][v.column];
					Cell copy = new Cell(c.row, c.column, c.getFormula(), c.getValue());
					inCaseOfCycle.add(copy);
					calculateCell(v.row, v.column);
				}

			} else {

				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < columns; j++) {
						if (j == 0)
							continue;
						Cell c = cellsArray[i][j];
						Cell copy = new Cell(c.row, c.column, c.getFormula(), c.getValue());
						inCaseOfCycle.add(copy);
						calculateCell(i, j);
					}
				}
			}

		} catch (GraphException e) {
			cycleFound = true;
			for (Cell aCell : inCaseOfCycle) {
				cellsArray[aCell.row][aCell.column] = aCell;
				inCaseOfCycle.remove(aCell);
			}
		} catch (NoSuchElementException e) {
			outOfBounds = true;
			cellGraph = new Graph();
		} catch (ArrayIndexOutOfBoundsException e) {
			outOfBounds = true;
			cellGraph = new Graph();
		}

	}

	/**
	 * Evaluates a cell's value or formula.
	 * 
	 * @param row
	 *            the cell's row
	 * @param column
	 *            the cell's value
	 */
	public void evaluateCell(int row, int column) {
		cellsArray[row][column].evaluate();
	}

	/**
	 * Similar to evaluateCell, but is specifically for this class.
	 * 
	 * @param row
	 *            the cell's row
	 * @param column
	 *            the cell's column
	 */
	public void calculateCell(int row, int column) {
		cellsArray[row][column].calculate(cellsArray);
	}

	/**
	 * Checks to see if a given position in the 2d array exists or not.
	 * 
	 * @param row
	 *            the array's row
	 * @param column
	 *            the array's column
	 * @return true if the position exists
	 */
	public boolean isValid(int row, int column) {
		return cellsArray[row][column].isValid();
	}

	/**
	 * Getter for a cell's value.
	 * 
	 * @param row
	 *            the cell's row
	 * @param col
	 *            the cell's column
	 * @return the cell's value
	 */
	public int getValue(int row, int col) {
		return cellsArray[row][col].getValue();
	}

	/**
	 * Getter for the number of rows in the spreadsheet.
	 * 
	 * @return the spreadsheet's row number
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Getter for the number of columns in the spreadsheet.
	 * 
	 * @return the spreadsheet's column number
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * Sets a cell's formula
	 * 
	 * @param row
	 *            the cell's row
	 * @param col
	 *            the cell's column
	 * @param formula
	 *            the cell's formula
	 */
	public void setFormula(int row, int col, String formula) {
		cellsArray[row][col].setFormula(formula);
	}

	/**
	 * Returns true if the spreadsheet contains a cycle.
	 * 
	 * @return true if cycle is found
	 */
	public boolean cycleFound() {
		return cycleFound;
	}
}
