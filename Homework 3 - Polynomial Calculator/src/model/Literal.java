package model;

/**
 * A Literal in a Polynomial, consisting of an integer coefficient and an
 * integer exponent.
 * 
 * @author TCSS 342
 * @version 1.0
 */

public class Literal {

	private int coefficient;
	private int exponent;

	public Literal() {
		coefficient = 0;
		exponent = 0;
	}

	public Literal(int coefficient, int exponent) {
		this.coefficient = coefficient;
		this.exponent = exponent;
	}

	// accessor methods

	public int getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(int coefficient) {
		this.coefficient = coefficient;
	}

	public int getExponent() {
		return exponent;
	}

	public void setExponent(int exponent) {
		this.exponent = exponent;
	}
}
