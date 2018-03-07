package com.RCharf;

import java.util.ArrayList;

public class ReplaySubject<T> extends Subject<T> {

    private ArrayList<T> cache = new ArrayList<T>();


    private ReplaySubject(IObservable source) {
        super(source);
    }

    public ReplaySubject(){
        super();
    }

    @Override
    public synchronized void onNext(T value) {
        if(hasSubscribed){
            wrapper.onNext(value);
        }else{
            cache.add(value);
        }
    }

    @Override
    public void onError(Exception e) {
        if(hasSubscribed){
            wrapper.onError(e);
        }
    }

    @Override
    public void onComplete() {
        if(hasSubscribed){
            wrapper.onComplete();
        }
    }

    @Override
    public IDisposable subscribe(IObserver observer) {
        onSubscribe(observer);
        return Disposable.empty();
    }

    @Override
    protected synchronized void onSubscribe(IObserver<T> observer) {
        if(hasSubscribed){
            return;
        }
        wrapper = Observer.create(observer);
        for (T value : cache) {
            wrapper.onNext(value);
        }
        cache.clear();
        hasSubscribed = true;
    }
}
