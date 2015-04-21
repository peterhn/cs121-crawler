package ir.assignments.three;

import java.util.Collection;
import java.util.HashSet;

public class Crawler {
	/**
	 * This method is for testing purposes only. It does not need to be used
	 * to answer any of the questions in the assignment. However, it must
	 * function as specified so that your crawler can be verified programatically.
	 * 
	 * This methods performs a crawl starting at the specified seed URL. Returns a
	 * collection containing all URLs visited during the crawl.
	 */
	public static Collection<String> crawl(String seedURL) {
		final HashSet<String> allURLs = new HashSet<>();
		allURLs.add(seedURL);
		System.out.println("Crawl seedURL: " + seedURL);
		return allURLs;
	}
}
