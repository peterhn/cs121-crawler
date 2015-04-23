package ir.assignments.three;

import java.io.File;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {

	public static final String BASE_DIR = "./crawlerContents/";
	public static final String STORAGE_FOLDER = BASE_DIR + "root/";
 	public static final String LOG_DIR = BASE_DIR + "log/";
 	public static final String DATA_FILE = BASE_DIR + "crawlerLink.data";
	public static final String USER_AGENT = "UCI Inf141-CS121 crawler 68419390 53042590 25372224";
	public static final String SEED = "http://www.ics.uci.edu/";
	public static final int NUMBER_CRAWLERS = 12;

	public static void main(final String[] args) throws Exception {
		final CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(STORAGE_FOLDER);
		config.setUserAgentString(USER_AGENT);
		config.setPolitenessDelay(300);
		config.setResumableCrawling(true);
		config.setMaxDepthOfCrawling(8);
		config.setMaxPagesToFetch(-1);

		/*
		 * Explicitly make sure that the storage directories exist
		 */
		final File webpagesFolder = new File(STORAGE_FOLDER);
		if (!webpagesFolder.exists()) {
			webpagesFolder.mkdirs();
		}
		/*
		 * Instantiate the controller for this crawl.
		 */
		final PageFetcher pageFetcher = new PageFetcher(config);
		final RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		robotstxtConfig.setUserAgentName(USER_AGENT);
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
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(Crawler.class, NUMBER_CRAWLERS);

		/*
		 * For now
		 */
		System.out.println("Crawler has finished");
	}
}
