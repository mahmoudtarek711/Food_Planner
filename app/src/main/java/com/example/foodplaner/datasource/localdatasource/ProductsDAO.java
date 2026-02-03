//package com.example.foodplaner.datasource.localdatasource;
//
//import androidx.room.Dao;
//import androidx.room.Delete;
//import androidx.room.Insert;
//import androidx.room.OnConflictStrategy;
//import androidx.room.Query;
//
//import com.example.productsmvp.model.product;
//
//import java.util.List;
//@Dao
//public interface ProductsDAO {
//    @Insert(onConflict = OnConflictStrategy.ABORT)
//    void insertProduct(product product);
//
//    @Delete
//    void deleteProduct(product product);
//
//    @Query("SELECT * FROM products")
//    List<product> getAllProducts();
//
//}
