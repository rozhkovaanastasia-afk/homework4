import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final int MAX_NUMBER = 10_000_000;
    private static final int THREADS = 8;

    private static long totalSteps = 0;

    public static void main(String[] args) {

        System.out.println("Collatz calculation started");
        System.out.println("Numbers: from 1 to " + MAX_NUMBER);
        System.out.println("Threads: " + THREADS);

        long startTime = System.nanoTime();

        ExecutorService executor = Executors.newFixedThreadPool(THREADS);

        int chunkSize = MAX_NUMBER / THREADS;

        for (int i = 0; i < THREADS; i++) {

            int start = i * chunkSize + 1;
            int end = (i == THREADS - 1) ? MAX_NUMBER : (i + 1) * chunkSize;

            executor.execute(() -> {
                long localSteps = 0;

                for (int n = start; n <= end; n++) {
                    localSteps += collatzSteps(n);
                }

                synchronized (Main.class) {
                    totalSteps += localSteps;
                }
            });
        }

        executor.shutdown();

        try {
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();

        double executionTime = (endTime - startTime) / 1_000_000_000.0;

        double averageSteps = (double) totalSteps / MAX_NUMBER;

        System.out.println("Calculation finished");
        System.out.println("Total steps: " + totalSteps);
        System.out.println("Average steps: " + averageSteps);
        System.out.println("Execution time, sec: " + executionTime);

        saveResults(averageSteps, executionTime);
    }

    private static int collatzSteps(long n) {

        int steps = 0;

        while (n != 1) {

            if (n % 2 == 0) {
                n = n / 2;
            } else {
                n = 3 * n + 1;
            }

            steps++;
        }

        return steps;
    }

    private static void saveResults(double averageSteps, double executionTime) {

        try (FileWriter writer =
                     new FileWriter("results/collatz_parallel_results.csv", true)) {

            writer.write(
                    "10000000," +
                    THREADS + "," +
                    totalSteps + "," +
                    averageSteps + "," +
                    executionTime + "\n"
            );

            System.out.println(
                    "Results saved to results/collatz_parallel_results.csv"
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
