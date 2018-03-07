package com.RCharf;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.function.Function;

public class Observable<T> implements IObservable<T>{

    protected IObservable<T> source;

    protected Observable(IObservable source) {
        this.source = source;
    }

    protected Observable() {}

    public static <V> Observable<V> create(IObservable<V> ObservableOnSubscribe){
        return new Observable<V>(ObservableOnSubscribe);
    }

    @Override
    public IDisposable subscribe(IObserver observer) {
        Observer wrapper = Observer.create(observer);
        IDisposable disposable = Disposable.empty();
        try {
            disposable = source.subscribe(wrapper);
        }catch (Exception e){
            wrapper.onError(e);
        }
        return getWrapperDisposable(wrapper, disposable);
    }

    public IDisposable subscribe(Consumer<T> onNext){
        Observer wrapper = createCustomObserver(onNext, null, null);
        return this.subscribe(wrapper);
    }

    public IDisposable subscribe(Consumer<T> onNext, Consumer onError, Consumer onCompleted){
        Observer wrapper = createCustomObserver(onNext, onError, onCompleted);
        return this.subscribe(wrapper);
    }

    protected Observer<T> createCustomObserver(Consumer<T> onNextCons, Consumer onErrorCons, Consumer onCompletedCons){
        return Observer.create(new IObserver<T>() {

            Consumer<T> onNextLambda = onNextCons;
            Consumer onErrorLambda = onErrorCons;
            Consumer onCompletedLambda = onCompletedCons;

            @Override
            public void onNext(T value) {
                if(onNextLambda!=null){
                    onNextLambda.accept(value);
                }
            }

            @Override
            public void onError(Exception e) {
                if(onErrorLambda!=null){
                    onErrorLambda.accept(null);
                }
            }

            @Override
            public void onComplete() {
                if(onCompletedLambda!=null){
                    onCompletedLambda.accept(null);
                }
            }
        });
    }

    protected IDisposable getWrapperDisposable(Observer wrapper, IDisposable disposable) {
        return new IDisposable() {
            @Override
            public void dispose() {
                wrapper.dispose();
                if(disposable!=null) {
                    disposable.dispose();
                }
            }
        };
    }

    public void blockUntilComplete(IObserver<T> observer){
        BlockObserver<T> bo = new BlockObserver<>(observer);
        IDisposable disposable = this.subscribe(bo);
        bo.waitForRelease();
        if(disposable!=null){
            disposable.dispose();
        }
    }

    public T blockingFirst(){
        final BlockingFirstObserver<T> blockObserver = new BlockingFirstObserver<>();
        IDisposable disposable = this.subscribe(blockObserver);
        blockObserver.waitForRelease();
        if(disposable!=null){
            disposable.dispose();
        }
        return blockObserver.value;
    }

    public ArrayList<T> toCollection() throws Exception {
        final CollectionObserver<T> collectionObserver = new CollectionObserver<>();
        IDisposable disposable = this.subscribe(collectionObserver);
        collectionObserver.waitForRelease();
        if(disposable!=null){
            disposable.dispose();
        }
        Exception e = collectionObserver.getError();
        if(e!=null){
            throw e;
        }else{
            return collectionObserver.returnedList();
        }
    }

    public <R> Observable map(Function<T,R> function){
        return new ObservableMap<T,R>(this, function);
    }

    public Observable where(Function<T,Boolean> function){
        return new ObservableFilter<T>(this,function);
    }

    public static Observable<Integer> range(int start, int count)
    {
        int max = start + count;
        return Observable.generate(
                start,
                value -> value < max,
                value -> value + 1,
                value -> value);
    }

    public static Observable<Integer> timer(int milliseconds)
    {
        return Observable.create(new IObservable<Integer>() {
            @Override
            public IDisposable subscribe(final IObserver observer) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        observer.onNext(0);
                        observer.onComplete();
                        timer.cancel();
                    }
                },milliseconds);
                return new IDisposable() {
                    @Override
                    public void dispose() {
                        timer.cancel();
                    }
                };
            }
        });
    }

    public static <S,R> Observable<R> generate(S state, Function<S,Boolean> condition, Function<S,S> iterate,Function<S,R> stateToResult){
        return Observable.create(new IObservable<R>() {
            private S mState = state;
            private Function<S,Boolean> mCondition = condition;
            private Function<S,S> mIterate = iterate;
            private Function<S,R> mStateToResult = stateToResult;

            @Override
            public IDisposable subscribe(IObserver<R> observer) {
                while(mCondition.apply(mState)){
                    observer.onNext(mStateToResult.apply(mState));
                    mState = mIterate.apply(mState);
                }
                observer.onComplete();
                return Disposable.empty();
            }
        });
    }



}

