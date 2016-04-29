package model;

import java.util.NoSuchElementException;

/**
 * List ADT implemented using a linked list
 *
 * @author TCSS 342
 * @version 1.0
 */
public class LinkedList {

	private ListNode head; // the first (empty) node of the list

	class Iterator implements java.util.Iterator {

		private ListNode node;

		public Iterator() {
			node = head.getNext(); // point to the first "real" element
		}

		public Iterator(ListNode node) {
			this.node = node;
		}

		/**
		 * Is there another element in the list?
		 * 
		 * @return true iff there is another element in the list
		 */
		public boolean hasNext() {
			return node != null;
		}

		/**
		 * Return the current element and advance the pointer to the next
		 * element.
		 * 
		 * @return the current element
		 */
		public Object next() {
			if (node != null) {
				Object returnElement = node.getElement();
				node = node.getNext();
				return returnElement;
			} else {
				throw new NoSuchElementException();
			}
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public ListNode getNode() {
			return node;
		}

		public Object getElement() {
			if (node != null) {
				Object returnElement = node.getElement();
				return returnElement;
			} else {
				throw new NoSuchElementException();
			}
		}
	}

	/**
	 * Return an iterator that points to the beginning of the list.
	 * 
	 * @return iterator of the list
	 */
	public Iterator iterator() {
		return new Iterator();
	}

	/**
	 * Return an Iterator that points to the (empty) header element.
	 * 
	 * @return Iterator to the header element
	 */
	public Iterator zeroth() {
		return new Iterator(head);
	}

	/**
	 * The first ListNode in the LinkedList has a null element as its contents.
	 */
	public LinkedList() {
		head = new ListNode(null);
	}

	/**
	 * Is the list empty?
	 * 
	 * @return true iff the list is empty
	 */
	public boolean isEmpty() {
		return head.getNext() == null;
	}

	/**
	 * Empty the list.
	 */
	public void makeEmpty() {
		head.setNext(null);
	}

	/**
	 * Insert an element after an Iterator position.
	 * 
	 * @param element
	 *            the element to insert
	 * @param pointer
	 *            the place in the list to insert after
	 */
	public void insert(Object element, Iterator pointer) {
		if (pointer != null) {
			ListNode node = pointer.getNode();
			if (node != null) {
				node.setNext(new ListNode(element, node.getNext()));
			}
		}
	}

	/**
	 * Remove the element after an Iterator's position.
	 * 
	 * @param pointer
	 *            the place in the list to remove AFTER
	 */
	public void remove(Iterator pointer) {
		if (pointer != null) {
			ListNode iterElement = (ListNode) pointer.getNode();
			if ((iterElement != null) && (iterElement.getNext() != null)) {
				iterElement.setNext(iterElement.getNext().getNext());
			}
		}
	}
}
