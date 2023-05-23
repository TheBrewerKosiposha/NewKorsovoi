package com.marwaeltayeb.souq.net;

import static com.marwaeltayeb.souq.utils.Constant.LOCALHOST;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = LOCALHOST;
    private static RetrofitClient mInstance;
    private final Retrofit retrofit;


    // Конструктор RetrofitClient, который инициализирует экземпляр Retrofit с помощью строителя Retrofit.Builder.
    // Здесь мы указываем основной URL-адрес сервера, используемый для запросов, и определяем, что должно использоваться для сериализации
    // и десериализации JSON, с помощью конвертера GsonConverterFactory.

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    //Метод getInstance, который используется для получения единственного экземпляра RetrofitClient.
    // Если экземпляр не был создан, то данный метод создает новый экземпляр.

    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }
//Метод getApi, который возвращает экземпляр интерфейса Api.
//Он используется для выполнения HTTP-запросов на сервер. Вызов retrofit.create() возращает экземпляр Api.class, который содержит методы для выполнения запросов.
    public Api getApi() {
        return retrofit.create(Api.class);
    }
//Таким образом, этот класс предоставляет инстанс Retrofit, который может быть использован для запросов на сервер, с использованием API, реализованного в классе Api.
}
