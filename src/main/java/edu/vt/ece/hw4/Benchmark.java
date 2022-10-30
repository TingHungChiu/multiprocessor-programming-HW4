package edu.vt.ece.hw4;

import edu.vt.ece.hw4.barriers.Barrier;
import edu.vt.ece.hw4.barriers.TTASBarrier;
import edu.vt.ece.hw4.barriers.TTASBarrier2;
import edu.vt.ece.hw4.bench.*;
import edu.vt.ece.hw4.locks.*;


public class Benchmark {

    private static final String ALOCK = "ALock";
    private static final String BACKOFFLOCK = "BackoffLock";
    private static final String MCSLOCK = "MCSLock";

    private static final String SPINSLEEPLOCK = "SpinSleepLock";
    private static final String PRIORITYQUEUELOCK = "PriorityQueueLock";
    private static final String TTASLOCK = "TTASLock";
    private static final String SIMPLEHLOCK = "SimpleHLock";

    public static void main(String[] args) throws Exception {
        String mode = args.length <= 0 ? "normal" : args[0];
        String lockClass = (args.length <= 1 ? ALOCK : args[1]);
        int threadCount = (args.length <= 2 ? 16 : Integer.parseInt(args[2]));
        int totalIters = (args.length <= 3 ? 64000 : Integer.parseInt(args[3]));
        int iters = totalIters / threadCount;
        System.out.println(args[0]+" "+args[1]+" "+args[2]+" "+args[3]+" "+args[4]);
        run(args, mode, lockClass, threadCount, iters);
    }

    private static void run(String[] args, String mode, String lockClass, int threadCount, int iters) throws Exception {
        for (int i = 0; i < 5; i++) {
            Lock lock = null;
            switch (lockClass.trim()) {
                case ALOCK:
                    lock = new ALock(threadCount);
                    break;
                case BACKOFFLOCK:
                    lock = new BackoffLock(args[4]);
                    break;
                case MCSLOCK:
                    lock = new MCSLock();
                    break;
                case SPINSLEEPLOCK:
                    lock = new SpinSleepLock(Integer.valueOf(args[4]));
                    break;
                case PRIORITYQUEUELOCK:
                    lock = new PriorityQueueLock();
                    break;
                case TTASLOCK:
                    lock = new TTASLock();
                    break;
                case SIMPLEHLOCK:
                    lock = new SimpleHLock(Integer.valueOf(args[4]),threadCount);
                    break;
            }
            switch (mode.trim().toLowerCase()) {
                case "normal":
                    final Counter counter = new SharedCounter(0, lock);
                    runNormal(counter, threadCount, iters);
                    break;
                case "empty":
                    runEmptyCS(lock, threadCount, iters);
                    break;
                case "long":
                    runLongCS(lock, threadCount, iters);
                    break;
                case "barrier":
                    final Counter counter1 = new SharedCounter(0, lock);
                    runBarrier(counter1,threadCount);
                    break;
                    //throw new UnsupportedOperationException("Complete this.");
                case "barrier2":
                    final int[] b = new int[threadCount];
                    runBarrier2(b,threadCount);
                    break;
                case "cluster":
                    final Counter counter2 = new SharedCounter(0, lock);
                    runClusterCS(counter2,threadCount,iters);
                    break;
                default:
                    throw new UnsupportedOperationException("Implement this");
            }
        }
    }

    private static void runNormal(Counter counter, int threadCount, int iters) throws Exception {
        final TestThread[] threads = new TestThread[threadCount];
        TestThread.reset();
        for (int t = 0; t < threadCount; t++) {
            threads[t] = new TestThread(counter, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }
        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();

            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread is "+String.format("%.5f",(float)totalTime /(float)threadCount)+ "ms" );
    }

    private static void runEmptyCS(Lock lock, int threadCount, int iters) throws Exception {

        final EmptyCSTestThread[] threads = new EmptyCSTestThread[threadCount];
        EmptyCSTestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new EmptyCSTestThread(lock, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        double totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread per iters is " + String.format("%.20f",totalTime /(double)threadCount) + "ms");
    }

    static void runLongCS(Lock lock, int threadCount, int iters) throws Exception {
        final Counter counter = new Counter(0);
        final LongCSTestThread[] threads = new LongCSTestThread[threadCount];
        LongCSTestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new LongCSTestThread(lock, counter, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }
    private static void runBarrier( Counter counter, int threadCount) throws Exception{
        final TTASBarrier[] threads = new TTASBarrier[threadCount];
        TTASBarrier.reset();
        for (int t = 0; t < threadCount; t++)
            threads[t] = new TTASBarrier(counter, threadCount);
       for (int t = 0; t < threadCount; t++)
            threads[t].start();

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread is " + totalTime/threadCount + "ms");
    }
    private static void runBarrier2( int b[], int threadCount) throws Exception{
        final TTASBarrier2[] threads = new TTASBarrier2[threadCount];
        TTASBarrier2.reset();
        for (int t = 0; t < threadCount; t++)
            threads[t] = new TTASBarrier2(b, threadCount);
        for (int t = 0; t < threadCount; t++)
            threads[t].start();

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread is " + totalTime/threadCount + "ms");
    }

    private static void runClusterCS(Counter counter, int threadCount ,int iters) throws Exception{
        final TestThread[] threads = new TestThread[threadCount];
        TestThread.reset();
        for (int t = 0; t < threadCount; t++) {
            threads[t] = new TestThread(counter, iters);
        }
        for (int t = 0; t < threadCount; t++)
            threads[t] = new TestThread(counter, iters);
        for (int t = 0; t < threadCount; t++)
            threads[t].start();

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread is " + (float)totalTime/threadCount + "ms");
    }
}