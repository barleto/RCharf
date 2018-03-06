package com.RCharf;

public class Subject<T> extends Observable<T> implements IObserver<T> {

    protected Observer<T> wrapper = null;
    protected volatile Boolean hasSubscribed = false;

    protected Subject(IObservable source) {
        super(source);
    }

    public Subject() {
        super();
    }

    @Override
    public synchronized void onNext(T value) {
        if(wrapper!=null) {
            wrapper.onNext(value);
        }
    }

    @Override
    public void onError(Exception e) {
        if(wrapper!=null) {
            wrapper.onError(e);
        }
    }

    @Override
    public void onComplete() {
        if(wrapper!=null) {
            wrapper.onComplete();
        }
    }

    @Override
    public IDisposable subscribe(IObserver observer) {
        onSubscribe(observer);
        return Disposable.empty();
    }


    protected synchronized void onSubscribe(IObserver<T> observer) {
        if(hasSubscribed){
            return;
        }
        wrapper = Observer.create(observer);
        hasSubscribed = true;
    }
}

