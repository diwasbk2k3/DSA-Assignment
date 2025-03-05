// Q.N. 6 (a)
// You are given a class NumberPrinter with three methods: printZero, printEven, and printOdd. These methods are designed to print the numbers 0, even numbers, and odd numbers, respectively.
// Task:
// Create a ThreadController class that coordinates three threads:
// 5. ZeroThread: Calls printZero to print 0s.
// 6. EvenThread: Calls printEven to print even numbers.
// 7. OddThread: Calls printOdd to print odd numbers.
// These threads should work together to print the sequence "0102030405..." up to a specified number n. The output should be interleaved, ensuring that the numbers are printed in the correct order.
// Example:
// If n = 5, the output should be "0102030405". Constraints:
// The threads should be synchronized to prevent race conditions and ensure correct output.
// The NumberPrinter class is already provided and cannot be modified.


import java.util.concurrent.Semaphore;

class NumberPrinter {
    // This class provides methods to print zero, even, and odd numbers.
    public void printZero() {
        System.out.print(0);  
        // Prints zero when called.
    }

    public void printEven(int number) {
        System.out.print(number);  
        // Prints an even number when called.
    }

    public void printOdd(int number) {
        System.out.print(number);  
        // Prints an odd number when called.
    }
}

public class ThreadController {
    private int n;  
    // Number of times zero should be printed and numbers to be processed.

    private Semaphore zeroSemaphore = new Semaphore(1);  
    // Controls when zero can be printed. Starts with 1 permit.

    private Semaphore evenSemaphore = new Semaphore(0);  
    // Controls when even numbers can be printed. Starts with 0 permits.

    private Semaphore oddSemaphore = new Semaphore(0);  
    // Controls when odd numbers can be printed. Starts with 0 permits.

    public ThreadController(int n) {
        this.n = n;
    }

    public void startThreads(NumberPrinter printer) { // Fixed incorrect parameter type
        // Method to start three threads: zero, even, and odd printing.

        Thread zeroThread = new Thread(() -> {
            // Thread to print zero before each number.
            try {
                for (int i = 1; i <= n; i++) {
                    zeroSemaphore.acquire();  
                    // Wait until allowed to print zero.

                    printer.printZero();  
                    // Print zero.

                    if (i % 2 == 0) {
                        evenSemaphore.release();  
                        // Release even semaphore if the number is even.
                    } else {
                        oddSemaphore.release();  
                        // Release odd semaphore if the number is odd.
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  
                // Handle interruption properly.
            }
        });

        Thread evenThread = new Thread(() -> {
            // Thread to print even numbers.
            try {
                for (int i = 2; i <= n; i += 2) {
                    evenSemaphore.acquire();  
                    // Wait until allowed to print an even number.

                    printer.printEven(i);  
                    // Print the even number.

                    zeroSemaphore.release();  
                    // Allow zero to be printed next.
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  
                // Handle interruption properly.
            }
        });

        Thread oddThread = new Thread(() -> {
            // Thread to print odd numbers.
            try {
                for (int i = 1; i <= n; i += 2) {
                    oddSemaphore.acquire();  
                    // Wait until allowed to print an odd number.

                    printer.printOdd(i);  
                    // Print the odd number.

                    zeroSemaphore.release();  
                    // Allow zero to be printed next.
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  
                // Handle interruption properly.
            }
        });

        zeroThread.start();  
        // Start the zero printing thread.

        evenThread.start();  
        // Start the even number printing thread.

        oddThread.start();  
        // Start the odd number printing thread.
    }

    public static void main(String[] args) {
        int n = 5;  
        // Set the number of iterations.

        NumberPrinter printer = new NumberPrinter(); // Fixed incorrect instantiation
        ThreadController controller = new ThreadController(n); // Fixed incorrect instantiation
        controller.startThreads(printer); // Fixed incorrect object reference
    }
}

// Output Explanation:
// 0 printed by zeroThread → 1 by oddThread
// 0 printed by zeroThread → 2 by evenThread
// 0 printed by zeroThread → 3 by oddThread
// 0 printed by zeroThread → 4 by evenThread
// 0 printed by zeroThread → 5 by oddThread
// Final Output: 0102030405

/*Algorithm Explanation:

1.Initialization: Three semaphores are used to control the threads: zeroSemaphore (initially 1 permit), evenSemaphore, and oddSemaphore (both initially 0 permits).
2.Thread Execution:
  - ZeroThread: Prints zeros. It acquires zeroSemaphore, prints a zero, and then releases either evenSemaphore or oddSemaphore based on whether the next number is even or odd.
  - EvenThread: Waits on evenSemaphore, prints an even number, and then releases zeroSemaphore to allow the next zero to be printed.
  - OddThread: Waits on oddSemaphore, prints an odd number, and then releases zeroSemaphore.
3.Synchronization: The semaphores ensure that the threads work together to print the sequence "0102030405..." by synchronizing the printing of zeros and numbers.
4.Output: The final output is an interleaved sequence where each number is preceded by a zero, maintaining the correct order of numbers.
*/