import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Cell {
	
	//Private instance members
	private String formula, prevFormula;	
	protected int row, column;	
	private int value;	
	private List<Cell> dependents;
	private ExpressionTree eTree;
	private boolean isValid;
	private static final char Plus = '+', Minus = '-', Mult = '*', Div = '/', LeftParen = '(';
	private static final int BadCell = -1;
	private static Cell[][] cells;
	
	//main method for testing only
    public static void main(String[] args){
    	Cell cell = new Cell();
    	//cell.convertCellReference("AFO345");
    	cell.setFormula("3*(-2*5- 2)");
    	//cell.evaluate(cells);
        }
    
    Cell()
    {
    	formula = "0";
    	value = 0;
    	eTree = new ExpressionTree();
    	dependents = new LinkedList<Cell>();
    	isValid = true;
    }
    
    Cell(int theRow, int theCol)
    {
    	row = theRow;
    	column = theCol;
    }
    
    public Cell(int theRow, int theCol, String theFormula, int theValue){
    	row = theRow;
    	column = theCol;
    	formula = theFormula;
    	value = theValue;
    }
	
	/**
	 * Converts the cell reference into a numerical representation
	 * @param cellRep The string representation of the cell's address.
	 * @return the numerical representation of the cell's address.
	 */
	private int[] convertCellReference(String cellRep)
	{
		int[] values = new int[2];
		int index = 0;
		for(int i = 0; i<cellRep.length(); i++)
		{
			char ch = Character.toUpperCase(cellRep.charAt(i));			
			if (Character.isLetter(ch))
			{
				int chVal = ch - 64;
				index = index*(26) + chVal;
				values[1] = index - 1;
			}
			
			//Once all letters have been processed, the rest is the row reference that can be directly converted to int.
			else
			{
				values[0] = Integer.parseInt(cellRep.substring(i, cellRep.length()));				
				break;
			}
		}
		System.out.println(values[0]);
		System.out.println(values[1]);
		return values;
	}
	
	public void setValid(boolean value)
	{
		isValid = value;
	}
	
	public boolean isValid()
	{
		return isValid;
	}
	
	public boolean isActive()
	{
		return formula != "0";
	}
	public String getFormula()
	{
		return formula;
	}
	
	public void setFormula(String theFormula)
	{
		formula = theFormula;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public void setValue(int theValue)
	{
		value = theValue;
	}
	
	public void setLocation(int theRow, int theColumn)
	{
		row = theRow;
		column = theColumn;
	}
	
	public int[] getLocation()
	{
		int[] location = {row, column};
		return location;
	}
	
	public int getRow()
	{
		return row;
	}
	
	public int getCol()
	{
		return column;
	}
	
	public List<Cell> getDependents()
	{
		return dependents;
	}
	
/*	public void removeDependent(String theCell)
	{
		Iterator<Cell> itr = dependents.iterator();
		while (itr.hasNext())
		{
			if (itr.next().getValue() == theCell)
			{
				 itr.remove();
				 dependentCount--;
				 break;
			}
		}
		
	}*/
	
	/**
	 * Given an operator, return its priority.
	 *
	 * priorities:
	 *   +, - : 0
	 *   *, / : 1
	 *   (    : 2
	 *
	 * @param ch  a char
	 * @return  the priority of the operator
	 */
	int operatorPriority (char ch) {
	    if (!isOperator(ch)) {
	        // This case should NEVER happen
	        System.out.println("Error in operatorPriority.");
	        System.exit(0);
	    }
	    switch (ch) {
	        case Plus:
	            return 0;
	        case Minus:
	            return 0;
	        case Mult:
	            return 1;
	        case Div:
	            return 1;
	        case LeftParen:
	            return 2;

	        default:
	            // This case should NEVER happen
	            System.out.println("Error in operatorPriority.");
	            return -1;
	    }
	}
	
	/**
	 * Return true if the char ch is an operator of a formula.
	 * Current operators are: +, -, *, /, (.
	 * @param ch  a char
	 * @return  whether ch is an operator
	 */
	boolean isOperator (char ch) {
	    return ((ch == Plus) ||
	            (ch == Minus) ||
	            (ch == Mult) ||
	            (ch == Div) ||
	            (ch == LeftParen) );
	}
	
	/**
	 * getFormula
	 * 
	 * Given a string that represents a formula that is an infix
	 * expression, return a stack of Tokens so that the expression,
	 * when read from the bottom of the stack to the top of the stack,
	 * is a postfix expression.
	 * 
	 * A formula is defined as a sequence of tokens that represents
	 * a legal infix expression.
	 * 
	 * A token can consist of a numeric literal, a cell reference, or an
	 * operator (+, -, *, /).
	 * 
	 * Multiplication (*) and division (/) have higher precedence than
	 * addition (+) and subtraction (-).  Among operations within the same
	 * level of precedence, grouping is from left to right.
	 * 
	 * This algorithm follows the algorithm described in Weiss, pages 105-108.
	 */
	private Stack<Token> getFormula(String formula) {
	    Stack returnStack = new Stack();  // stack of Tokens (representing a postfix expression)
	    dependents.clear();
	    boolean error = false;
	    char ch = ' ', prevCh = ' ', prevPrevCh = ' ';

	    int literalValue = 0;

	    CellToken cellToken;
	    int column = 0;
	    int row = 0;

	    int index = 0;  // index into formula
	    Stack operatorStack = new Stack();  // stack of operators

	    while (index < formula.length() ) {
	        // get rid of leading whitespace characters
	        while (index < formula.length() ) {
	            ch = formula.charAt(index);
	            if (!Character.isWhitespace(ch)) {
	            	
	                break;
	            }
	            prevCh = ch;
	            index++;
	        }

	        if (index == formula.length() ) {
	        	isValid = false;
	            error = true;
	            break;
	        }

	        // ASSERT: ch now contains the first character of the next token.
	        if (isOperator(ch)) {
	            // We found an operator token
	        	
	            switch (ch) {
	                case OperatorToken.PLUS:
	                case OperatorToken.MINUS:
	                case OperatorToken.MULT:
	                case OperatorToken.DIV:
	                case OperatorToken.LEFT_PAREN:
	                    // push operatorTokens onto the output stack until
	                    // we reach an operator on the operator stack that has
	                    // lower priority than the current one.
	                	OperatorToken stackOperator;
	                    while (!operatorStack.isEmpty()) {
	                        stackOperator = (OperatorToken) operatorStack.peek();
	                        if ( (operatorPriority(stackOperator.getOperatorToken()) >= operatorPriority(ch)) &&
	                            (stackOperator.getOperatorToken() != OperatorToken.LEFT_PAREN) ) {

	                            // output the operator to the return stack    
	                            operatorStack.pop();
	                            returnStack.push(stackOperator);
	                        } else {
	                            break;
	                        }
	                    }
	                    break;

	                default:
	                    // This case should NEVER happen
	                    System.out.println("Error in getFormula.");
	                    System.exit(0);
	                    break;
	            }
	            // push the operator on the operator stack
	            operatorStack.push(new OperatorToken(ch));

	            index++;

	        } else if (ch == ')') {    // maybe define OperatorToken.RightParen ?
	            OperatorToken stackOperator;
	            stackOperator = (OperatorToken) operatorStack.pop();
	            // This code does not handle operatorStack underflow.
	            while (stackOperator.getOperatorToken() != OperatorToken.LEFT_PAREN) {
	                // pop operators off the stack until a LeftParen appears and
	                // place the operators on the output stack
	                returnStack.push(stackOperator);
	                stackOperator = (OperatorToken) operatorStack.pop();
	            }

	            index++;
	        } else if (Character.isDigit(ch)) {
	            // We found a literal token
	            literalValue = ch - '0';
	            index++;
	            while (index < formula.length()) {
	                ch = formula.charAt(index);
	                if (Character.isDigit(ch)) {
	                    literalValue = (literalValue * 10) + (ch - '0');
	                    index++;
	                } else {
	                    break;
	                }
	            }
	            
	            if (prevCh == OperatorToken.MINUS)
	            {
	            	operatorStack.pop();
	            	literalValue *= -1;
	            }
	            // place the literal on the output stack
	            returnStack.push(new LiteralToken(literalValue));

	        } else if (Character.isUpperCase(ch)) {
	            // We found a cell reference token
	            cellToken = new CellToken();
	            index = getCellToken(formula, index, cellToken);
	            if (cellToken.getRow() == BadCell) {
	            	isValid = false;
	                error = true;
	                break;
	            } else {
	                // place the cell reference on the output stack
	                returnStack.push(cellToken);
	                dependents.add(new Cell(cellToken.getRow(), cellToken.getCol()));
	            }

	        } else {
	        	isValid = false;
	            error = true;
	            break;
	        }
	        prevCh = ch;
	    }

	    // pop all remaining operators off the operator stack
	    while (!operatorStack.isEmpty()) {
	        returnStack.push(operatorStack.pop());
	    }

	    if (error) {
	        // a parse error; return the empty stack
	        returnStack.empty();
	        isValid = false;
	    }

	    return returnStack;
	}
	
	public void evaluate()
	{
		eTree.buildTree(getFormula(formula));
		//eTree.print();	
		//System.out.println(Integer.toString(value));
	}
	
	public void calculate(Cell[][] cellArray)
	{
		//evaluate();
		value = eTree.calculate(cellArray);
	}
	
	private void listExpression()
	{
		
		//evaluate();
	}

	/**
	 * getCellToken
	 * 
	 * Assuming that the next chars in a String (at the given startIndex)
	 * is a cell reference, set cellToken's column and row to the
	 * cell's column and row.
	 * If the cell reference is invalid, the row and column of the return CellToken
	 * are both set to BadCell (which should be a final int that equals -1).
	 * Also, return the index of the position in the string after processing
	 * the cell reference.
	 * (Possible improvement: instead of returning a CellToken with row and
	 * column equal to BadCell, throw an exception that indicates a parsing error.)
	 * 
	 * A cell reference is defined to be a sequence of CAPITAL letters,
	 * followed by a sequence of digits (0-9).  The letters refer to
	 * columns as follows: A = 0, B = 1, C = 2, ..., Z = 25, AA = 26,
	 * AB = 27, ..., AZ = 51, BA = 52, ..., ZA = 676, ..., ZZ = 701,
	 * AAA = 702.  The digits represent the row number.
	 *
	 * @param inputString  the input string
	 * @param startIndex  the index of the first char to process
	 * @param cellToken  a cellToken (essentially a return value)
	 * @return  index corresponding to the position in the string just after the cell reference
	 */
	int getCellToken (String inputString, int startIndex, CellToken cellToken) {
	    char ch;
	    int column = 0;
	    int row = 0;
	    int index = startIndex;

	    // handle a bad startIndex
	    if ((startIndex < 0) || (startIndex >= inputString.length() )) {
	        cellToken.setColumn(BadCell);
	        cellToken.setRow(BadCell);
	        return index;
	    }

	    // get rid of leading whitespace characters
	    while (index < inputString.length() ) {
	        ch = inputString.charAt(index);            
	        if (!Character.isWhitespace(ch)) {
	            break;
	        }
	        index++;
	    }
	    if (index == inputString.length()) {
	        // reached the end of the string before finding a capital letter
	        cellToken.setColumn(BadCell);
	        cellToken.setRow(BadCell);
	        return index;
	    }

	    // ASSERT: index now points to the first non-whitespace character

	    ch = inputString.charAt(index);            
	    // process CAPITAL alphabetic characters to calculate the column
	    if (!Character.isUpperCase(ch)) {
	        cellToken.setColumn(BadCell);
	        cellToken.setRow(BadCell);
	        return index;
	    } else {
	        column = ch - 'A';
	        index++;
	    }

	    while (index < inputString.length() ) {
	        ch = inputString.charAt(index);            
	        if (Character.isUpperCase(ch)) {
	            column = ((column + 1) * 26) + (ch - 'A');
	            index++;
	        } else {
	            break;
	        }
	    }
	    if (index == inputString.length() ) {
	        // reached the end of the string before fully parsing the cell reference
	        cellToken.setColumn(BadCell);
	        cellToken.setRow(BadCell);
	        return index;
	    }

	    // ASSERT: We have processed leading whitespace and the
	    // capital letters of the cell reference

	    // read numeric characters to calculate the row
	    if (Character.isDigit(ch)) {
	        row = ch - '0';
	        index++;
	    } else {
	        cellToken.setColumn(BadCell);
	        cellToken.setRow(BadCell);
	        return index;
	    }

	    while (index < inputString.length() ) {
	        ch = inputString.charAt(index);            
	        if (Character.isDigit(ch)) {
	            row = (row * 10) + (ch - '0');
	            index++;
	        } else {
	            break;
	        }
	    }

	    // successfully parsed a cell reference
	    cellToken.setColumn(column);
	    cellToken.setRow(row);
	    return index;
	}


	
/*	public void addDependent(CellToken dependent)
	{
		dependents.add(dependent);
	}*/
}
