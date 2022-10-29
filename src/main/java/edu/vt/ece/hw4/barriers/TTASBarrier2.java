package edu.vt.ece.hw4.barriers;


import edu.vt.ece.hw4.bench.Counter;
import edu.vt.ece.hw4.locks.TTASLock;


public class TTASBarrier2 extends Thread implements Barrier {

    private static int ID_GEN = 0;
    private int id;
    private int threadCount;
    private int []b ;
    private long elapsed;


    public static void reset() {
        ID_GEN = 0;
    }

    public TTASBarrier2(int b[], int threadCount){
        id = ID_GEN++;
        this.threadCount = threadCount;
        this.b = b;
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
        elapsed = end - start;
    }


    public int getThreadId(){
        return id;
    }

    public long getElapsedTime() {
        return elapsed;
    }

}
