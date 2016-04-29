package model;

// Basic node stored in unbalanced binary search trees
// Note that this class is not accessible outside
// of package DataStructures

class BinaryNode {
	int counter; // edited this
    // Constructors
    BinaryNode( Comparable theElement ) {
        this( theElement, null, null );
        counter = 1; //edited this
    }

    BinaryNode( Comparable theElement, BinaryNode lt, BinaryNode rt ) {
        element  = theElement;
        left     = lt;
        right    = rt;
        counter = 1; //edited this
    }

    // Friendly data; accessible by other package routines
    Comparable element;      // The data in the node
    BinaryNode left;         // Left child
    BinaryNode right;        // Right child
}
