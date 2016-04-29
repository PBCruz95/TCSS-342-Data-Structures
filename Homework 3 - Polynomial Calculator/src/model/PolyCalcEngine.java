package model;

/**
 * The part of the polynomial calculator that responds to user input by using
 * the Polynomial ADT.
 * 
 * @author TCSS 342
 * @version 1.0
 */
public class PolyCalcEngine {

	private Polynomial aPoly;
	private Polynomial bPoly;

	public PolyCalcEngine() {
		aPoly = new Polynomial();
		bPoly = new Polynomial();
	}

	/**
	 * Set A to zero.
	 */
	public void zeroA() {
		aPoly.zeroPolynomial();
	}

	/**
	 * Set B to zero.
	 */
	public void zeroB() {
		bPoly.zeroPolynomial();
	}

	/**
	 * Set A to the negation of A.
	 */
	public void negateA() {
		aPoly = aPoly.negate();
	}

	/**
	 * Set B to the negation of B.
	 */
	public void negateB() {
		bPoly = bPoly.negate();
	}

	/**
	 * Add a term (coef, exp) to A.
	 */
	public void addTermA(int coefficient, int exponent) {
		aPoly.insertTerm(coefficient, exponent);
	}

	/**
	 * Add a term (coef, exp) to B.
	 */
	public void addTermB(int coefficient, int exponent) {
		bPoly.insertTerm(coefficient, exponent);
	}

	/**
	 * Add A to B and put the result in A.
	 */
	public void addAAB() {
		aPoly = aPoly.plus(bPoly);
	}

	/**
	 * Add A to B and put the result in B.
	 */
	public void addBAB() {
		bPoly = aPoly.plus(bPoly);
	}

	/**
	 * Add A from B and put the result in A.
	 */
	public void subtractAB() {
		aPoly = aPoly.minus(bPoly);
	}

	/**
	 * Multiply A and B and put the result in A.
	 */
	public void multiplyAB() {
		aPoly = aPoly.times(bPoly);
	}

	/**
	 * Take the derivative of A and put the result in B.
	 */
	public void derivativeA() {
		bPoly = aPoly.derivative();
	}

	/**
	 * Put B's value into A and A's value into B.
	 */
	public void swapAB() {
		Polynomial tempPoly;
		tempPoly = aPoly;
		aPoly = bPoly;
		bPoly = tempPoly;
	}

	/**
	 * Return the String representation of A.
	 * 
	 * @return String representation of A
	 */
	public String getAPolyValue() {
		return aPoly.print();
	}

	/**
	 * Return the String representation of B.
	 * 
	 * @return String representation of B
	 */
	public String getBPolyValue() {
		return bPoly.print();
	}
}
