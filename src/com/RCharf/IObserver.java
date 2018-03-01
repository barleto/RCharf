package com.RCharf;

public interface IObserver<T> {
    void onNext(T value);
    void onError(Exception e);
    void onComplete();
}
