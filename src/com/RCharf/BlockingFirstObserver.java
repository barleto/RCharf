package com.RCharf;

public class BlockingFirstObserver<T> extends BlockingObserverBase<T>{

    Boolean found =false;
    T value;
    Exception error = null;


    @Override
    public void onNext(T value) {
        if(!found){
            found = true;
            this.value = value;
        }
        lock.release();
    }

    @Override
    public void onError(Exception e) {
        if(!found){
            found = true;
            this.error = e;
        }
        lock.release();
    }

    @Override
    public void onComplete() {
        lock.release();
    }
}
