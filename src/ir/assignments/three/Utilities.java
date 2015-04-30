package ir.assignments.three;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * A collection of utility methods for text processing.
 */
public class Utilities {
	
	private static final HashSet<String> stopWords = new HashSet<>();
	private static final String STOP_WORDS = Controller.CRAWL_ROOT + "stopwords.txt";
	
	public static boolean fillStopWords() throws FileNotFoundException {
		final File f = new File(STOP_WORDS);
		if (f.exists()) {
			final Scanner sc = new Scanner(f);
			while (sc.hasNextLine()) {
				final String s = sc.nextLine();
				stopWords.add(s);
			}
			sc.close();
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isStopWord(final String s) {
		return stopWords.contains(s);
	}
	
	
	/**
	 * Reads the input text file and splits it into alphanumeric tokens. Returns
	 * an ArrayList of these tokens, ordered according to their occurrence in
	 * the original text file.
	 *
	 * Non-alphanumeric characters delineate tokens, and are discarded.
	 *
	 * Words are also normalized to lower case.
	 *
	 * Example:
	 *
	 * Given this input string "An input string, this is! (or is it?)"
	 *
	 * The output list of strings should be ["an", "input", "string", "this",
	 * "is", "or", "is", "it"]
	 *
	 * @param input
	 *            The file to read in and tokenize.
	 * @return The list of tokens (words) from the input file, ordered by
	 *         occurrence.
	 */
	public static ArrayList<String> tokenizeFile(File input) {
		/*
		 * Return null if the File input is null or if the file does not exist
		 * for some reason
		 */
		if (input == null || !input.exists()) {
			return null;
		}

		final ArrayList<String> tokenList = new ArrayList<>();
		try {
			final Scanner scanner = new Scanner(input);
			while (scanner.hasNextLine()) {
				final String line = scanner.nextLine();
				/*
				 * Replace all non alpha-numeric with spaces Split the string at
				 * all whitespaces using a whitespace regex
				 */
				final String[] splitLine = sanitizeString(line);
				/*
				 * Add each member of the splitLine[] into the tokenList by
				 * occurence Trim the strings just in case
				 */
				if (splitLine != null) {
					for (String token : splitLine) {
						if (!token.isEmpty()) {
							tokenList.add(token);
						}
					}
				}
			}
			scanner.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
		return tokenList;
	}

	/**
	 * 
	 * Strips away any non alphanumeric inputs from a string and splits it by
	 * whitespace into an array
	 * 
	 * @param string
	 *            Can contain whitespace, alphanumeric characters, and
	 *            non-alphanumeric character
	 * @return An array on sanitized strings.
	 */
	public static String[] sanitizeString(final String string) {
		return string != null ? string.toLowerCase()
				.replaceAll("[^A-Za-z0-9]", " ").split("\\s+") : null;
	}

	/**
	 * Takes a list of {@link Frequency}s and prints it to standard out. It also
	 * prints out the total number of items, and the total number of unique
	 * items.
	 *
	 * Example one:
	 *
	 * Given the input list of word frequencies ["sentence:2", "the:1",
	 * "this:1", "repeats:1", "word:1"]
	 *
	 * The following should be printed to standard out
	 *
	 * Total item count: 6 Unique item count: 5
	 *
	 * sentence 2 the 1 this 1 repeats 1 word 1
	 *
	 *
	 * Example two:
	 *
	 * Given the input list of 2-gram frequencies ["you think:2", "how you:1",
	 * "know how:1", "think you:1", "you know:1"]
	 *
	 * The following should be printed to standard out
	 *
	 * Total 2-gram count: 6 Unique 2-gram count: 5
	 *
	 * you think 2 how you 1 know how 1 think you 1 you know 1
	 *
	 * @param frequencies
	 *            A list of frequencies.
	 */
	public static void printFrequencies(List<Frequency> frequencies) {
		/*
		 * Bail if input is bad
		 */
		if (frequencies == null || frequencies.isEmpty()) {
			return;
		}

		int totalCount = 0;
		int uniqueCount = 0;
		final StringBuilder output = new StringBuilder();
		for (final Frequency freq : frequencies) {
			totalCount += freq.getFrequency();
			++uniqueCount;
			output.append(String.format("%-40s %d\n", freq.getText(), freq.getFrequency()));
		}

		/*
		 * Print out the title section
		 */
		System.out.println("Total item count: " + totalCount);
		System.out.println("Unique item count: " + uniqueCount + "\n");
		
		/*
		 * Print out the parsed output
		 */
		System.out.print(output.toString());
	}

	/**
	 * Function made by: Peter Yamanaka pyamanak@uci.edu
	 * 
	 * Loops over a list of frequencies and checks if the given word is
	 * equivalent to an already present frequency. If there is no present
	 * frequency, the word is added to the list of frequencies.
	 * 
	 * @param word
	 *            Word to check for existence
	 * @param frequencies
	 *            List of frequency objects
	 */
	public static void addOrIncrementFrequency(final String word,
			final List<Frequency> frequencies) {
		boolean contained = false;
		for (final Frequency freq : frequencies) {
			if (freq.getText().equals(word)) {
				contained = true;
				freq.incrementFrequency();
				return;
			}
		}
		if (!contained) {
			frequencies.add(new Frequency(word, 1));
		}
	}
}
