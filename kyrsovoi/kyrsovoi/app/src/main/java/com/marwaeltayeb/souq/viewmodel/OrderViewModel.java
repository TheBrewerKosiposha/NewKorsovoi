package com.marwaeltayeb.souq.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.marwaeltayeb.souq.model.OrderApiResponse;
import com.marwaeltayeb.souq.repository.OrderRepository;

//объявление класса OrderViewModel, который наследуется от класса ViewModel.
public class OrderViewModel extends ViewModel {

    //объявление приватного поля orderRepository, которое ссылается на экземпляр класса OrderRepository.
    private final OrderRepository orderRepository;
//конструктор класса OrderViewModel, которой создает экземпляр класса OrderRepository и сохраняет его в поле orderRepository.
    public OrderViewModel() {
        orderRepository = new OrderRepository();
    }
//метод getOrders, который принимает входной параметр userId и возвращает объект LiveData с типом OrderApiResponse.
// Он вызывает метод getOrders у orderRepository и передает ему userId.
// Методы из orderRepository возвращают объект LiveData с OrderApiResponse.
// Вызов этого метода позволит получать данные о заказах из репозитория и установить их наблюдение.
    public LiveData<OrderApiResponse> getOrders(int userId) {
        return orderRepository.getOrders(userId);
    }

    //В целом, данный код является частью архитектуры MVVM (Model-View-ViewModel) и предоставляет доступ к данным о заказах из класса ViewModel.
    // Он использует OrderRepository, чтобы получать данные о заказах и возвращает LiveData для реактивного программирования -- данные будут обновляться автоматически в случае изменения источника данных.
}

