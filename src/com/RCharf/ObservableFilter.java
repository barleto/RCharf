package com.RCharf;

public class ObservableFilter<T> extends Observable<T>{

    FilterFunction<T> function = null;
    private IObserver<T> proxy;

    protected  ObservableFilter(IObservable<T> source,FilterFunction<T> funciton) {
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
