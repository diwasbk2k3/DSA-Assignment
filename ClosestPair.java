// Q.N. 2(b)
// You have two points in a 2D plane, represented by the arrays x_coords and y_coords. The goal is to find 
// the lexicographically pair i.e. (i, j) of points (one from each array) that are closest to each other. 
// Goal: 
// Determine the lexicographically pair of points with the smallest distance and smallest distance calculated 
// using  
// | x_coords [i] - x_coords [j]| + | y_coords [i] - y_coords [j]| 
// Note that 
// |x| denotes the absolute value of x. 
// A pair of indices (i1, j1) is lexicographically smaller than (i2, j2) if i1 < i2 or i1 == i2 and j1 < j2. 
// Input: 
// x_coords: The array of x-coordinates of the points. 
// y_coords: The array of y-coordinates of the points. 
// Output: 
// The indices of the closest pair of points. 
// Input: x_coords = [1, 2, 3, 2, 4], y_coords = [2, 3, 1, 2, 3] 
// Output: [0, 3] 
// Explanation: Consider index 0 and index 3. The value of | x_coords [i]- x_coords [j]| + | y_coords [i]- 
// y_coords [j]| is 1, which is the smallest value we can achieve.

public class ClosestPair {

    // Function to find the closest pair of points
    public static int[] findClosestPair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length; // Number of points
        int minDistance = Integer.MAX_VALUE; // Initialize with maximum possible value
        int[] result = new int[2]; // To store the indices of the closest pair

        // Iterate through all pairs of points (i, j)
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Calculate the Manhattan distance
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);

                // If the distance is smaller, update the result
                if (distance < minDistance) {
                    minDistance = distance;
                    result[0] = i;
                    result[1] = j;
                } 
                // If the distance is the same, choose lexicographically smaller pair
                else if (distance == minDistance) {
                    if (i < result[0] || (i == result[0] && j < result[1])) {
                        result[0] = i;
                        result[1] = j;
                    }
                }
            }
        }

        // Return the indices of the closest pair
        return result;
    }

    public static void main(String[] args) {
        // Example input
        int[] x_coords = {1, 2, 3, 2, 4};
        int[] y_coords = {2, 3, 1, 2, 3};

        // Find the closest pair
        int[] closestPair = findClosestPair(x_coords, y_coords);

        // Display the output
        System.out.println("Output: [" + closestPair[0] + ", " + closestPair[1] + "]");
    }
}

// Output: [0, 3]

/* Algorithm Explanation:

1. Initialize variables:
   - `minDistance` is set to the maximum possible integer value, ensuring that the first calculated distance will be smaller.
   - `result[]` array stores the indices of the closest pair of points.

2. Iterate through all pairs of points (i, j):
   - For each pair of points `i` and `j`, calculate the Manhattan distance:
     - distance = |x_coords[i] - x_coords[j]| + |y_coords[i] - y_coords[j]|

3. Track the minimum distance:
   - If the calculated distance is smaller than the current `minDistance`, update `minDistance` and store the pair's indices in `result[]`.

4. Lexicographical order:
   - If two pairs have the same distance, choose the one with the lexicographically smaller pair of indices (i, j). This ensures that the pair with the smaller values comes first in case of a tie.

5. Return the indices:
   - After evaluating all pairs, return the `result[]` array, which contains the indices of the closest pair.

Example Walkthrough:

For the input points:
x_coords = [1, 2, 3, 2, 4]
y_coords = [2, 3, 1, 2, 3]

The closest pair is found between points (0, 3) with a distance of 1, and this pair will be returned.
*/