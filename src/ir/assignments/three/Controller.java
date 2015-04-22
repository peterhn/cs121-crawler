package ir.assignments.three;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {

	public static final String STORAGE_FOLDER = "./crawlerContents/";
	public static final String WEBPAGES_FOLDER = STORAGE_FOLDER + "/webpages/";
	public static final String USER_AGENT = "UCI Inf141-CS121 crawler 68419390 53042590";
	public static final String SEED = "http://www.ics.uci.edu/";
	public static final int NUMBER_CRAWLERS = 64;

	public static void main(String[] args) throws Exception {
		final CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(STORAGE_FOLDER);
		config.setUserAgentString(USER_AGENT);
		config.setPolitenessDelay(300);
		config.setResumableCrawling(true);

		/*
		 * Explicitly make sure that the storage directories exist
		 */
		final File webpagesFolder = new File(WEBPAGES_FOLDER);
		if (!webpagesFolder.exists()) {
			webpagesFolder.mkdirs();
		}
		/*
		 * Instantiate the controller for this crawl.
		 */
		final PageFetcher pageFetcher = new PageFetcher(config);
		final RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		final RobotstxtServer robotstxtServer = new RobotstxtServer(
				robotstxtConfig, pageFetcher);
		final CrawlController controller = new CrawlController(config,
				pageFetcher, robotstxtServer);

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		// controller.addSeed("http://www.ics.uci.edu/~lopes/");
		// controller.addSeed("http://www.ics.uci.edu/~welling/");
		controller.addSeed(SEED);
		
		
		/*
		 * Add all of the already existing subdomains from previous crawls
		 */
		initFoundSubdomains();

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(IcsCrawler.class, NUMBER_CRAWLERS);
	}

	private static void initFoundSubdomains() {
		final File file = new File(Controller.STORAGE_FOLDER + "subdomains.txt");
		if (file.exists()) {
			try {
				final Scanner scanner = new Scanner(file);
				while (scanner.hasNextLine()) {
					IcsCrawler.getSubdomainSet().add(scanner.nextLine());
					}
				scanner.close();
			} catch (FileNotFoundException e) {
				System.err.println(e.getMessage());
			}
		}
	}
}
