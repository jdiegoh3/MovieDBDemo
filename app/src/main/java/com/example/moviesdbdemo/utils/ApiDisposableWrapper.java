package com.example.moviesdbdemo.utils;

public class ApiDisposableWrapper<T, E> extends ApiDisposable<T, E> {

    private ApiDisposable disposable;

    public ApiDisposableWrapper(ApiDisposable disposable) {
        this.disposable = disposable;
    }

    @Override
    public void onNext(T response) {
        super.onNext(response);
        disposable.onNext(response);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        disposable.onError(e);
    }

    @Override
    public void onFailure(int statusCode, E error) {
        super.onFailure(statusCode, error);
        disposable.onFailure(statusCode, error);
    }

    @Override
    public void onComplete() {
        super.onComplete();
        disposable.onComplete();
    }

}
