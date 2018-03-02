package com.RCharf;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ricardo.charf
 */
public class Main {

    public static void main(String[] args){

        Observable<String> ob = Observable.create(new IObservable<String>() {
            @Override
            public IDisposable subscribe(final IObserver observer) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        observer.onNext("First next!");
                        observer.onNext("Second next!");
                        observer.onComplete();
                        timer.cancel();
                    }
                },2000);
                return null;
            }
        });

        System.out.println("before blocking first call");
        System.out.println(ob.blockingFirst());
        System.out.println("after blocking first call");

        System.out.println();

        System.out.println("before collection call");
        try {
            System.out.println("Collection: "+ob.filter(new FilterFunction<String>() {
                @Override
                public Boolean apply(String value) {
                    if(value.contains("First")){
                        return false;
                    }else{
                        return true;
                    }
                }
            }).collection());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("after collection call");

        System.out.println();

        System.out.println("before blockUntilComplete call with map");
        ob.map(new MapFunction<String, String>() {
            @Override
            public String apply(String value) {
                return value+"-> added with map!";
            }
        }).map(new MapFunction<String, Integer>(){ //Chained map!
            @Override
            public Integer apply(String value) {
                return value.length();
            }
        }).blockUntilComplete(new IObserver<Integer>() {
            @Override
            public void onNext(Integer value) {
                System.out.println("Next: "+value);
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

        System.out.println();

        System.out.println("before call non-blocking");
        ob.subscribe(new IObserver<String>() {
            @Override
            public void onNext(String value) {
                System.out.println("Next: "+value);
            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onComplete() {

            }
        });
        System.out.println("after call non-blocking");
        return;
    }
}