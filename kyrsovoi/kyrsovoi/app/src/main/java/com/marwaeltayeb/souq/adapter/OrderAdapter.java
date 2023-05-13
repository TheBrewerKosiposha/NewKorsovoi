package com.marwaeltayeb.souq.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.marwaeltayeb.souq.R;
import com.marwaeltayeb.souq.databinding.OrderListItemBinding;
import com.marwaeltayeb.souq.model.Order;

import java.text.DecimalFormat;
import java.util.List;

//объявление класса адаптера, который наследуется от RecyclerView.Adapter, используется для отображения списков элементов.
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{
//бъявление переменной orderList для хранения списка заказов.
    private final List<Order> orderList;
    // объявление переменной currentOrder для хранения текущего заказа.
    private Order currentOrder;
//объявление clickHandler, который реализован в интерфейсе OrderAdapterOnClickHandler и используется для обработки нажатия на элементы.
    private final OrderAdapter.OrderAdapterOnClickHandler clickHandler;


    //определение интерфейса для обработки нажатия на элементы в списке заказов, который передает информацию об объекте Order

    public interface OrderAdapterOnClickHandler {
        void onClick(Order order);
    }
//конструктор адаптера, который принимает список заказов и clickHandler в качестве параметров.
    public OrderAdapter(List<Order> orderList, OrderAdapter.OrderAdapterOnClickHandler clickHandler) {
        this.orderList = orderList;
        this.clickHandler = clickHandler;
    }



    //Метод onCreateViewHolder используется для создания новых экземпляров представлений в списке заказов.
    // Он использует метод DataBindingUtil.inflate (), чтобы надувать макет представления и связывать его с экземпляром класса ViewHolder.
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        OrderListItemBinding orderListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.order_list_item,parent,false);
        return new OrderViewHolder(orderListItemBinding);
    }
//Метод onBindViewHolder используется для связи содержимого элемента, определенного в ViewHolder с элементом списка (представлением).
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        currentOrder = orderList.get(position);

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String formattedPrice = formatter.format(currentOrder.getProductPrice());
        holder.binding.productPrice.setText(formattedPrice + " PLN");

        holder.binding.orderNumber.setText(currentOrder.getOrderNumber());
        holder.binding.orderDate.setText(currentOrder.getOrderDate());
    }
//Метод getItemCount возвращает количество элементов в списке заказов
    @Override
    public int getItemCount() {
        if (orderList == null) {
            return 0;
        }
        return orderList.size();
    }

    //объявление класса ViewHolder, наследуется от RecyclerView.ViewHolder для хранения ссылки на представление элемента списка заказов.
    class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // Создание экземпляров представления
        private final OrderListItemBinding binding;

        private OrderViewHolder(OrderListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
       // установка слушателя нажатия на itemView в методе OrderViewHolder, чтобы элемент списка заказов можно было нажимать.
            itemView.setOnClickListener(this);
        }
//вызов метода onClick во время нажатия на элементы списка заказов и передача текущего заказа внутри интерфейса OrderAdapterOnClickHandler.
        @Override
        public void onClick(View v) {
            int position = getBindingAdapterPosition();
            // Получить местоположение продукта
            currentOrder = orderList.get(position);
            // Отправить товар можно одним щелчком мыши
            clickHandler.onClick(currentOrder);
        }
    }
}
