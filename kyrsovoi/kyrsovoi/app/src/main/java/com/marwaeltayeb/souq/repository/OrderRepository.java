package com.marwaeltayeb.souq.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.marwaeltayeb.souq.model.OrderApiResponse;
import com.marwaeltayeb.souq.net.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {
// - объявление статического приватного поля TAG, который используется для ведения логов.
    private static final String TAG = OrderRepository.class.getSimpleName();
//метод getOrders, который принимает входной параметр userId и возвращает объект LiveData с типом OrderApiResponse.
    public LiveData<OrderApiResponse> getOrders(int userId) {
        //создание объекта MutableLiveData, который инициализируется как пустой объект и будет использоваться для установки значения в случае успешного вызова запроса getOrders у RetrofitClient.
        final MutableLiveData<OrderApiResponse> mutableLiveData = new MutableLiveData<>();
//асинхронный вызов метода getOrders из RetrofitClient, который принимает входной параметр userId.
// Он обрабатывается в колбэке объекта типа Callback<OrderApiResponse>.
        RetrofitClient.getInstance().getApi().getOrders(userId).enqueue(new Callback<OrderApiResponse>() {

//реализация метода onResponse из интерфейса Callback<OrderApiResponse>.
// Он выполняется в случае успешного получения ответа от сервера.
            @Override
            public void onResponse(Call<OrderApiResponse> call, Response<OrderApiResponse> response) {
               //Запись логов в консоль с помощью метода d из класса Log. Выводит статус код ответа сервера.
                Log.d("onResponse", "" + response.code());
//получение тела ответа сервера в переменную responseBody.
                OrderApiResponse responseBody = response.body();
//проверка наличия тела ответа сервера и установка соответствующего значения в MutableLiveData.
                if (response.body() != null) {
                    mutableLiveData.setValue(responseBody);
                }
            }
//реализация метода onFailure из интерфейса Callback<OrderApiResponse>.
// Он выполняется в случае ошибки во время выполнения запроса.
            @Override
            public void onFailure(Call<OrderApiResponse> call, Throwable t) {
                //запись логов в консоль с помощью метода d из класса Log. Выводит сообщение об ошибке.
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

//возврат MutableLiveData, который будет содержать данные, полученные при вызове метода getOrders у RetrofitClient.
        return mutableLiveData;
    }


    //В целом, данный код производит асинхронный вызов для получения данных о заказах с сервера, используя RetrofitClient.
    // В случае удачного запроса, метод onResponse получает тело ответа и сохраняет его в MutableLiveData.
    // В случае ошибки, метод onFailure выводит сообщение об ошибке в логи.
    // Этот репозиторий используется вместе с архитектурой MVVM (Model-View-ViewModel), в которой ViewModel будет обрабатывать данные из OrderRepository.

}