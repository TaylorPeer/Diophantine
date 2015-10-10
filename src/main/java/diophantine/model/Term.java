package diophantine.model;

public class Term {

	private String variable;

	private int coefficient = 0;

	/**
	 * @return the variable
	 */
	public String getVariable() {
		return variable;
	}

	/**
	 * @param variable
	 *            the variable to set
	 */
	public void setVariable(String variable) {
		this.variable = variable;
	}

	/**
	 * @return the coefficient
	 */
	public int getCoefficient() {
		return coefficient;
	}

	/**
	 * @param coefficient
	 *            the coefficient to set
	 */
	public void setCoefficient(int coefficient) {
		this.coefficient = coefficient;
	}

}
