

public class LiteralToken extends TokenAbstract {
    
    private int value;

    public LiteralToken() {
        // TODO Auto-generated constructor stub
    }
    
    public LiteralToken(int theValue) {
        value = theValue;
    }
    
    void setValue(int theValue) {
        value = theValue;
    }
    
    int getValue() {
        return value;
    }
    
    @Override
	public String toString()
    {
    	return Integer.toString(value);
    }

}
