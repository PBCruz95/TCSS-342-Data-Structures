/*
 * Patrick Cruz - Homework #3
 */
package model;

public class Polynomial {
	private LinkedList termList;

	public Polynomial() {
		termList = new LinkedList();
	}

	/**
	 * Inserts a ListNode into a LinkedList. The ListNode will contain a
	 * Literal. This will automatically organize the list from greatest to
	 * smallest exponent.
	 * 
	 * @param coefficient
	 *            the literal's coefficient
	 * @param exponent
	 *            the literal's exponent
	 */
	public void insertTerm(int coefficient, int exponent) {
		if (coefficient == 0) { // Cannot have 0 coefficient.
			return;
		}
		LinkedList.Iterator literator = termList.zeroth();
		Literal newTerm = new Literal(coefficient, exponent);

		while (literator.hasNext()) {
			ListNode nextNode = literator.getNode().getNext();

			if (nextNode == null) { // Nothing to compare to; immediately insert
									// to the end.
				break;
			}
			Literal nextTerm = (Literal) nextNode.getElement();

			if (nextTerm.getExponent() < newTerm.getExponent()) {
				termList.insert(newTerm, literator);
				return;

			} else if (nextTerm.getExponent() == newTerm.getExponent()) {
				nextTerm.setCoefficient(nextTerm.getCoefficient() + coefficient);
				return;
			}
			literator.next();
		}
		termList.insert(newTerm, literator);
	}

	/**
	 * Set the polynomial to 0.
	 */
	public void zeroPolynomial() {
		termList.makeEmpty();
	}

	/**
	 * Negate all terms in a polynomial, making positives negative and negatives
	 * positive.
	 * 
	 * @return the negated polynomial
	 */
	public Polynomial negate() {
		Polynomial polyClone = new Polynomial();
		Polynomial outPoly = new Polynomial();
		LinkedList.Iterator thiserator = this.termList.iterator();

		while (thiserator.hasNext()) {
			Literal curTerm = (Literal) thiserator.next();
			polyClone.insertTerm(curTerm.getCoefficient(), curTerm.getExponent());
		}

		LinkedList.Iterator cloneIterator = polyClone.termList.iterator();
		Literal curTerm;

		while (cloneIterator.hasNext()) {
			curTerm = (Literal) cloneIterator.next();
			outPoly.insertTerm(-curTerm.getCoefficient(), curTerm.getExponent());
		}
		return outPoly;
	}

	/**
	 * Adds this polynomial to another polynomial.
	 * 
	 * @param addPoly
	 *            the polynomial to be added to this one
	 * @return the sum of the two polynomials
	 */
	public Polynomial plus(Polynomial addPoly) {
		Polynomial outPoly = new Polynomial();
		LinkedList.Iterator thiserator = this.termList.iterator();

		while (thiserator.hasNext()) {
			Literal curTerm = (Literal) thiserator.next();
			outPoly.insertTerm(curTerm.getCoefficient(), curTerm.getExponent());
		}

		LinkedList.Iterator outIterator = outPoly.termList.zeroth();
		LinkedList.Iterator addIterator = addPoly.termList.iterator();

		if (outIterator.getNode().getNext() == null) { // Adding to the 0 poly
			return addPoly;
		}

		Literal addTerm = advanceAddTerm(addIterator);
		ListNode nextNode;
		ListNode newNode;
		Literal nextTerm;
		int addExpo;
		int nextExpo;

		while (addTerm != null && outIterator.hasNext()) {
			nextNode = outIterator.getNode().getNext();

			if (nextNode == null) { // Add newNode to the end
				newNode = new ListNode(addTerm);
				outIterator.getNode().setNext(newNode);
				addTerm = advanceAddTerm(addIterator);
				continue;
			}

			nextTerm = (Literal) nextNode.getElement();
			addExpo = addTerm.getExponent();
			nextExpo = nextTerm.getExponent();

			if (addExpo > nextExpo) {
				outPoly.termList.insert(addTerm, outIterator);
				addTerm = advanceAddTerm(addIterator);

			} else if (addExpo == nextExpo) {
				Literal sumTerm = new Literal(addTerm.getCoefficient() + nextTerm.getCoefficient(),
						addTerm.getExponent());

				if (sumTerm.getCoefficient() != 0) {
					nextNode.setElement(sumTerm);
					outIterator.next();

				} else
					outPoly.termList.remove(outIterator);
				addTerm = advanceAddTerm(addIterator);
				continue;
			}
			outIterator.next();
		}
		return outPoly;
	}

