package com.RCharf;

public class Observer<T> implements IObserver<T>{

    Boolean isDisposed = false;
    IObserver<T> handler;

    private Observer(IObserver<T> handler){
        this.handler = handler;
    }

    @Override
    public void onNext(T value) {
        if(!isDisposed){
            handler.onNext(value);
        }
    }

    @Override
    public void onError(Exception e) {
        if(!isDisposed){
            handler.onError(e);
        }
        isDisposed = true;
    }

    @Override
    public void onComplete() {
        if(!isDisposed){
            handler.onComplete();
        }
        isDisposed = true;
    }

    public void dispose(){
        isDisposed = true;
    }

    public static <T> Observer<T> create(IObserver<T> observer){
        return new Observer<T>(observer);
    }

}
