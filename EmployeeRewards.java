// Q.N. 2(a)
// You have a team of n employees, and each employee is assigned a performance rating given in the integer array ratings. You want to assign rewards to these employees based on the following rules: Every employee must receive at least one reward.
// Employees with a higher rating must receive more rewards than their adjacent colleagues.
// Goal:
// Determine the minimum number of rewards you need to distribute to the employees.
// Input:
// ratings: The array of employee performance ratings.
// Output:
// The minimum number of rewards needed to distribute.
// Example 1:
// Input: ratings = [1, 0, 2]
// Output: 5
// Explanation: You can allocate to the first, second and third employee with 2, 1, 2 rewards respectively. Example 2:
// Input: ratings = [1, 2, 2]
// Output: 4
// Explanation: You can allocate to the first, second and third employee with 1, 2, 1 rewards respectively. The third employee gets 1 rewards because it satisfies the above two conditions.

import java.util.Arrays;

public class EmployeeRewards {

    public static int findMinRewards(int[] ratings) {
        int n = ratings.length;
        if (n == 0) return 0;  // If no employees, no rewards needed.

        // Create an array for rewards and give 1 reward to each employee.
        int[] rewards = new int[n];
        Arrays.fill(rewards, 1);

        // Left to right pass.
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;  // If current rating is higher than left, give more rewards.
            }
        }

        // Right to left pass.
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);  // Ensure the correct rewards.
            }
        }

        // Calculate the total rewards.
        int totalRewards = 0;
        for (int reward : rewards) {
            totalRewards += reward;
        }

        return totalRewards;  // Return the final count of rewards.
    }

    public static void main(String[] args) {
        // Example 1
        int[] ratings1 = {1, 0, 2};
        System.out.println("Minimum rewards for ratings [1, 0, 2]: " + findMinRewards(ratings1));

        // Example 2
        int[] ratings2 = {1, 2, 2};
        System.out.println("Minimum rewards for ratings [1, 2, 2]: " + findMinRewards(ratings2));
    }
}

/* 
Output
Minimum rewards for ratings [1, 0, 2]: 5
Minimum rewards for ratings [1, 2, 2]: 4
*/

/*
Algorithm Explanation:

1. Initialize an array `rewards[]` with length `n` where each employee is initially assigned 1 reward.
   - This ensures that every employee gets at least one reward, satisfying the base condition.

2. Left to Right Pass:
   - Traverse the ratings array from left to right (index 1 to n-1).
   - For each employee, if their rating is higher than the previous employee's rating, increase their reward by 1.
   - This ensures employees with higher ratings than their left neighbors get more rewards.

3. Right to Left Pass:
   - Traverse the ratings array from right to left (index n-2 to 0).
   - For each employee, if their rating is higher than the next employee's rating, ensure that their reward is updated to the maximum of their current reward and the next employee's reward + 1.
   - This ensures employees with higher ratings than their right neighbors also get more rewards, even if they were already adjusted in the left to right pass.

4. Calculate the total rewards:
   - Sum the rewards for each employee to get the total number of rewards required.

5. Return the total rewards, which is the minimum number of rewards needed to satisfy the conditions.
*/ 