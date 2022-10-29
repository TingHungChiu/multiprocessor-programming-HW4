package edu.vt.ece.hw4.backoff;



import static edu.vt.ece.hw4.locks.BackoffLock.count;

public class ExponentialBackoff implements Backoff {
    static double a;

    @Override
    public void backoff() throws InterruptedException {
        //System.out.println("I am in backoff()");

        a = count;
        //System.out.println("a:"+count);
        int delay = (int) Math.pow(2,a);
        System.out.println("delay:"+delay);
        Thread.sleep(delay);
    }

}
