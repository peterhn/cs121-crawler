package ir.assignments.three;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class IcsCrawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|gif|jpg|pdf" + "|png|mp3|mp3|zip|gz))$");
	private int longestPageLength = Integer.MIN_VALUE;

	/**
	 * This method receives two parameters. The first parameter is the page in
	 * which we have discovered this new url and the second parameter is the new
	 * url. You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic). In this example,
	 * we are instructing the crawler to ignore urls that have css, js, git, ...
	 * extensions and to only accept urls that start with
	 * "http://www.ics.uci.edu/". In this case, we didn't need the referringPage
	 * parameter to make the decision.
	 */
	@Override
	public boolean shouldVisit(final Page referringPage, final WebURL url) {
		final String href = url.getURL().toLowerCase();

		/*
		 * A list of bad domains that the crawler should skip.
		 */
		final String[] urlTraps = {
				"http://archive.ics.uci.edu/",
                "http://calendar.ics.uci.edu/",
                "http://ngs.ics.uci.edu/",
                "http://evoke.ics.uci.edu/",
		};

		/*
		 * Explicitly return false for any TRAP URLs
		 */
		for (final String trapUrl : urlTraps) {
			if (href.indexOf(trapUrl) > -1) {
				return false;
			}
		}
		return !FILTERS.matcher(href).matches()
				&& href.indexOf(".ics.uci.edu") > -1; // href.startsWith("http://www.ics.uci.edu/");
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(final Page page) {
		String url = page.getWebURL().getURL();
		String subdomain = page.getWebURL().getSubDomain();
		String id = UUID.randomUUID().toString();

		if (!subdomain.equals("www.ics")) {
			try {
				FileWriter fw = new FileWriter(Controller.STORAGE_FOLDER
						+ "subdomains.txt", true);
				BufferedWriter out = new BufferedWriter(fw);
				out.append(url);
				out.newLine();
				out.close();
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}

		System.out.println("Subdomain: " + subdomain);
		System.out.println("URL: " + url);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
//			Set<WebURL> links = htmlParseData.getOutgoingUrls();

			/*
			 * Store pages HTML
			 */
			try {
				FileWriter webpageFileWriter = new FileWriter(
						Controller.WEBPAGES_FOLDER + id + ".html");
				BufferedWriter out = new BufferedWriter(webpageFileWriter);
				out.append(html);
				out.newLine();
				out.close();

			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}

			/*
			 * Store pages as plain text 
			 */
			try {
				FileWriter webpageFileWriter = new FileWriter(
						Controller.WEBPAGES_FOLDER + id + ".txt");
				BufferedWriter out = new BufferedWriter(webpageFileWriter);
				out.append(text);
				out.newLine();
				out.close();

			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}

			int pageWordCount = countWords(text);

			try {
				FileWriter fw = new FileWriter(Controller.STORAGE_FOLDER
						+ "pages.txt", true);
				BufferedWriter out = new BufferedWriter(fw);
				out.append(id + " " + pageWordCount + " " + url);
				out.newLine();
				out.close();

				FileWriter wordsWriter = new FileWriter(
						Controller.STORAGE_FOLDER + "words.txt", true);
				BufferedWriter wordsOut = new BufferedWriter(wordsWriter);
				wordsOut.append(text);
				wordsOut.close();

			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}

			/*
			 * Comment these out to speed up the crawler just slightly
			 */
//			System.out.println("Text length: " + text.length());
//			System.out.println("Html length: " + html.length());
//			System.out.println("Number of outgoing links: " + links.size());
		}
	}

	private int countWords(final String text) {
		return text.split("\\s+").length;
	}

}
