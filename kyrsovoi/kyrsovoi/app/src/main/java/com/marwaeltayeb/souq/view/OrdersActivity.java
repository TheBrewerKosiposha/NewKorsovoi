package com.marwaeltayeb.souq.view;

import static com.marwaeltayeb.souq.utils.Constant.ORDER;
import static com.marwaeltayeb.souq.utils.InternetUtils.isNetworkConnected;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.marwaeltayeb.souq.R;
import com.marwaeltayeb.souq.adapter.OrderAdapter;
import com.marwaeltayeb.souq.databinding.ActivityOrdersBinding;
import com.marwaeltayeb.souq.localdatabase.DBHelper;
import com.marwaeltayeb.souq.model.Order;
import com.marwaeltayeb.souq.receiver.NetworkChangeReceiver;
import com.marwaeltayeb.souq.storage.LoginUtils;
import com.marwaeltayeb.souq.utils.OnNetworkListener;
import com.marwaeltayeb.souq.viewmodel.OrderViewModel;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity implements OrderAdapter.OrderAdapterOnClickHandler {

    private ActivityOrdersBinding binding;
    private OrderViewModel orderViewModel; //относится к классу OrderViewModel, который используется для получения списка заказов в режиме реального времени (LiveData).
    private OrderAdapter orderAdapter; //это адаптер RecyclerView, используемый для связывания данных с списком заказов.

    //мы передаем макет R.layout.activity_orders в метод setContentView() и инициализируем binding, используя DataBindingUtil.setContentView(this, R.layout.activity_orders).

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_orders);
//Создается экземпляр orderViewModel и становится доступным для использования.
        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);

        setUpRecycleView();

        getOrders();

    }

    //Затем вызывается метод setUpRecyclerView(), который настраивает RecyclerView для отображения списка заказов.
    // Мы создаем LinearLayoutManager с ориентацией VERTICAL и применяем его к RecyclerView.
    // Затем мы добавляем ItemDecoration к RecyclerView, чтобы добавить разделитель между элементами списка.

    private void setUpRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.orderList.setLayoutManager(layoutManager);
        binding.orderList.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.orderList.addItemDecoration(dividerItemDecoration);
    }
    OnNetworkListener onNetworkListener;

    //Затем вызывается метод getOrders(), который получает заказы с помощью orderViewModel.getOrders().
    // Метод ожидает идентификатор пользователя и вызывает observe(), чтобы подписаться на изменения списка заказов.
    // Когда заказы получены, мы создаем новый экземпляр orderAdapter и устанавливаем его в RecyclerView.
    private void getOrders() {
        DBHelper dbHelper = new DBHelper(this);
        if (!isNetworkConnected(this    )) {


            ArrayList<Order> localOrders = dbHelper.getAllOrders();
            orderAdapter = new OrderAdapter(localOrders, this);
            binding.orderList.setAdapter(orderAdapter);


        }
        else {

            orderViewModel.getOrders(LoginUtils.getInstance(this).getUserInfo().getId())
                    .observe(this, orderApiResponse -> {
                        orderAdapter = new OrderAdapter(orderApiResponse.getOrderList(), this);
                        binding.orderList.setAdapter(orderAdapter);


                        // Delete any existing data in the local database
                        dbHelper.deleteDatabase();

                        List<Order> orders = orderApiResponse.getOrderList();

                        // Save the orders from the remote database to the local database
                        for (Order order : orders) {
                            Order localOrder = dbHelper.getLocalOrderById(order.getProductId());

                            if (localOrder == null) {
                                dbHelper.insertOrder(order);
                            } else if (!localOrder.equals(order)) {
                                dbHelper.updateOrder(order);
                            }
                        }


                    });
        }

    }



    //из интерфейса OrderAdapterOnClickHandler. Когда мы нажимаем на элемент списка заказов, мы получаем соответствующий заказ и передаем его в активность StatusActivity.
    @Override
    public void onClick(Order order) {
        Intent intent = new Intent(OrdersActivity.this, StatusActivity.class);
        // Передать объект класса order
        intent.putExtra(ORDER, (order));
        startActivity(intent);
    }



}
