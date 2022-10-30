package edu.vt.ece.hw4.backoff;

import static edu.vt.ece.hw4.locks.BackoffLock.count;

public class FibonacciBackoff implements Backoff {
    static double a;
    int Fibonacci(int tmp){
        int num1 = 0, num2 = 1;
        int counter = 0;
        switch(tmp){
            case 1:
                return 0;
            case 2:
                return 1;
            default:
                int num3 = 0;
                for (int i=2;i<tmp;i++){
                    num3 = num1+num2;
                    num1 = num2;
                    num2 = num3;
                }
                return num3;
        }
    }
    @Override
    public void backoff() throws InterruptedException {
        a = count+1;

        int delay = Fibonacci((int)a);
        System.out.println("delay:"+delay);
        Thread.sleep(delay);
    }
}
