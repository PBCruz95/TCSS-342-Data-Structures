package model;

import java.lang.Comparable;
import java.util.ArrayList;

// AvlTree class
//
// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x (unimplemented)
// Comparable find( x )   --> Return item that matches x
// Comparable findMin( )  --> Return smallest item
// Comparable findMax( )  --> Return largest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// void printTree( )      --> Print tree in sorted order

/**
 * Implements an AVL tree. Note that all "matching" is based on the compareTo
 * method.
 * 
 * @author Mark Allen Weiss
 */
public class AvlTree {

	/** The tree root. */
	private AvlNode root;

	/**
	 * Construct the tree.
	 */
	public AvlTree() {
		root = null;
	}

	/**
	 * Insert into the tree; duplicates are ignored.
	 * 
	 * @param x
	 *            the item to insert.
	 */
	public void insert(Comparable x) {
		root = insert(x, root);
	}

	/**
	 * Remove from the tree. Nothing is done if x is not found.
	 * 
	 * @param x
	 *            the item to remove.
	 */
	public void remove(Comparable x) {
		System.out.println("Sorry, remove unimplemented");
	}

	/**
	 * Find the smallest item in the tree.
	 * 
	 * @return smallest item or null if empty.
	 */
	public Comparable findMin() {
		return elementAt(findMin(root));
	}

	/**
	 * Find the largest item in the tree.
	 * 
	 * @return the largest item of null if empty.
	 */
	public Comparable findMax() {
		return elementAt(findMax(root));
	}

	/**
	 * Find an item in the tree.
	 * 
	 * @param x
	 *            the item to search for.
	 * @return the matching item or null if not found.
	 */
	public Comparable find(Comparable x) {
		return elementAt(find(x, root));
	}

	/**
	 * Make the tree logically empty.
	 */
	public void makeEmpty() {
		root = null;
	}

	/**
	 * Test if the tree is logically empty.
	 * 
	 * @return true if empty, false otherwise.
	 */
	public boolean isEmpty() {
		return root == null;
	}

	/**
	 * Print the tree contents in sorted order.
	 */
	public void printTree() {
		if (isEmpty())
			System.out.println("Empty tree");
		else
			printTree(root);
	}

	/**
	 * Internal method to get element field.
	 * 
	 * @param t
	 *            the node.
	 * @return the element field or null if t is null.
	 */
	private Comparable elementAt(AvlNode t) {
		return t == null ? null : t.element;
	}

	/**
	 * Internal method to insert into a subtree.
	 * 
	 * @param x
	 *            the item to insert.
	 * @param t
	 *            the node that roots the tree.
	 * @return the new root.
	 */
	private AvlNode insert(Comparable x, AvlNode t) {
		if (t == null) {
			t = new AvlNode(x, null, null);
		} else if (x.compareTo(t.element) < 0) {
			t.left = insert(x, t.left);
			if (height(t.left) - height(t.right) == 2)
				if (x.compareTo(t.left.element) < 0)
					t = rotateWithLeftChild(t);
				else
					t = doubleWithLeftChild(t);
		} else if (x.compareTo(t.element) > 0) {
			t.right = insert(x, t.right);
			if (height(t.right) - height(t.left) == 2)
				if (x.compareTo(t.right.element) > 0)
					t = rotateWithRightChild(t);
				else
					t = doubleWithRightChild(t);
		} else {
			t.counter++; // Duplicate; increment t's counter (I edited this)
		}
		t.height = max(height(t.left), height(t.right)) + 1;
		return t;
	}

	/**
	 * Internal method to find the smallest item in a subtree.
	 * 
	 * @param t
	 *            the node that roots the tree.
	 * @return node containing the smallest item.
	 */
	private AvlNode findMin(AvlNode t) {
		if (t == null)
			return t;

		while (t.left != null)
			t = t.left;
		return t;
	}

	/**
	 * Internal method to find the largest item in a subtree.
	 * 
	 * @param t
	 *            the node that roots the tree.
	 * @return node containing the largest item.
	 */
	private AvlNode findMax(AvlNode t) {
		if (t == null)
			return t;
		while (t.right != null)
			t = t.right;
		return t;
	}

	/**
	 * Internal method to find an item in a subtree.
	 * 
	 * @param x
	 *            is item to search for.
	 * @param t
	 *            the node that roots the tree.
	 * @return node containing the matched item.
	 */
	private AvlNode find(Comparable x, AvlNode t) {
		while (t != null)
			if (x.compareTo(t.element) < 0)
				t = t.left;
			else if (x.compareTo(t.element) > 0)
				return t; // Match
		return null; // No match
	}

	/**
	 * Internal method to print a subtree in sorted order.
	 * 
	 * @param t
	 *            the node that roots the tree.
	 */
	private void printTree(AvlNode t) {
		if (t != null) {
			printTree(t.left);
			System.out.println(t.element);
			printTree(t.right);
		}
	}

