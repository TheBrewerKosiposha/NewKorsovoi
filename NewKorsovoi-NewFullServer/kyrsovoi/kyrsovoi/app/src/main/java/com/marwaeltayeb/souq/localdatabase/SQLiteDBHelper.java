package com.marwaeltayeb.souq.localdatabase;

import static com.marwaeltayeb.souq.localdatabase.dbHelper.COLUMN_CATEGORY;
import static com.marwaeltayeb.souq.localdatabase.dbHelper.COLUMN_IMAGE;
import static com.marwaeltayeb.souq.localdatabase.dbHelper.COLUMN_PRICE;
import static com.marwaeltayeb.souq.localdatabase.dbHelper.COLUMN_PRODUCT_NAME;
import static com.marwaeltayeb.souq.localdatabase.dbHelper.COLUMN_QUANTITY;
import static com.marwaeltayeb.souq.localdatabase.dbHelper.COLUMN_SUPPLIER;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.marwaeltayeb.souq.model.Product;

import java.util.List;

public class SQLiteDBHelper {
    private SQLiteDatabase db;
    private dbHelper Helper;

    public SQLiteDBHelper(Context context) {
        Helper = new dbHelper(context);
    }
    public void open() throws SQLException {
        db = Helper.getWritableDatabase();
    }

    public void close() {
        Helper.close();
    }

    public void insertProducts(List<Product> products) {
        ContentValues cv;
        for (Product product : products) {
            cv = new ContentValues();
            cv.put(COLUMN_PRODUCT_NAME, product.getProductName());
            cv.put(COLUMN_PRICE, product.getProductPrice());
            cv.put(COLUMN_QUANTITY, product.getProductQuantity());
            cv.put(COLUMN_SUPPLIER, product.getProductSupplier());
            cv.put(COLUMN_CATEGORY, product.getProductCategory());
            cv.put(COLUMN_IMAGE, product.getProductImage());
// add more fields as necessary

// Insert record into SQLite database
            db.insert(Helper.TABLE_NAME, null, cv);
        }
    }

}


