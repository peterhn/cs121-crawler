package ir.assignments.three;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler extends WebCrawler {

	public static Collection<String> crawl(final String seedURL) {
		return null;
	}

	// Do not find any of these patterns, ignore them all
	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g"
					+ "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	// URLs must conform to this domain
	private final static Pattern SUFFIX = Pattern.compile("^http://.*\\.ics\\.uci\\.edu/.*");

	// TRAP URLs which will be difficult to crawl or impossible to crawl
	private final static Pattern TRAPS = Pattern.compile("^http://(ftp|archive|calendar)\\.ics\\.uci\\.edu/.*");

	// Skip URLs which have queries or other kinds of JS, they are not different RAW pages
	private final static Pattern QUERYFILTERS = Pattern.compile(".*[\\?@=].*");

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
		return !FILTERS.matcher(href).matches()
				&& SUFFIX.matcher(href).matches()
				&& !QUERYFILTERS.matcher(href).matches()
				&& !TRAPS.matcher(href).matches();
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(final Page page) {
		final WebURL url = page.getWebURL();

		if (page.getParseData() instanceof HtmlParseData) {
			final HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			final String title = htmlParseData.getTitle();
			final String text = htmlParseData.getText();
			final String html = htmlParseData.getHtml();
			final Set<WebURL> links = htmlParseData.getOutgoingUrls();

			writeLog(url, title, text);
			displayLog(url, text, html, links);
		}
	}

	private synchronized void displayLog(final WebURL url, final String text, final String html, final Set<WebURL> links){
		System.out.println("URL: " + url.getURL());
		System.out.println("Text length: " + text.length());
		System.out.println("Html length: " + html.length());
		System.out.println("Number of outgoing links: " + links.size());
		try {
			// Write out a single line which contains all of the outgoing links found on the given URL
			final FileWriter fWriter = new FileWriter(Controller.DATA_FILE, true);
			final StringBuilder builder = new StringBuilder(url.getURL());
			builder.append("\t");
			for (final WebURL link : links) {
				builder.append(link.getURL());
				builder.append(" ");
			}
			builder.append("\n");
			fWriter.write(builder.toString());
			fWriter.close();
		} catch (final IOException e) {
			System.err.println("Error when writing " + url.getURL());
		}
	}

	private void writeLog(final WebURL url, final String title, final String text) {
		try {
			// Long way of basically mirroring the webserver

			// The remaining that will be mirrored into the LOG_DIR
			final String path = url.getSubDomain();
			final String logPath = Controller.LOG_DIR + path;
			File file = new File(logPath);

			// If the retrieved URL is a directory, store it as index.txt
			if (file.isDirectory()) {
				file = new File(logPath + "index.txt");
			// If the file discovered is a directory by path but not function, then
			// we must manually create an index.txt file in it
			} else if (url.getPath().lastIndexOf('.') < url.getPath().lastIndexOf('/')) {
				file = new File(logPath + "/index.txt");
			}
			// Make sure that parent directories exist before attempting to write data
			if (!file.exists()) {
				// This will mirror the webserver by creating all parent directories
				final File parent = new File(file.getParent());
				if (!parent.exists()){
					parent.mkdirs();
				}
			}

			// Finally write the contents to the file, including title and text
			final FileWriter fWriter = new FileWriter(file);
			if (title != null) {
				fWriter.write(title);
			}
			if (text != null) {
				fWriter.write(text);
			}
			fWriter.close();

		} catch (final Exception e) {
			System.err.println("Error when writing " + url );
		}
	}

}
