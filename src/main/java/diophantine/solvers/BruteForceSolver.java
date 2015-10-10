package diophantine.solvers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import diophantine.model.Equation;
import diophantine.model.Term;

/**
 * Brute force Diophantine equation solver.
 * 
 * @author taylorpeer
 */
public class BruteForceSolver implements Solver {
	
	private static final int TIMEOUT_MS = 3000;

	private Equation equation;

	private int maxTermValue;

	public BruteForceSolver(Equation equation) {
		this.equation = equation;
		this.maxTermValue = equation.getTerms().size() + 1;
	}

	public void solve() {
		
		long startTime = Calendar.getInstance().getTimeInMillis();;
		
		List<Term> terms = equation.getTerms();
		int value = equation.getValue();
		Map<Term, Integer> termValues = new LinkedHashMap<Term, Integer>();
		for (Term term : terms) {
			termValues.put(term, 1);
		}
		while (!checkSolution(termValues, value)) {
			long currentTime = Calendar.getInstance().getTimeInMillis();;
			long elapsedTime = currentTime - startTime;
			if (elapsedTime >= TIMEOUT_MS) {
				System.out.println("- Brute force algorithm terminated prematurely due to timeout. No Solution found.");
				return;
			}
			int index = termValues.size() - 1;
			termValues = incrementTermValues(termValues, index);
		}
		System.out.println("- Solution found via brute force: ");
		for (Map.Entry<Term, Integer> entry : termValues.entrySet()) {
			Term term = entry.getKey();
			Integer termValue = entry.getValue();
			System.out.println("\t" + term.getVariable() + ": " + termValue);
		}
	}

	/**
	 * Checks if a given collection of term values solves the configured equation.
	 * 
	 * @param termValues
	 * @param value
	 * @return
	 */
	private boolean checkSolution(Map<Term, Integer> termValues, int value) {
		int total = 0;
		for (Map.Entry<Term, Integer> entry : termValues.entrySet()) {
			Term term = entry.getKey();
			Integer termValue = entry.getValue();
			total += (term.getCoefficient() * termValue);
		}
		return total == value;
	}

	/**
	 * Increments the term values to advance the search.
	 * 
	 * @param termValues
	 * @param index
	 * @return
	 */
	private Map<Term, Integer> incrementTermValues(Map<Term, Integer> termValues, int index) {

		Term term = (new ArrayList<Term>(termValues.keySet())).get(index);
		Integer value = (new ArrayList<Integer>(termValues.values())).get(index);
		value++;

		// Check if the end of the search space has been reached
		if (index == 0 && value > maxTermValue) {
			// Reset and star over with larger search space
			for (Term originalTerm : equation.getTerms()) {
				termValues.put(originalTerm, 1);
			}
			maxTermValue = maxTermValue + 10;
		} else {
			if (value > maxTermValue) {
				value = 1;
				termValues = incrementTermValues(termValues, index - 1);
			}
			termValues.put(term, value);
		}

		return termValues;
	}

}
