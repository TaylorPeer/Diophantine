package diophantine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import diophantine.Diophantine;
import diophantine.model.Equation;
import diophantine.solvers.BruteForceSolver;
import diophantine.solvers.GeneticSolver;

public class TestEquationSolvers {

	private GeneticSolver geneticSolver;

	private BruteForceSolver bruteForceSolver;

	private static List<String> equationStrings;

	@BeforeClass
	public static void initializeEquations() {
		equationStrings = new ArrayList<String>();
		equationStrings.add("a+2b-3c+4d=30");
		equationStrings.add("a+2b+3c+4d+5e+6f+7g=450");
		equationStrings.add("a+2b+3c+4d+5e+6f+7g+8h=550");
		equationStrings.add("a+2b+3c+4d+5e+6f+7g+8h+9i+10j=1000");
		equationStrings.add("a+2b+3c+4d+5e+6f+7g+8h+9i+10j+11k+5l+4m+6n+2o-7p+q=1000");
	}

	@Test
	public void testAllSolvers() {

		long startTime;
		long endTime;
		long elapsedTime;

		for (String equationString : equationStrings) {

			System.out.println("Equation to solve: " + equationString);

			Equation equation = Diophantine.parseEquationString(equationString);

			// Genetic Algorithm
			startTime = Calendar.getInstance().getTimeInMillis();
			geneticSolver = new GeneticSolver(equation, 25, 0.5, 0.2);
			geneticSolver.solve();
			endTime = Calendar.getInstance().getTimeInMillis();
			elapsedTime = endTime - startTime;
			System.out.println("(Compeleted in " + elapsedTime + " milliseconds)");

			// Brute Force
			startTime = Calendar.getInstance().getTimeInMillis();
			bruteForceSolver = new BruteForceSolver(equation);
			bruteForceSolver.solve();
			endTime = Calendar.getInstance().getTimeInMillis();
			elapsedTime = endTime - startTime;
			System.out.println("(Compeleted in " + elapsedTime + " milliseconds)");
			System.out.println("---");

		}

	}

	@Ignore
	public void logExecutionTime() {

		int repitions = 50;
		String equationString = equationStrings.get(equationStrings.size() - 1);
		Equation equation = Diophantine.parseEquationString(equationString);

		long startTime;
		long endTime;

		// Run the algorithm with various parameter combinations
		for (double mutationRate = 0.2; mutationRate <= 0.51; mutationRate = mutationRate + 0.02) {

			for (double survivalRate = 0.1; survivalRate <= 0.51; survivalRate = survivalRate + 0.02) {

				String mutationRateString = String.format("%.2f", mutationRate);
				String survivalRateString = String.format("%.2f", survivalRate);

				System.out.print(mutationRateString + ", " + survivalRateString + ", ");

				long totalTime = 0;

				for (int repition = 0; repition < repitions; repition++) {

					startTime = Calendar.getInstance().getTimeInMillis();
					GeneticSolver geneticSolver = new GeneticSolver(equation, 50, mutationRate, survivalRate);
					geneticSolver.solve();
					endTime = Calendar.getInstance().getTimeInMillis();
					totalTime += endTime - startTime;
				}

				System.out.print(totalTime / repitions + "\n");
			}
		}

	}

}
