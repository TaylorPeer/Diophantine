package diophantine.model;

import java.util.ArrayList;
import java.util.List;

public class Equation {

	private List<Term> terms = new ArrayList<Term>();

	private int value;

	/**
	 * @return the terms
	 */
	public List<Term> getTerms() {
		return terms;
	}

	/**
	 * @param term
	 */
	public void addTerm(Term term) {
		terms.add(term);
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

}
