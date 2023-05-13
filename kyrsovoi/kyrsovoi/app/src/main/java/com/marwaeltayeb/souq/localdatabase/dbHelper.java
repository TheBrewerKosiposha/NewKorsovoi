package com.marwaeltayeb.souq.localdatabase;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.marwaeltayeb.souq.model.Order;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "orders.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "orders";
    private static final String COLUMN_ORDER_ID = "orderId";
    private static final String COLUMN_ORDER_DATE = "orderDate";
    private static final String COLUMN_ORDER_STATUS = "orderStatus";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ORDER_ID + " INTEGER, " +
                    COLUMN_ORDER_DATE + " TEXT, " +
                    COLUMN_ORDER_STATUS + " TEXT)";

    public DBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public void createDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        createDatabase();
    }
    // Метод для добавления нового заказа

    public ArrayList<Order> getAllOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        try (SQLiteDatabase db = getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            int idColumnIndex = cursor.getColumnIndex(COLUMN_ORDER_ID);
            int dateColumnIndex = cursor.getColumnIndex(COLUMN_ORDER_DATE);
            int statusColumnIndex = cursor.getColumnIndex(COLUMN_ORDER_STATUS);
            while (cursor.moveToNext()) {
                if (idColumnIndex != -1) {
                    int id = cursor.getInt(idColumnIndex);
                    String date = cursor.getString(dateColumnIndex);
                    String status = cursor.getString(statusColumnIndex);
                    Order order = new Order(id, date, status);
                    orders.add(order);
                } else {
                    Log.e(TAG, "Column " + COLUMN_ORDER_ID + " not found in table " + TABLE_NAME);
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error reading orders from database", e);
        }
        return orders;
    }

    public boolean orderExists(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ORDER_ID}, COLUMN_ORDER_ID + "=?",
                new String[]{String.valueOf(orderId)}, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }


    public void insertOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_ID, order.getProductId());
        values.put(COLUMN_ORDER_DATE, order.getOrderDate());
        values.put(COLUMN_ORDER_STATUS, order.getOrderDateStatus());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_ID, order.getProductId());
        values.put(COLUMN_ORDER_DATE, order.getOrderDate());
        values.put(COLUMN_ORDER_STATUS, order.getOrderDateStatus());
        db.update(TABLE_NAME, values, COLUMN_ORDER_ID + " = ?", new String[] { String.valueOf(order.getProductId()) });
        db.close();
    }

    public void deleteOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ORDER_ID + " = ?", new String[] { String.valueOf(order.getProductId()) });
        db.close();
    }
    private Context context;
    public void deleteDatabase() {
        context.deleteDatabase(DATABASE_NAME);
    }
    public boolean databaseExists() {
        SQLiteDatabase db = null;

        try {
            db = SQLiteDatabase.openDatabase(DATABASE_NAME, null, SQLiteDatabase.OPEN_READONLY);
            return true;
        } catch (SQLiteException e) {
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public Order getLocalOrderById(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                COLUMN_ORDER_ID,
                COLUMN_ORDER_DATE,
                COLUMN_ORDER_STATUS,
        };

        String selection = COLUMN_ORDER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(productId)};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        Order order = null;

        if (cursor != null) {
            if (cursor != null && cursor.moveToFirst()) {
                int productIdSIndex = cursor.getColumnIndex(COLUMN_ORDER_ID);
                String dateIndex = String.valueOf(cursor.getColumnIndex(COLUMN_ORDER_DATE));
                String statusIndex = String.valueOf(cursor.getColumnIndex(COLUMN_ORDER_STATUS));

                if(productIdSIndex >= 0 && dateIndex.length() >= 0 && statusIndex.length() >= 0) {
                    int productIdS = cursor.getInt(productIdSIndex);
                    String date = cursor.getString(Integer.parseInt(dateIndex));
                    String status = cursor.getString(Integer.parseInt(statusIndex));

                    order = new Order(productIdS, date, status);
                }
                cursor.close();

                // удаление заказа из локальной базы данных
                List<Integer> deletedOrders = new ArrayList<>();
                deletedOrders.add(order.getProductId());
                deleteLocalOrders(deletedOrders);
            }
        }

        db.close();

        return order;
    }

    public void deleteLocalOrders(List<Integer> deletedOrders) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < deletedOrders.size(); i++) {
            int orderId = deletedOrders.get(i);
            db.delete(TABLE_NAME, COLUMN_ORDER_ID + " = ?", new String[] { String.valueOf(orderId) });
        }
        db.close();
    }

/*
    public boolean deleteOrder(int orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleteCount = db.delete(TABLE_NAME, COLUMN_ORDER_ID + " = ?", new String[] { String.valueOf(orderId) });
        db.close();
        return deleteCount > 0;
    }

    public void updateOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_ID, order.getProductId());
        values.put(COLUMN_ORDER_DATE, order.getOrderDate());
        values.put(COLUMN_ORDER_STATUS, order.getOrderDateStatus());
        db.update(TABLE_NAME, values, COLUMN_ORDER_ID + " = ?", new String[] { String.valueOf(order.getProductId()) });
        db.close();
    }

 */
}
