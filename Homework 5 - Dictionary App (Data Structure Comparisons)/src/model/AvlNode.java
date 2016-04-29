package model;

// Basic node stored in AVL trees
// Note that this class is not accessible outside
// of package DataStructures

class AvlNode {
	int counter; //edited this
    // Constructors
    AvlNode( Comparable theElement ) {
        this( theElement, null, null );
        counter = 1; //edited this
    }

    AvlNode( Comparable theElement, AvlNode lt, AvlNode rt ) {
        element  = theElement;
        left     = lt;
        right    = rt;
        height   = 0;
        counter = 1; //edited this
    }

    // Friendly data; accessible by other package routines
    Comparable element;      // The data in the node
    AvlNode    left;         // Left child
    AvlNode    right;        // Right child
    int        height;       // Height
}
