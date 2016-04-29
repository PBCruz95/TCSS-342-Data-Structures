package model;

/**
 * Basic node stored in a linked list. Note that this class is not accessible
 * outside of package DataStructures.
 *
 * @author TCSS 342
 * @version 1.0
 */

public class ListNode {

	private Object element;
	private ListNode next;

	/**
	 * Construct a list node with an element and next pointer null.
	 * 
	 * @param theElement
	 *            the contents of the node.
	 */
	public ListNode(Object theElement) {
		this(theElement, null);
	}

	/**
	 * Construct a list node with an element and next pointer pointing to
	 * another list node.
	 * 
	 * @param theElement
	 *            the contents of the node
	 * @param n
	 *            list node this node should point to
	 */
	public ListNode(Object theElement, ListNode n) {
		element = theElement;
		next = n;
	}

	/**
	 * Get the element of the ListNode.
	 * 
	 * @return the element
	 */
	public Object getElement() {
		return element;
	}

	/**
	 * Set the element of the ListNode.
	 * 
	 * @param x
	 *            the new element
	 */
	public void setElement(Object x) {
		element = x;
	}

	/**
	 * Get the next element of the ListNode.
	 * 
	 * @return the next element
	 */
	public ListNode getNext() {
		return next;
	}

	/**
	 * Set the next pointer of the ListNode.
	 * 
	 * @param x
	 *            the next ListNode
	 */
	public void setNext(ListNode x) {
		next = x;
	}
}
