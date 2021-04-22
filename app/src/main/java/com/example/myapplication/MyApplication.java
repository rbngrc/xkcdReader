package com.example.myapplication;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import com.example.myapplication.xkcd.Comic;
import com.example.myapplication.xkcd.XkcdService;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {
    MainThreadExecutor mainThreadExecutor = new MainThreadExecutor();
    ExecutorService diskIOExecutor = Executors.newSingleThreadExecutor();

    XkcdService xkcdService;

    @Override
    public void onCreate() {
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://xkcd.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        xkcdService = retrofit.create(XkcdService.class);

        /*EJEMPLO DE USO PARA OBTENER UN COMIC:

        Call<Comic> request = xkcdService.getComic(1);

        // Esto realiza una llamada síncrona que habría que realizar usando un Executor para no bloquear el hilo principal:

        Response<Comic> response = request.execute();
        Comic comic = response.body();

        // Esto en cambio realiza una Llamada asíncrona
        // retrofit se encarga de realizarla en otro hilo y devolver el resultado en el hilo principal:

        request.enqueue(new Callback<Comic>() {
            @Override
            public void onResponse(Call<Comic> call, Response<Comic> response) {
                Comic comic = response.body();

            }

            @Override
            public void onFailure(Call<Comic> call, Throwable t) {
            }
        });

        */
    }

    private static class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());

        @Override
        public void execute(Runnable runnable) {
            mainThreadHandler.post(runnable);
        }
    }
}