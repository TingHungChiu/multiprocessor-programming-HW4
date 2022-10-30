

package edu.vt.ece.hw4.locks;

import edu.vt.ece.hw4.utils.ThreadCluster;

import java.util.concurrent.atomic.AtomicInteger;

public class SimpleHLock implements Lock {

    private int clusters;
    AtomicInteger state;
    TTASLock globallock = new TTASLock();

    public SimpleHLock(int clusters) {
        this.clusters = clusters;
        state = new AtomicInteger(-1);
    }

    @Override
    public void lock() {
        int mycluster = ThreadCluster.getCluster(clusters);
        TTASLock locallock = new TTASLock();
            locallock.lock();
            globallock.lock();



    }

    @Override
    public void unlock() {
    }
}