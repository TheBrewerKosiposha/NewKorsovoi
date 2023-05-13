package com.marwaeltayeb.souq.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
public class Order implements Serializable {

    @SerializedName("id")
    private int productId;
    @SerializedName("product_name")
    private String productName;
    @SerializedName("order_number")
    private String orderNumber;
    @SerializedName("order_date")
    private String orderDate;
    @SerializedName("price")
    private double productPrice;
    @SerializedName("status")
    private String orderDateStatus;
    @SerializedName("name")
    private String userName;
    @SerializedName("address")
    private String shippingAddress;
    @SerializedName("phone")
    private String shippingPhone;

    public Order(int productId, String date, String status) {

        this.orderNumber = String.valueOf(productId);
        this.orderDate = date;
        this.orderDateStatus = status;

    }


    public int getProductId() {
        return productId;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getProductName() {
        return productName;
    }

    public String getOrderDateStatus() {
        return orderDateStatus;
    }

    public String getUserName() {
        return userName;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    //Класс Order представляет объект типа "заказ" со следующими полями: productId, productName, orderNumber, orderDate, productPrice, orderDateStatus, userName, shippingAddress и shippingPhone.
    // Аннотация @SerializedName указывает на соответствие полей в классе Order с ключами в JSON-ответе.
    // Методы get...() возвращают значения соответствующих полей. Эти методы используются для получения данных о заказе из объекта Order, полученного после десериализации JSON-ответа.
}
