package com.RCharf;

import java.util.ArrayList;

public class CollectionObserver<T> extends BlockingObserverBase<T>{

    ArrayList<T> list = new ArrayList<T>();
    Exception e = null;

    public ArrayList<T> returnedList(){
        return list;
    }

    public Exception getError(){
        return this.e;
    }

    @Override
    public void onNext(T value) {
        list.add(value);
    }

    @Override
    public void onError(Exception e) {
        this.e = e;
        lock.release();
    }

    @Override
    public void onComplete() {
        lock.release();
    }
}
