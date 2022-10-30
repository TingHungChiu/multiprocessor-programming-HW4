package edu.vt.ece.hw4.bench;

import edu.vt.ece.hw4.locks.Lock;

public class EmptyCSTestThread extends Thread implements ThreadId {
    private static int ID_GEN = 0;

    private Lock lock;
    private int id;
    private double elapsed;
    private int iter;

    public EmptyCSTestThread(Lock lock, int iter) {
        id = ID_GEN++;
        this.lock = lock;
        this.iter = iter;
    }

    public static void reset() {
        ID_GEN = 0;
    }

    @Override
    public void run() {
        double start = System.currentTimeMillis();
        for (int i = 0; i < iter; i++) {
            lock.lock();
            lock.unlock();
        }
        elapsed = System.currentTimeMillis() - start;
    }

    public int getThreadId() {
        return id;
    }

    public double getElapsedTime() {
        return elapsed;
    }
}

