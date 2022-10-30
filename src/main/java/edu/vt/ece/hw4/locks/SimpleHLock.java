

package edu.vt.ece.hw4.locks;

import edu.vt.ece.hw4.utils.ThreadCluster;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class SimpleHLock implements Lock {

    private int clusters;
    int count;
    TTASLock globallock = new TTASLock();
    TTASLock[] locallock;
    AtomicIntegerArray state;
    ThreadCluster threadID ;
    public SimpleHLock(int clusters, int threadCount) {
        this.clusters = clusters;
        int[] a = new int[threadCount];
        state = new AtomicIntegerArray(a);
        locallock = new TTASLock[clusters];
        initial(locallock);
        threadID = new ThreadCluster();
        threadID.reset();
    }
    public void initial(Lock[] locks){
        for (int i =0;i< locks.length;i++)
        {
            locks[i] = new TTASLock();
        }
    }

    @Override
    public void lock() {
        System.out.println("in lock");
        state.set(threadID.get(),1);
        locallock[threadID.getCluster(clusters)].lock();
        if (count !=2){
            globallock.lock();
        }
    }

    @Override
    public void unlock() {
        locallock[threadID.getCluster(clusters)].lock();
        state.set(threadID.get(),0);
        count =0;
        for (int i =0; i<state.length();i++){
            if(state.get(i)==1) {
                count = 1;
            }
        }
        if (count==0){
            globallock.unlock();
        }else{
            count=2;
        }
    }
}