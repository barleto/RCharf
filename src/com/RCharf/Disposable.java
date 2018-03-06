package com.RCharf;

public abstract class Disposable implements IDisposable{

    @Override
    abstract public void dispose();

    public static IDisposable empty(){
        return new IDisposable() {
            @Override
            public void dispose() {

            }
        };
    }
}
