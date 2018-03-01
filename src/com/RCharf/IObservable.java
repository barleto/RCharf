package com.RCharf;

public interface IObservable<T> {
    IDisposable subscribe(IObserver<T> observer);
}
