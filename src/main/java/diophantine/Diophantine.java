package diophantine;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import diophantine.model.Equation;
import diophantine.model.Term;
import diophantine.solvers.GeneticSolver;

/**
 * TODO
 * 
 * @author taylorpeer
 */
public class Diophantine {

	private static final String ALPHA_NUMERIC_REGEX = "[^A-Za-z0-9]";

	private static final String NUMERIC_REGEX = ".*\\d+.*";

	/**
	 * Accepts a diophantine equation as a string and attempts to find a solution for it.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		String equationString = args[0];
		equationString.replace(" ", "");

		Equation equation = parseEquationString(equationString);
		GeneticSolver geneticSolver = new GeneticSolver(equation, 50, 0.5, 0.1);
		geneticSolver.solve();
		
	}

	/**
	 * TODO
	 * 
	 * @param part
	 * @return
	 */
	private static String cleanupEquationPart(String part) {
		part = part.replaceAll(ALPHA_NUMERIC_REGEX, "");
		// If no coefficient was found, assume it is 1 and add to the string
		if (!part.substring(0, 1).matches(NUMERIC_REGEX)) {
			part = "1" + part;
		}
		return part;
	}

	/**
	 * Parses the equation string and creates an equation object out of it.
	 * 
	 * @param equationString
	 * @return
	 */
	public static Equation parseEquationString(String equationString) {
		Equation equation = new Equation();

		// Split equation string on operators without removing them from the parts
		String[] equationParts = equationString.split("(?=\\+|\\-|=)");
		for (String part : equationParts) {
			if (part.contains("=")) {
				// Set value of equation (i.e. the right side of the equation)
				part = part.replace("=", "");
				int value = Integer.parseInt(part);
				equation.setValue(value);
			} else {
				Term term = new Term();
				boolean positiveTerm = true;
				if (part.contains("-")) {
					positiveTerm = false;
				}
				part = cleanupEquationPart(part);
				Pattern p = Pattern.compile("\\p{L}");
				Matcher m = p.matcher(part);
				if (m.find()) {
					int firstLetterIndex = m.start();
					String coefficientString = part.substring(0, firstLetterIndex);
					String variableString = part.substring(firstLetterIndex);
					int coefficient = Integer.parseInt(coefficientString);
					if (!positiveTerm) {
						coefficient = coefficient * -1;
					}
					term.setVariable(variableString);
					term.setCoefficient(coefficient);
				}
				equation.addTerm(term);
			}
		}
		return equation;
	}

}
