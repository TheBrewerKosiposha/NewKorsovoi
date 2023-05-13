package com.marwaeltayeb.souq.net;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.marwaeltayeb.souq.model.Product;
import com.marwaeltayeb.souq.model.ProductApiResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDataSource extends PageKeyedDataSource<Integer, Product> {

    private Context context;

    private static final String TAG = "ProductDataSource";
    private static final int FIRST_PAGE = 1;
    public static final int PAGE_SIZE = 20;
    private final String category;
    private final int userId;


    ProductDataSource(String category, int userId) {
        this.category = category;
        this.userId = userId;

    }
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Product> callback) {
        RetrofitClient.getInstance()
                .getApi().getProductsByCategory(category, userId,FIRST_PAGE)
                .enqueue(new Callback<ProductApiResponse>() {
                    @Override
                    public void onResponse(Call<ProductApiResponse> call, Response<ProductApiResponse> response) {
                        Log.v(TAG, "Succeeded " + response.body().getProducts().size());

                        if (response.body().getProducts() == null) {
                            return;
                        }

                        if (response.body() != null) {
                            // Insert products into SQLite database
                            //       SQLiteDBHelper Helper = new SQLiteDBHelper(context);
                        //    Helper.open();
                            // Insert products into SQLite database
                      //      Helper.insertProducts(response.body().getProducts());
                            // Close SQLite database connection
                      //      Helper.close();

                            callback.onResult(response.body().getProducts(), null, FIRST_PAGE + 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductApiResponse> call, Throwable t) {
                        Log.v(TAG, "Failed to get Products");
                    }
                });
    }

    private void insertProducts(List<Product> products) {
        // Insert products into SQLite database here

        //dbHelper helper = new dbHelper();
      //  helper.insertProducts(products);
    }



    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Product> callback) {
        RetrofitClient.getInstance()
                .getApi().getProductsByCategory(category,userId,params.key)
                .enqueue(new Callback<ProductApiResponse>() {
                    @Override
                    public void onResponse(Call<ProductApiResponse> call, Response<ProductApiResponse> response) {
                        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                        if (response.body() != null) {
                            // Check if the products already exist in SQLite database
                            List<Product> products = response.body().getProducts();
                            List<Product> newProducts = new ArrayList<>();
                            for (Product product : products) {
                                if (!productExistsInSQLite(product)) {
                                    // Insert new products into SQLite database
                                    newProducts.add(product);
                                }
                            }
                            insertProducts(newProducts);

                            // Passing the loaded data and the previous page key
                            callback.onResult(response.body().getProducts(), adjacentKey);
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductApiResponse> call, Throwable t) {
                        Log.v(TAG, "Failed to previous Products");
                    }
                });
    }

    private boolean productExistsInSQLite(Product product) {
        // Check if the product already exists in the SQLite database here
        return false;
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Product> callback) {
        RetrofitClient.getInstance()
                .getApi().getProductsByCategory(category,userId,params.key)
                .enqueue(new Callback<ProductApiResponse>() {
                    @Override
                    public void onResponse(Call<ProductApiResponse> call, Response<ProductApiResponse> response) {
                        if (response.body() != null) {
                            // Check if the products already exist in SQLite database
                            List<Product> products = response.body().getProducts();
                            List<Product> newProducts = new ArrayList<>();
                            for (Product product : products) {
                                if (!productExistsInSQLite(product)) {
                                    // Insert new products into SQLite database
                                    newProducts.add(product);
                                } else {
                                    // Compare the timestamp of the data in MySQL and SQLite
                                    // If the timestamp of the data in MySQL is newer, update the data in SQLite
                                    updateProductIfNewer(product);
                                }
                            }
                            insertProducts(newProducts);

                            // If the response has next page, increment the next page number
                            Integer key = response.body().getProducts().size() == PAGE_SIZE ? params.key + 1 : null;

                            // Passing the loaded data and next page value
                            callback.onResult(response.body().getProducts(), key);
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductApiResponse> call, Throwable t) {
                        Log.v(TAG, "Failed to get next Products");
                    }
                });
    }

    private void updateProductIfNewer(Product product) {
        // Compare the timestamp of the data in MySQL and SQLite here
        // If the timestamp of the data in MySQL is newer, update the data in SQLite
    }
}
