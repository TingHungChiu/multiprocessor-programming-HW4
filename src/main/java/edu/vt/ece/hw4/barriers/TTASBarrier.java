package edu.vt.ece.hw4.barriers;


import edu.vt.ece.hw4.Benchmark;
import edu.vt.ece.hw4.bench.Counter;
import edu.vt.ece.hw4.bench.SharedCounter;
import edu.vt.ece.hw4.locks.*;



public class TTASBarrier extends Thread implements Barrier {

    private static int ID_GEN = 0;
    private int id;
    private int threadCount;
    private int []b = new int[threadCount];
    private long elapsed;
    TTASLock lock = new TTASLock() ;
    public Counter counter;


    public static void reset() {
        ID_GEN = 0;
    }

    public TTASBarrier(Counter counter,int threadCount){
        id = ID_GEN++;
        this.threadCount = threadCount;
        this.counter = counter;
    }
    private void foo(){
        System.out.println("Thread "+id+":"+"foo()");
    }
    private void bar(){
        System.out.println("Thread "+id+":"+"bar()");
    }

    public void run() {
            foo();
            long start = System.currentTimeMillis();
            enter();
            long end = System.currentTimeMillis();
            bar();
            elapsed = end - start;
    }

    public void enter(){
        counter.getAndIncrement();
        while(Integer.valueOf(counter.toString())<threadCount){}
    }

    public void enter2() {
        System.out.println(id);
        foo();
        long start = System.currentTimeMillis();
        if (id == 0) {
            this.b[id] = 1;
        } else {
            while (this.b[id - 1] == 0);
            this.b[id] = 1;
        }

        if ((id == threadCount - 1) && (b[threadCount - 1] == 1))
            b[threadCount - 1] = 2;

        while (b[threadCount - 1] != 2);

        long end = System.currentTimeMillis();
        bar();
        elapsed = end - start;
    }

    public int getThreadId(){
        return id;
    }

    public long getElapsedTime() {
        return elapsed;
    }

}