	/**
	 * Advances the addTerm local variable found in the plus method.
	 * 
	 * @param it
	 *            the iterator that goes through the polynomial to be added
	 * @return the next literal in the iterator, if there is one
	 */
	private Literal advanceAddTerm(LinkedList.Iterator it) {
		if (it.hasNext()) {
			return (Literal) it.next();
		} else
			return null;
	}

	/**
	 * Subtracts a polynomial from this one by negating the polynomial and
	 * adding it afterward.
	 * 
	 * @param polynomial
	 *            the polynomial to be subtracted by
	 * @return the difference of the two polynomials
	 */
	public Polynomial minus(Polynomial subtractPoly) {
		return plus(subtractPoly.negate());
	}

	/**
	 * Multiplies this polynomial with another.
	 * 
	 * @param multPoly
	 * @return
	 */
	public Polynomial times(Polynomial multPoly) {
		Polynomial polyClone = new Polynomial();
		LinkedList.Iterator thiserator = this.termList.iterator();

		while (thiserator.hasNext()) {
			Literal curTerm = (Literal) thiserator.next();
			polyClone.insertTerm(curTerm.getCoefficient(), curTerm.getExponent());
		}
		// Nevermind, it's not happening.
		return null;
	}

	/**
	 * Computes this polynomial's derivative.
	 * 
	 * @return the polynomial's derivative
	 */
	public Polynomial derivative() {
		Polynomial polyClone = new Polynomial();
		Polynomial outPoly = new Polynomial();
		LinkedList.Iterator thiserator = this.termList.iterator();

		while (thiserator.hasNext()) {
			Literal curTerm = (Literal) thiserator.next();
			polyClone.insertTerm(curTerm.getCoefficient(), curTerm.getExponent());
		}

		LinkedList.Iterator cloneIterator = polyClone.termList.iterator();
		while (cloneIterator.hasNext()) {
			Literal curTerm = (Literal) cloneIterator.next();
			if (curTerm.getExponent() != 0) {
				outPoly.insertTerm(curTerm.getCoefficient() * curTerm.getExponent(), curTerm.getExponent() - 1);
			}
		}
		return outPoly;
	}

	/**
	 * Returns the polynomial as a string.
	 * 
	 * @return this polynomial's string representation
	 */
	public String print() {
		String polyString = "";
		LinkedList.Iterator literator = termList.iterator();
		if (!literator.hasNext()) {
			return "0";
		}
		Literal curTerm = (Literal) literator.next();
		int curCoef = curTerm.getCoefficient();
		int curExpo = curTerm.getExponent();

		// The lines between this comment and the next while loop account for
		// the first term's sign possible negative sign
		if (curCoef < 0) {
			polyString += "-";
			curCoef = -curCoef;
		}
		if (curCoef != 1 || curExpo == 0) {
			polyString += curCoef;
		}
		if (curExpo == 0) {
		} else if (curExpo == 1) {
			polyString += "x";
		} else if (curExpo < 0) {
			polyString += "x^(" + curExpo + ")";
		} else
			polyString += "x^" + curExpo;

		while (literator.hasNext()) {
			curTerm = (Literal) literator.next();
			curCoef = curTerm.getCoefficient();
			curExpo = curTerm.getExponent();
			if (curCoef < 0) {
				polyString += " - ";
				curCoef = -curCoef;
			} else {
				polyString += " + ";
			}
			if (curCoef != 1 || curExpo == 0) {
				polyString += curCoef;
			}
			if (curExpo == 0) {
			} else if (curExpo == 1) {
				polyString += "x";
			} else if (curExpo < 0) {
				polyString += "x^(" + curExpo + ")";
			} else
				polyString += "x^" + curExpo;

		}
		return polyString;
	}
}
