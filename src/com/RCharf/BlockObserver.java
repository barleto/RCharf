package com.RCharf;

public class BlockObserver<T> extends BlockingObserverBase<T>{

    private IObserver<T> handler = null;

    public BlockObserver(IObserver<T> handler){
        this.handler = handler;
    }

    @Override
    public void onNext(T value) {
        handler.onNext(value);
    }

    @Override
    public void onError(Exception e) {
        handler.onError(e);
        lock.release();
    }

    @Override
    public void onComplete() {
        handler.onComplete();
        lock.release();
    }
}
