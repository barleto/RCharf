package com.RCharf;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ricardo.charf
 */
public class Main {

    public static void main(String[] args) {

        Observable<String> observable = Observable.create(new IObservable<String>() {
            @Override
            public IDisposable subscribe(final IObserver observer) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        observer.onNext("First next!");
                        observer.onNext("Second next!");
                        observer.onComplete();
                        observer.onNext("this won't be called");
                        timer.cancel();
                    }
                }, 1000);
                return new IDisposable() {
                    @Override
                    public void dispose() {
                        timer.cancel();
                    }
                };
            }
        });
        //-------------------------------------------

        System.out.println("before blocking first call");
        System.out.println(observable.blockingFirst());
        System.out.println("after blocking first call");
        //-------------------------------------------

        System.out.println();

        System.out.println("before toCollection call");
        try {
            System.out.println("Collection: " + observable.where(v -> v.contains("Second")).toCollection());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("after toCollection call");
        //-------------------------------------------

        System.out.println();

        System.out.println("before subscribe lambda, non-blocking");
        Observable.range(0, 10).subscribe(System.out::println);
        System.out.println("after subscribe lambda, non-blocking");
        //-------------------------------------------

        System.out.println();

        System.out.println("before Replay Subject");
        Subject rs = new ReplaySubject<String>();
        rs.onNext("Replay Subject: 1! (Before subscribe)");
        rs.subscribe(System.out::println);
        rs.onNext("Replay Subject: 2!");
        rs.onNext("Replay Subject: 3!");
        System.out.println("After Replay Subject");
        //-------------------------------------------

        System.out.println();

        System.out.println("before blockUntilComplete call with map");
        observable.map(value -> value + "-> added with map!")
                .map(value -> ((String) value).length())
                .blockUntilComplete(new IObserver<Integer>() {
                    @Override
                    public void onNext(Integer value) {
                        System.out.println("The Next length is: " + value);
                    }

                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Completed!");
                    }
                });
        System.out.println("after blockUntilComplete call with map");

        return;
    }

}

