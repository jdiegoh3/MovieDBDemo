package com.example.moviesdbdemo.utils;

import io.reactivex.observers.DisposableObserver;

public class ApiDisposable<T, E> extends DisposableObserver<T> {

    @Override
    public void onNext(T t) {
        // Blank
    }

    @Override
    public void onError(Throwable e) {
        // Blank
    }

    public void onFailure(int statusCode, E error) {
        // Blank
    }

    @Override
    public void onComplete() {
        // Blank
    }

}
