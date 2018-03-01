package com.RCharf;

import java.util.concurrent.Semaphore;

public abstract class BlockingObserverBase<T> implements IObserver<T>{
    protected Semaphore lock = new Semaphore(1);

    public void waitForRelease(){
        try {
            //Acquire semaphore two times, to be sure that the Thread is going to yield
            lock.acquire();
            lock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
