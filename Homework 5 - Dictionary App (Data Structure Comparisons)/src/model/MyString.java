package model;

import java.lang.Comparable;

/**
 * MyString mimics the Java String class, but it
 * is also declared to implement Comparable and Hashable.
 * 
 * @author Donald Chinn
 * @version October 29, 2002
 */
public class MyString implements Comparable, Hashable {
    
    private String value;
    
    /**
     * Construct the MyString object with initial value "".
     */
    public MyString( ) {
        this( "" );
    }

    /**
     * Construct the MyString object.
     * @param x the initial value.
     */
    public MyString( String x ) {
        value = x;
    }

    /**
     * Gets the stored String value.
     * @return the stored value.
     */
    public String stringValue( ) {
        return value;
    }

    /**
     * Implements the toString method.
     * @return the String representation.
     */
    public String toString( ) {
        return value;
    }

    /**
     * Implements the compareTo method.
     * @param rhs the other MyString object.
     * @return 0 if two objects are equal;
     *     less than zero if this object is smaller;
     *     greater than zero if this object is larger.
     * @exception ClassCastException if rhs is not
     *     a MyString.
     */
    public int compareTo( Object rhs ) {
        String thisString = this.value;
        String rhsString = ((MyString) rhs).value;
        return thisString.compareTo(rhsString);
    }

    /**
     * Implements the equals method.
     * @param rhs the second MyInteger.
     * @return true if the objects are equal, false otherwise.
     * @exception ClassCastException if rhs is not
     *     a MyInteger.
     */
    public boolean equals( Object rhs ) {
        return (rhs != null) && (value.equals(((MyString)rhs).value));
    }

    /**
     * Implements the hash method.
     * @param tableSize the hash table size.
     * @return a number between 0 and tableSize-1.
     */
    public int hash( int tableSize ) {
        return QuadraticProbingHashTable.hash(value, tableSize);
    }

}
