// Q.N. 1(b)
// You have two sorted arrays of investment returns, returns1 and returns2, and a target number k. You want to find the kth lowest combined return that can be achieved by selecting one investment from each array.
// Rules:
// The arrays are sorted in ascending order.
// You can access any element in the arrays.
// Goal:
// Determine the kth lowest combined return that can be achieved.
// Input:
// returns1: The first sorted array of investment returns.
// returns2: The second sorted array of investment returns.
// k: The target index of the lowest combined return.
// Output:
// The kth lowest combined return that can be achieved. 
// Example 1:
// Input: returns1= [2,5], returns2= [3,4], k = 2 Output: 8
// Explanation: The 2 smallest investments are are: - returns1 [0] * returns2 [0] = 2 * 3 = 6
// - returns1 [0] * returns2 [1] = 2 * 4 = 8
// The 2nd smallest investment is 8.
// Example 2:
// Input: returns1= [-4,-2,0,3], returns2= [2,4], k = 6 Output: 0
// Explanation: The 6 smallest products are:
// - returns1 [0] * returns2 [1] = (-4) * 4 = -16 - returns1 [0] * returns2 [0] = (-4) * 2 = -8 - returns1 [1] * returns2 [1] = (-2) * 4 = -8 - returns1 [1] * returns2 [0] = (-2) * 2 = -4 - returns1 [2] * returns2 [0] = 0 * 2 = 0
// - returns1 [2] * returns2 [1] = 0 * 4 = 0
// The 6th smallest investment is 0.

import java.util.*;

public class KthLowestCombined {

    public static int findKthLowestCombinedReturn(int[] returns1, int[] returns2, int k) {
        // Min-heap to store pairs of indices and their products
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> 
            Integer.compare(returns1[a[0]] * returns2[a[1]], returns1[b[0]] * returns2[b[1]]));

        // Initialize the heap with the first element from returns1 paired with all elements from returns2
        for (int i = 0; i < returns1.length; i++) {
            minHeap.offer(new int[]{i, 0});  // Pair (returns1 index, returns2 index)
        }

        // Extract the kth smallest product from the heap
        while (k-- > 1) {
            int[] pair = minHeap.poll();  // Get the current smallest product
            int i = pair[0], j = pair[1];
            // If possible, move to the next element in returns2 for the same returns1[i]
            if (j + 1 < returns2.length) {
                minHeap.offer(new int[]{i, j + 1});
            }
        }

        // The kth smallest product
        int[] kthPair = minHeap.poll();
        return returns1[kthPair[0]] * returns2[kthPair[1]];
    }

    public static void main(String[] args) {
        // Example 1
        int[] returns1 = {2, 5};
        int[] returns2 = {3, 4};
        int k1 = 2;
        System.out.println("Example 1: " + findKthLowestCombinedReturn(returns1, returns2, k1)); // Output: 8

        // Example 2
        int[] returns1_2 = {-4, -2, 0, 3};
        int[] returns2_2 = {2, 4};
        int k2 = 6;
        System.out.println("Example 2: " + findKthLowestCombinedReturn(returns1_2, returns2_2, k2)); // Output: 0
    }
}

/* 
Output
Example 1: 8
Example 2: 0
*/

/*
Algorithm Explanation:

Priority Queue Initialization:
Use a min-heap to store pairs of indices (i, j) with products returns1[i] * returns2[j].
Initialize by pairing each element of returns1 with the first element of returns2.
Heap Processing:
Pop the smallest product from the heap.
If possible, add the next element from returns2 for the same returns1 element.
Kth Smallest Product:
Repeat until the kth smallest product is reached.
Output:
Return the product corresponding to the kth smallest pair.
*/