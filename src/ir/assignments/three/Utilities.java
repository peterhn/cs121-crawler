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
			System.out.println("no stopwords");
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
				 * regex by word
				 */
				for (final String token : line.split("\\w")) {
					if (token.length() > 0) {
						tokenList.add(token.toLowerCase());
					}
				}
			}
			scanner.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
		return tokenList;
	}
}