	/**
	 * Internal method to print a subtree rooted at t, indicating depth.
	 * 
	 * @param t
	 *            the node that roots the (sub)tree
	 * @param depth
	 *            the current depth to print the subtree
	 */
	private void printTreeDepth(AvlNode t, int depth) {
		if (t != null) {
			printTreeDepth(t.right, depth + 1);
			for (int i = 0; i < depth; i++) {
				System.out.print("  ");
			}
			System.out.println(t.element);
			printTreeDepth(t.left, depth + 1);
		}
	}

	/**
	 * Return the height of node t, or -1, if null.
	 */
	private static int height(AvlNode t) {
		return t == null ? -1 : t.height;
	}

	/**
	 * Return maximum of lhs and rhs.
	 */
	private static int max(int lhs, int rhs) {
		return lhs > rhs ? lhs : rhs;
	}

	/**
	 * Rotate binary tree node with left child. For AVL trees, this is a single
	 * rotation for case 1. Update heights, then return new root.
	 */
	private static AvlNode rotateWithLeftChild(AvlNode k2) {
		AvlNode k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		k2.height = max(height(k2.left), height(k2.right)) + 1;
		k1.height = max(height(k1.left), k2.height) + 1;
		return k1;
	}

	/**
	 * Rotate binary tree node with right child. For AVL trees, this is a single
	 * rotation for case 4. Update heights, then return new root.
	 */
	private static AvlNode rotateWithRightChild(AvlNode k1) {
		AvlNode k2 = k1.right;
		k1.right = k2.left;
		k2.left = k1;
		k1.height = max(height(k1.left), height(k1.right)) + 1;
		k2.height = max(height(k2.right), k1.height) + 1;
		return k2;
	}

	/**
	 * Double rotate binary tree node: first left child with its right child;
	 * then node k3 with new left child. For AVL trees, this is a double
	 * rotation for case 2. Update heights, then return new root.
	 */
	private static AvlNode doubleWithLeftChild(AvlNode k3) {
		k3.left = rotateWithRightChild(k3.left);
		return rotateWithLeftChild(k3);
	}

	/**
	 * Double rotate binary tree node: first right child with its left child;
	 * then node k1 with new right child. For AVL trees, this is a double
	 * rotation for case 3. Update heights, then return new root.
	 */
	private static AvlNode doubleWithRightChild(AvlNode k1) {
		k1.right = rotateWithLeftChild(k1.right);
		return rotateWithRightChild(k1);
	}

	/**
	 * Print the most frequently occurring words in the tree, along with their
	 * frequency.
	 */
	public void PrintMostFrequent() {
		ArrayList<AvlNode> nodeArray = new ArrayList<>();
		nodeArray = GetMostFrequent(root, nodeArray);
		for (int i = 0; i < nodeArray.size(); i++) {
			System.out.println(nodeArray.get(i).element + " : " + nodeArray.get(i).counter);
		}
	}

	/**
	 * Recursively gets the 10 most frequently occurring words in the tree and their
	 * frequency.
	 */
	//NOTE: I changed getWord() in the dictionary app so that it would always return a string with all lowercase letters.
	public ArrayList<AvlNode> GetMostFrequent(AvlNode node, ArrayList<AvlNode> array) {
		if (node == null) {
			return array;
		}
		if (array.isEmpty()) {
			array.add(node);
		}
		for (int i = 0; i < array.size(); i++) {
			if (node.counter > array.get(i).counter) {
				array.add(i, node);
				break;
			}
		}
		if (array.size() < 10) {
			array.add(node);
		}
		if (array.size() == 11) { // For this particular assignment, our word
									// limit is 10. So we shouldn't have 11 or
									// more words in the list.
			array.remove(array.size() - 1); // removes the lowest on the list
		}
		GetMostFrequent(node.left, array);
		GetMostFrequent(node.right, array);
		return array;
	}

	// a test program
	public static void test() {
		AvlTree avltree = new AvlTree();

		avltree.insert(new MyString("Happy"));
		avltree.insert(new MyString("weird"));
		avltree.insert(new MyString("sad"));
		avltree.insert(new MyString("Acropolis"));
		avltree.insert(new MyString("ZZZZZZYY"));
		avltree.printTreeDepth(avltree.root, 0);
	}

	// Test program
	public static void main(String[] args) {
		AvlTree t = new AvlTree();
		final int NUMS = 4000;
		final int GAP = 37;
		System.out.println("Checking... (no more output means success)");

		for (int i = GAP; i != 0; i = (i + GAP) % NUMS)
			t.insert(new Integer(i));
		if (NUMS < 40)
			t.printTree();
		if (((Integer) (t.findMin())).intValue() != 1 || ((Integer) (t.findMax())).intValue() != NUMS - 1)
			System.out.println("FindMin or FindMax error!");

		for (int i = 1; i < NUMS; i++)
			if (((Integer) (t.find(new Integer(i)))).intValue() != i)
				System.out.println("Find error1!");
	}

}
