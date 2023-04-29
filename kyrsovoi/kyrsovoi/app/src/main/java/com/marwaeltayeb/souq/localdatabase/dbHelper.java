package com.marwaeltayeb.souq.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.marwaeltayeb.souq.model.Product;

import java.util.List;

public class dbHelper extends SQLiteOpenHelper {
    // Имя базы данных
    private List productList;
    private static final String DATABASE_NAME = "my_products_database.db";

    // Версия базы данных
    private static final int DATABASE_VERSION = 1;

    // Имя таблицы
static final String TABLE_NAME = "products";

    // Названия столбцов
    private static final String COLUMN_ID = "id";
    static final String COLUMN_PRODUCT_NAME = "product_name";
    static final String COLUMN_PRICE = "price";
    static final String COLUMN_QUANTITY = "quantity";
    static final String COLUMN_SUPPLIER = "supplier";
    static final String COLUMN_CATEGORY = "category";
     static final String COLUMN_IMAGE = "image";

    // SQL-запрос на создание таблицы
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_PRODUCT_NAME + " TEXT,"
            + COLUMN_PRICE + " REAL,"
            + COLUMN_QUANTITY + " INTEGER,"
            + COLUMN_SUPPLIER + " TEXT,"
            + COLUMN_CATEGORY + " TEXT,"
            + COLUMN_IMAGE + " BLOB"
            + ")";

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // code to create tables and insert products into database goes here
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Удаление таблицы и создание новой при обновлении базы данных
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertProducts(List<Product> products) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        for (Product product : products) {
            cv.put(COLUMN_PRODUCT_NAME, product.getProductName());
            cv.put(COLUMN_PRICE, product.getProductPrice());
            cv.put(COLUMN_QUANTITY, product.getProductQuantity());
            cv.put(COLUMN_SUPPLIER, product.getProductSupplier());
            cv.put(COLUMN_CATEGORY, product.getProductCategory());
            cv.put(COLUMN_IMAGE, product.getProductImage());

            db.insert(TABLE_NAME, null, cv);
        }

        db.close();
    }
}


