// Q.N. 4 (a)
// Tweets table
// Write a solution to find the top 3 trending hashtags in February 2024. Every tweet may contain several hashtags.
// Return the result table ordered by count of hashtag, hashtag in descending order.
// The result format is in the following example.
// Explanation:
// #HappyDay: Appeared in tweet IDs 13, 14, and 17, with a total count of 3 mentions. #TechLife: Appeared in tweet IDs 16 and 18, with a total count of 2 mentions.
// #WorkLife: Appeared in tweet ID 15, with a total count of 1 mention.
// Note: Output table is sorted in descending order by hashtag_count and hashtag respectively.

import java.util.*;

class Tweet {
    int tweetId;
    String tweet;
    String tweetDate; // Format: "YYYY-MM-DD"

    Tweet(int tweetId, String tweet, String tweetDate) {
        this.tweetId = tweetId;
        this.tweet = tweet;
        this.tweetDate = tweetDate;
    }
}

public class TrendingHashtags {
    public static List<String[]> findTopHashtags(List<Tweet> tweets) {
        // Step 1: Map to store hashtag counts
        Map<String, Integer> hashtagCount = new HashMap<>();

        // Step 2: Process each tweet
        for (Tweet tweet : tweets) {
            // Filter for February 2024
            if (tweet.tweetDate.startsWith("2024-02")) {
                // Split tweet into words
                String[] words = tweet.tweet.split("\\s+");
                for (String word : words) {
                    if (word.startsWith("#") && word.length() > 1) { // Valid hashtag check
                        hashtagCount.put(word, hashtagCount.getOrDefault(word, 0) + 1);
                    }
                }
            }
        }

        // Step 3: Sort hashtags by count and name
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(hashtagCount.entrySet());
        sortedList.sort((a, b) -> {
            int countCompare = b.getValue().compareTo(a.getValue()); // Descending count
            if (countCompare == 0) {
                return b.getKey().compareTo(a.getKey()); // Descending hashtag
            }
            return countCompare;
        });

        // Step 4: Prepare result (top 3)
        List<String[]> result = new ArrayList<>();
        for (int i = 0; i < Math.min(3, sortedList.size()); i++) {
            Map.Entry<String, Integer> entry = sortedList.get(i);
            result.add(new String[]{entry.getKey(), entry.getValue().toString()});
        }

        return result;
    }

    // Test the solution
    public static void main(String[] args) {
        List<Tweet> tweets = Arrays.asList(
            new Tweet(13, "Celebrating #HappyDay", "2024-02-01"),
            new Tweet(14, "Love #HappyDay today", "2024-02-02"),
            new Tweet(15, "Busy with #WorkLife", "2024-02-03"),
            new Tweet(16, "Enjoying #TechLife", "2024-02-04"),
            new Tweet(17, "#HappyDay is great", "2024-02-05"),
            new Tweet(18, "#TechLife rocks", "2024-02-06")
        );

        List<String[]> topHashtags = findTopHashtags(tweets);
        System.out.println("hashtag\t  | hashtag_count");
        for (String[] entry : topHashtags) {
            System.out.println(entry[0] + " | " + entry[1]);
        }
    }
}

/*output
#HappyDay | 3
#TechLife | 2
#WorkLife | 1
*/

/* Algorithm Explanation

1. Create a HashMap to store hashtags and their occurrence counts.
2. Iterate through the list of tweets:
   - Filter tweets from February 2024.
   - Extract words from each tweet and check for valid hashtags.
   - Update the count of each hashtag in the HashMap.
3. Convert the HashMap entries into a list and sort:
   - First, by count in descending order.
   - If counts are equal, by hashtag name in descending order.
4. Select the top 3 trending hashtags.
5. Return the result as a list of string arrays containing hashtags and their counts.
*/