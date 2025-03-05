// Q.N. 6 (b)
// Scenario: A Multithreaded Web Crawler
// Problem:
// You need to crawl a large number of web pages to gather data or index content. Crawling each page sequentially can be time-consuming and inefficient.
// Goal:
// Create a web crawler application that can crawl multiple web pages concurrently using multithreading to improve performance.
// Tasks:
// Design the application:
// Create a data structure to store the URLs to be crawled.
// Implement a mechanism to fetch web pages asynchronously.
// Design a data storage mechanism to save the crawled data.
// Create a thread pool:
// Use the ExecutorService class to create a thread pool for managing multiple threads.
// Submit tasks:
// For each URL to be crawled, create a task (e.g., a Runnable or Callable object) that fetches the web page and processes the content.
// Submit these tasks to the thread pool for execution.
// Handle responses:
// Process the fetched web pages, extracting relevant data or indexing the content.
// Handle errors or exceptions that may occur during the crawling process.
// Manage the crawling queue:
// Implement a mechanism to manage the queue of URLs to be crawled, such as a priority queue or a breadth-first search algorithm.
// By completing these tasks, you will create a multithreaded web crawler that can efficiently crawl large numbers of web page

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebCrawler {

    // Thread-safe queue for URLs to crawl
    private ConcurrentLinkedQueue<String> urlQueue = new ConcurrentLinkedQueue<>();
    // Thread-safe map to store crawled data
    private ConcurrentHashMap<String, String> crawledData = new ConcurrentHashMap<>();
    // Executor service for managing threads
    private ExecutorService executorService;

    public WebCrawler(int numThreads) {
        executorService = Executors.newFixedThreadPool(numThreads);
    }

    // Task to crawl a URL
    class CrawlTask implements Callable<String> {
        private String url;

        public CrawlTask(String url) {
            this.url = url;
        }

        @Override
        public String call() {
            return fetchWebPage(url);
        }

        private String fetchWebPage(String url) {
            StringBuilder content = new StringBuilder();
            try {
                @SuppressWarnings("deprecation")
                URL urlObj = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                // Store crawled data
                crawledData.put(url, content.toString());
            } catch (IOException e) {
                System.err.println("Error fetching URL: " + url + " - " + e.getMessage());
            }
            return content.toString();
        }
    }

    // Add URLs to the queue
    public void addUrls(List<String> newUrls) {
        for (String newUrl : newUrls) {
            urlQueue.add(newUrl);
        }
    }

    // Start the crawling process
    public void startCrawling() {
        while (!urlQueue.isEmpty()) {
            String url = urlQueue.poll();
            if (url != null) {
                executorService.submit(new CrawlTask(url));
            }
        }
        // Shutdown executor after tasks are completed
        executorService.shutdown();
    }

    // Main method to run the crawler
    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler(10); // 10 threads
        // Add initial URLs to crawl
        crawler.addUrls(List.of("https://wikipedia.com", "https://facebook.com"));
        // Start crawling
        crawler.startCrawling();
        // Wait for termination
        try {
            while (!crawler.executorService.isTerminated()) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Output crawled data
        crawler.crawledData.forEach((url, content) -> {
            System.out.println("Crawled URL: " + url);
            System.out.println("Content Length: " + content.length());
        });
    }
}

/*
Crawled URL: https://facebook.com
Content Length: 77043
Crawled URL: https://wikipedia.com
Content Length: 86577
*/


/*
Algorithm Explanation:

1. Initialize Data Structures:
   - A thread-safe queue (`ConcurrentLinkedQueue`) stores URLs to be crawled.
   - A thread-safe map (`ConcurrentHashMap`) stores crawled data.

2. Executor Service Setup:
   - `ExecutorService` creates a fixed thread pool to manage multiple threads.

3. Crawling Task (`CrawlTask`):
   - Each task fetches a webpage using `HttpURLConnection`, reads its content, and stores it in the `crawledData` map.

4. Adding URLs:
   - The `addUrls()` method enqueues URLs into `urlQueue`.

5. Start Crawling:
   - The `startCrawling()` method:
     - Polls a URL from `urlQueue`.
     - Submits it as a `CrawlTask` to `executorService`.

6. Thread Execution & Shutdown:
   - The program waits until all tasks are completed and then shuts down the executor service.

7. Output Crawled Data:
   - After crawling, it prints the URLs and their content length.
*/
