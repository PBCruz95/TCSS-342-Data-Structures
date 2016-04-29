

public class OperatorToken extends TokenAbstract {
    
    public static final char PLUS = '+';
    public static final char MINUS = '-';
    public static final char MULT = '*';
    public static final char DIV = '/';
    public static final char LEFT_PAREN = '(';
    
    private char operatorToken;


    public OperatorToken() {
        // TODO Auto-generated constructor stub
    }
    
    public OperatorToken(char theOperator) {
        operatorToken = theOperator;
    }

    void setOperatorToken(char theOperator) {
        operatorToken = theOperator;
    }
    
    char getOperatorToken() {
        return operatorToken;
    }
    
    @Override
	public String toString()
    {
    	return Character.toString(operatorToken);
    }
}
