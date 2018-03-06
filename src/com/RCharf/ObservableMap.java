package com.RCharf;

import java.util.function.Function;

public class ObservableMap<T, R> extends Observable<T>{

    Function<T, R> function = null;
    private IObserver<T> proxy;

    protected  ObservableMap(IObservable<T> source,Function<T,R> funciton) {
        super(source);
        this.function = funciton;
    }

    public IDisposable subscribe(final IObserver observer) {
        this.proxy = new IObserver<T>() {
            @Override
            public void onNext(T value) {
                observer.onNext(function.apply(value));
            }

            @Override
            public void onError(Exception e) {
                observer.onError(e);
            }

            @Override
            public void onComplete() {
                observer.onComplete();
            }
        };
        return source.subscribe(proxy);
    }
}
