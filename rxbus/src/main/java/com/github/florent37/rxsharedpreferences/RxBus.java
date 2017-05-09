package com.github.florent37.rxsharedpreferences;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by etp7220 on 30/03/2017.
 */

public class RxBus {

    private static RxBus rxBus = new RxBus();

    public static RxBus getDefault() {
        return rxBus;
    }

    private final Subject<Object> bus = PublishSubject.create().toSerialized();

    public Observable<Object> allEvents(){
        return bus;
    }

    public Observable<String> onEvent(final String eventName) {
        return bus
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(@NonNull Object anObject) throws Exception {
                        return eventName.equals(anObject);
                    }
                })
                .cast(String.class);
    }

    protected  <CLASS> Observable<CLASS> onEvent(final Class<CLASS> theClass) {
        return bus
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(@NonNull Object o) throws Exception {
                        return theClass.isInstance(o) || theClass.equals(o);
                    }
                })
                .cast(theClass);
    }

    public void post(final Object event) {
        bus.onNext(event);
    }

    private Observable<Object> postAsObservable(final Object event) {
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                post(event);

                final Object _useless = new Object();
                e.onNext(_useless);
                e.onComplete();
            }
        });
    }

    public <CLASS> Observable<CLASS> get(Class<CLASS> theClass) {
        return Observable.zip(
                onEvent(theClass),
                postAsObservable(new AskedEvent(theClass)),
                new BiFunction<CLASS, Object, CLASS>() {
                    @Override
                    public CLASS apply(@NonNull CLASS neededObject, @NonNull Object _useless) throws Exception {
                        return neededObject;
                    }
                });
    }

    public <CLASS> Observable<Getter<CLASS>> onGet(final Class<CLASS> theClass) {
        return onEvent(AskedEvent.class)//I wait for an event (askevent) of CLASS
                .filter(new Predicate<AskedEvent>() {
                    @Override
                    public boolean test(@NonNull AskedEvent askedEvent) throws Exception {
                        return askedEvent.askedObject.equals(theClass);
                    }
                })
                .map(new Function<AskedEvent, Getter<CLASS>>() {
                    @Override
                    public Getter<CLASS> apply(@NonNull AskedEvent o) throws Exception {
                        return new Getter<CLASS>() {
                            //then I send to the listener a Getter (interface)
                            //when the getter is notified, the value is sent to the first subscrier
                            //who called the method `get`
                            @Override
                            public void get(CLASS value) {
                                post(value); //the value is published on the bus
                            }
                        };
                    }
                });
    }

    public interface Getter<T> {
        void get(T value);
    }

    private class AskedEvent {
        public final Object askedObject;

        public AskedEvent(Object askedObject) {
            this.askedObject = askedObject;
        }
    }

}
