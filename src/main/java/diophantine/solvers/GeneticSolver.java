package diophantine.solvers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import diophantine.model.Equation;
import diophantine.model.Term;

/**
 * Genetic algorithm Diophantine equation solver.
 * 
 * @author taylorpeer
 *
 */
public class GeneticSolver implements Solver {

	private Equation equation;

	private int maxTermValue;

	/**
	 * Number of individuals in the seed generation.
	 */
	private int generationSize;

	/**
	 * Number of individuals to be selected for survival after every generation.
	 */
	private double survivalRate;

	/**
	 * Factor by which term values may be randomized (mutated) during crossover.
	 */
	private double mutationRate;

	public GeneticSolver(Equation equation, int generationSize, double mutationRate,
			double survivalRate) {
		this.equation = equation;
		this.generationSize = generationSize;
		this.mutationRate = mutationRate;
		this.survivalRate = survivalRate;
		this.maxTermValue = equation.getTerms().size() * 10;
	}

	public void solve() {
		Generation currentGeneration = new Generation();

		// Generate first generation randomly
		for (int i = 0; i < generationSize; i++) {
			currentGeneration.addIndividual(createRandomIndividual());
		}

		// Search for solution
		while (!currentGeneration.checkForSolution()) {
			currentGeneration.generateNextGeneration();
		}
	}

	/**
	 * Creates an individual potential solution with randomized term values.
	 * 
	 * @return
	 */
	private Individual createRandomIndividual() {
		Map<Term, Integer> termValues = new LinkedHashMap<Term, Integer>();
		for (Term term : equation.getTerms()) {
			int value = ThreadLocalRandom.current().nextInt(0, maxTermValue + 1);
			termValues.put(term, value);
		}
		Individual individual = new Individual();
		individual.setTermValues(termValues);
		return individual;
	}

	/**
	 * An individual representing a potential solution to the configured equation.
	 * 
	 * @author taylorpeer
	 */
	private class Individual {

		private Map<Term, Integer> termValues = new LinkedHashMap<Term, Integer>();

		/**
		 * Returns the fitness value of this individual. The fitness value is a distance measure from a valid solution,
		 * so the closer the fitness is to 0, the better.
		 * 
		 * @param targetValue
		 * @return
		 */
		public int getFitness(int targetValue) {
			int total = 0;
			for (Map.Entry<Term, Integer> entry : termValues.entrySet()) {
				Term term = entry.getKey();
				Integer termValue = entry.getValue();
				total += (term.getCoefficient() * termValue);
			}
			return Math.abs(targetValue - total);
		}

		public Map<Term, Integer> getTermValues() {
			return termValues;
		}

		public void setTermValues(Map<Term, Integer> termValues) {
			this.termValues = termValues;
		}

	}

	/**
	 * Collection of individuals that represent a generation of the genetic algorithm.
	 * 
	 * @author taylorpeer
	 */
	private class Generation {

		private int count = 1;

		private List<Individual> individuals = new ArrayList<Individual>();

		/**
		 * Checks if an individual of the population contains a solution to the configured equation.
		 * 
		 * @return
		 */
		public boolean checkForSolution() {
			Collections.sort(individuals, new Comparator<Individual>() {
				public int compare(Individual i1, Individual i2) {
					int value = equation.getValue();
					return i1.getFitness(value) - i2.getFitness(value);
				}
			});
			if (individuals.get(0).getFitness(equation.getValue()) == 0) {
				System.out.println("- Solution found via genetic algorithm after " + count + " generation(s): ");
				for (Map.Entry<Term, Integer> entry : individuals.get(0).getTermValues().entrySet()) {
					Term term = entry.getKey();
					Integer termValue = entry.getValue();
					System.out.println("\t" + term.getVariable() + ": " + termValue);
				}
				return true;
			}
			return false;
		}

		/**
		 * Returns a collection of the fittest individuals. The size of the collection is determined by the value of
		 * {@link survivingIndividualsPerGeneration}.
		 * 
		 * @return
		 */
		private List<Individual> selectFittest() {
			List<Individual> fittest = new ArrayList<Individual>();
			for (int i = 0; i < (survivalRate * generationSize); i++) {
				fittest.add(individuals.get(i));
			}
			return fittest;
		}

		/**
		 * Combines the values of two given solutions to create a hybrid {@link Individual} whose term values are a
		 * mutated average of the two.
		 * 
		 * @param parent1
		 * @param parent2
		 * @return
		 */
		private Individual performCrossover(Individual parent1, Individual parent2) {
			Map<Term, Integer> termValues = new LinkedHashMap<Term, Integer>();

			for (Term term : equation.getTerms()) {
				Integer parent1TermValue = parent1.getTermValues().get(term);
				Integer parent2TermValue = parent2.getTermValues().get(term);
				int average = Math.round((parent1TermValue + parent2TermValue) / 2);

				// Randomize (Mutation)
				int minBounds = (int) Math.max(average - (average * mutationRate), 0);
				int maxBounds = average + minBounds;

				int newValue = ThreadLocalRandom.current().nextInt(minBounds, maxBounds + 1);
				termValues.put(term, newValue);
			}
			Individual individual = new Individual();
			individual.setTermValues(termValues);
			return individual;
		}

		/**
		 * Creates a subsequent generation of individuals.
		 */
		public void generateNextGeneration() {
			List<Individual> fittest = selectFittest();
			individuals.clear();
			for (Individual parent1 : fittest) {
				for (Individual parent2 : fittest) {
					Individual individual = performCrossover(parent1, parent2);
					individuals.add(individual);
				}
			}
			// Include a random individual in every generation
			individuals.add(createRandomIndividual());
			count++;
		}

		public void addIndividual(Individual individual) {
			individuals.add(individual);
		}

	}

}
