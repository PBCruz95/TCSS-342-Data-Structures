package model;

// The basic entry stored in ProbingHashTable

class HashEntry {
    Hashable element;   // the element
    boolean  isActive;  // false is deleted
    int counter; //edited this

    public HashEntry( Hashable e ) {
        this( e, true );
        counter = 1; //edited this
    }

    public HashEntry( Hashable e, boolean i ) {
        element   = e;
        isActive  = i;
        counter = 1; //edited this
    }
}
