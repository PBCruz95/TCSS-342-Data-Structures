
public class CellToken extends TokenAbstract {

    private int column;
    private int row;
    
    public CellToken() {
        // TODO Auto-generated constructor stub
    }
    
    public CellToken(int theRow, int theColumn) {
        row = theRow;
        column = theColumn;
    }
    
    void setRow(int theRow) {
        row = theRow;
    }
    
    void setColumn(int theColumn) {
        column = theColumn;
    }
    
    int getRow() {
        return row;
    }
    
    int getCol() {
        return column;
    }
    
    
    /**
     *  Given a CellToken, print it out as it appears on the
     *  spreadsheet (e.g., "A3")
     *  @param cellToken  a CellToken
     *  @return  the cellToken's coordinates
     */
    static String printCellToken (CellToken cellToken) {
        char ch;
        String returnString = "";
        int col;
        int largest = 26;  // minimum col number with number_of_digits digits
        int number_of_digits = 2;

        col = cellToken.getCol();

        // compute the biggest power of 26 that is less than or equal to col
        // We don't check for overflow of largest here.
        while (largest <= col) {
            largest = largest * 26;
            number_of_digits++;
        }
        largest = largest / 26;
        number_of_digits--;

        // append the column label, one character at a time
        while (number_of_digits > 1) {
            ch = (char) (((col / largest) - 1) + 'A');
            returnString += ch;
            col = col % largest;
            largest = largest  / 26;
            number_of_digits--;
        }

        // handle last digit
        ch = (char) (col + 'A');
        returnString += ch;

        // append the row as an integer
        returnString += cellToken.getRow();

        return returnString;
    }
    
    @Override
	public String toString()
    {
    	return "(" + row + "," + column + ")";
    }

}
