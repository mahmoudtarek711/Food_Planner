//package com.example.foodplaner.datasource.localdatasource;
//
//import android.content.Context;
//
//import com.example.productsmvp.db.AppDatabase;
//import com.example.productsmvp.model.product;
//
//import java.util.List;
//
//public class ProductsLocalDataSource {
//    private ProductsDAO productsDAO;
//    public ProductsLocalDataSource(Context context) {
//        this.productsDAO = AppDatabase.getInstance(context).productsDAO();
//    }
//
//    public void insertProduct(product product){
//       Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                productsDAO.insertProduct(product);
//            }
//        });
//       thread.start();
//    }
//    public void deleteProduct(product product){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                productsDAO.deleteProduct(product);
//            }
//        }).start();
//    }
//    public List<product> getAllProducts() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                productsDAO.getAllProducts();
//            }
//        }).start();
//        return productsDAO.getAllProducts();
//    }
//}
