package edu.vt.ece.hw4.locks;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class PriorityQueueLock implements Lock {
    public AtomicBoolean locked;
    PriorityBlockingQueue<Qnode> priorityQueue = new PriorityBlockingQueue<Qnode>();
    private ThreadLocal<Qnode> myNode;

    @Override
    public void lock() {
        Qnode qnode = this.myNode.get();
        qnode.prior = Thread.currentThread().getPriority();
        priorityQueue.add(qnode);
        while (true) {
            while (priorityQueue.peek() != qnode);
            while (!locked.compareAndSet(false, true));
            if (priorityQueue.peek() == qnode)
                break;
            locked.set(false);
        }
    }

    @Override
    public void unlock() {
        Qnode qnode = this.myNode.get();
        priorityQueue.remove(qnode);
        locked.set(false);
    }


    private class Qnode {
        int prior;
    }
}