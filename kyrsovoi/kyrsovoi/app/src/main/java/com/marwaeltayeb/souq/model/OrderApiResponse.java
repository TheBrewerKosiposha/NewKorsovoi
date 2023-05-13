package com.marwaeltayeb.souq.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderApiResponse {
//аннотация, которая указывает, что поле orderList будет помечено в JSON-ответе как orders.
    @SerializedName("orders")
    //объявление приватного поля orderList, тип которого является списком объектов Order.
    private List<Order> orderList;
//объявление открытого метода getOrderList.
    public List<Order> getOrderList() {
        return orderList;
    }
    //возвращает значение поля orderList

    //Класс OrderApiResponse представляет объект ответа от сервера, который имеет список объектов Order.
    // Он используется для десериализации JSON-ответа от сервера в объект типа OrderApiResponse.
    // Аннотация @SerializedName указывает на соответствие поля orderList в классе OrderApiResponse со значением orders в JSON-ответе.
    // Метод getOrderList() возвращает список объектов Order, который был десериализован из JSON-ответа.
    // Это удобно, когда необходимо получить данные о заказах из API в приложении.

}
