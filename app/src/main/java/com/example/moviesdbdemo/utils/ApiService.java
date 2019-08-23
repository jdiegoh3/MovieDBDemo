package com.example.moviesdbdemo.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.moviesdbdemo.MoviesApplication.provideContext;


public class ApiService {

    //<editor-fold desc="Variables">

    private static final Class<?> TAG = ApiService.class;
    private static Map<String, String> headers = new HashMap<>();
    private Retrofit client;

    //</editor-fold>

    //<editor-fold desc="Client">

    private OkHttpClient createHttpClient(Cache cache){

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                        Request request = chain.request();
                        if(NetworkUtils.isInternetConnection(provideContext())){
                            request.newBuilder().addHeader("Cache-Control", "public, max-age=" + 5).build();
                        } else{
                            request.newBuilder()
                                    .addHeader("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                        }
                        return chain.proceed(request);
                    }
                })
                .cache(cache);


        return builder.build();
    }

    protected ApiService() {
        client = buildClient();
    }

    protected <T> T getService(Class<T> service) {
        return client.create(service);
    }

    private Retrofit buildClient() {
        GsonBuilder builder = new GsonBuilder();

        Cache cacheInstance = CacheUtils.getHttpCache(null);
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);

        return new Retrofit.Builder()
                .baseUrl(StaticConstants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(builder.create()))
                .build();
    }

    //</editor-fold>


    //<editor-fold desc="Requests Handling">

    protected <T, E> void handleRequest(Observable promise, final ApiDisposable<T, E> callback) {
        handleRequest(promise, callback, null);
    }

    @SuppressLint("CheckResult")
    protected <T, E> void handleRequest(Observable promise, final ApiDisposable<T, E> callback, final Class<E> errorResponse) {
        promise.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(buildRequestCallback(callback, errorResponse));
    }

    @SuppressLint("CheckResult")
    @SuppressWarnings("unchecked")
    protected <T> void resolvePromises(Observable zip, final ApiDisposable callback) {
        // TODO Up to now, it's supported 3 vital signs due to RxJx limitation (3 max). Inquire how to handle more observers.
        try {
            zip.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable e) {
                            handleError(e, callback, null);
                        }
                    })
                    .subscribe(new Consumer<T>() {
                        @Override
                        public void accept(T result) {
                            callback.onNext(result);
                            callback.onComplete();
                        }
                    });
        } catch (Exception e) {
            handleError(e, callback, null);
        }
    }

    protected <T, E> void handleError(Throwable e, ApiDisposable<T, E> callback, final Class<E> errorResponse) {
        if (e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof UnknownHostException) { // Network issues
            callback.onFailure(0, null);
        } else if (e instanceof HttpException) { // Web-services
            HttpException ex = (HttpException) e;
            callback.onFailure(ex.code(), parseError(ex.response(), errorResponse));
        } else {
            callback.onError(e);
        }
        callback.onComplete();
    }

    private byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
        try {
            byte[] buffer = new byte[40];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            return output.toByteArray();
        } catch (OutOfMemoryError error) {
            return null;
        }
    }

    private <T, E> E parseError(Response<T> response, Class<E> entityClass) {
        E entity = null;
        try {
            Gson gson = new Gson();
            if (response.errorBody() != null && entityClass != null) {
                String json = response.errorBody().string();
                entity = gson.fromJson(json, entityClass);
            }
        } catch (IOException e) {
            // Log the error
        } catch (Exception e) {
            entity = null;
        }
        return entity;
    }

    private <T, E> ApiDisposable<T, E> buildRequestCallback(final ApiDisposable<T, E> callback, final Class<E> errorResponse) {
        return new ApiDisposable<T, E>() {

            @Override
            public void onNext(T response) {
                callback.onNext(response);
                callback.onComplete();
                super.onNext(response);
            }

            @Override
            public void onError(Throwable e) {
                handleError(e, callback, errorResponse);
                super.onError(e);
            }

            @Override
            public void onComplete() {
                callback.onComplete();
                super.onComplete();
            }
        };
    }

    //</editor-fold>

}
