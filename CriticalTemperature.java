// Q.N. 1(a)
// You have a material with n temperature levels. You know that there exists a critical temperature f where 0 <= f <= n such that the material will react or change its properties at temperatures higher than f but remain unchanged at or below f.
// Rules:
// You can measure the material's properties at any temperature level once.
// If the material reacts or changes its properties, you can no longer use it for further measurements.
// If the material remains unchanged, you can reuse it for further measurements.
// Goal:
// Determine the minimum number of measurements required to find the critical temperature.
// Input:
// k: The number of identical samples of the material.
// n: The number of temperature levels
// Output:
// The minimum number of measurements required to find the critical temperature. 
// Example 1:
// Input: k = 1, n = 2 
// Output: 2 
// Explanation:
// Check the material at temperature 1. If its property changes, we know that f = 0.
// Otherwise, raise temperature to 2 and check if property changes. If its property changes, we know that f = 1. If its property changes at temperature, then we know f = 2.
// Hence, we need at minimum 2 moves to determine with certainty what the value of f is.
// Example 2:
// Input: k = 2, n = 6
// Output: 3
// Example 3:
// Input: k = 3, n = 14
// Output: 4

public class CriticalTemperature {

    public static int findMinMeasurements(int k, int n) {
        // Create a DP table of size (k+1) x (n+1)
        int[][] dp = new int[k + 1][n + 1];

        // Base case: If there is only 1 sample, we need to check each temperature level
        for (int j = 1; j <= n; j++) {
            dp[1][j] = j;
        }

        // Base case: If there are 0 temperature levels, no measurements are needed
        for (int i = 1; i <= k; i++) {
            dp[i][0] = 0;
        }
        
        // Fill the DP table
        for (int i = 2; i <= k; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = Integer.MAX_VALUE;
                for (int x = 1; x <= j; x++) {
                    // If the material changes at temperature x, we have i-1 samples and x-1 temperature levels left
                    int case1 = dp[i - 1][x - 1];

                    // If the material does not change at temperature x, we have i samples and j-x temperature levels left
                    int case2 = dp[i][j - x];

                    // Take the worst-case scenario and add 1 for the current measurement
                    int result = 1 + Math.max(case1, case2);
                    
                    // Choose the minimum of all possible x
                    dp[i][j] = Math.min(dp[i][j], result);
                }
            }
        }
        // Return the result for k samples and n temperature levels
        return dp[k][n];
    }

    public static void main(String[] args) {
        // Example 1
        int k1 = 1, n1 = 2;
        System.out.println("\nExample 1");
        System.out.println("Input: k = 1, n = 2");
        System.out.println("Output: " + findMinMeasurements(k1, n1)); // Output: 2

        // Example 2
        int k2 = 2, n2 = 6;
        System.out.println("\nExample 2");
        System.out.println("Input: k = 2, n = 6");
        System.out.println("Output: " + findMinMeasurements(k2, n2)); // Output: 3

        // Example 3
        int k3 = 3, n3 = 14;
        System.out.println("\nExample 3");
        System.out.println("Input: k = 3, n = 14");
        System.out.println("Output: " + findMinMeasurements(k3, n3)); // Output: 4
    }
}

/*
Output

Example 1
Input k = 1, n = 2
Output: 2

Example 2
Input k = 2, n = 6
Output: 3

Example 3
Input k = 3, n = 14
Output: 4
*/

/*
Algorithm Explanation:

Initialize a DP table of size (k+1) x (n+1).
Base Cases:
If there is only 1 sample (k = 1), we need to check each temperature level one by one. So, dp[1][j] = j.
If there are 0 temperature levels (n = 0), no measurements are needed. So, dp[i][0] = 0.

Fill the DP table:
For each sample i from 2 to k, and for each temperature level j from 1 to n, calculate dp[i][j] as follows:

For each possible temperature level x from 1 to j, calculate the worst-case scenario:
If the material changes at temperature x, we have i-1 samples left and x-1 temperature levels to check.
If the material does not change at temperature x, we have i samples and j-x temperature levels to check.
Take the maximum of these two scenarios and add 1 (for the current measurement).
Choose the minimum of all these values for dp[i][j].
Return dp[k][n] as the result.
*/