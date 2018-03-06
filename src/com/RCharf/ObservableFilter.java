package com.RCharf;

import java.util.function.Function;

public class ObservableFilter<T> extends Observable<T>{

    Function<T,Boolean> function = null;
    private IObserver<T> proxy;

    protected  ObservableFilter(IObservable<T> source,Function<T,Boolean> funciton) {
        super(source);
        this.function = funciton;
    }

    public IDisposable subscribe(final IObserver observer) {
        this.proxy = new IObserver<T>() {
            @Override
            public void onNext(T value) {
                if(function.apply(value)){
                    observer.onNext(value);
                }
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
