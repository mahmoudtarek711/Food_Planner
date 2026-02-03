//package com.example.foodplaner.datasource.remotedatasource;
//
//import com.example.productsmvp.model.productResponse;
//import com.example.productsmvp.network.Network;
//import com.example.productsmvp.network.ProductsServices;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class ProductsRemoteDataSource {
//    private ProductsServices productsServices;
//    public ProductsRemoteDataSource(){
//        this.productsServices = Network.getInstance().getAllproductsServices();
//    }
//    public void getAllProducts(ProductNetworkResponse callback){
//        productsServices.getProducts().enqueue(new Callback<productResponse>() {
//            @Override
//            public void onResponse(Call<productResponse> call, Response<productResponse> response) {
//                if (response.code() == 200)
//                callback.onSuccess(response.body().getProducts());
//                else
//                    callback.onFailure("Error: "+response.code());
//
//            }
//
//            @Override
//            public void onFailure(Call<productResponse> call, Throwable t) {
//
//            }
//        });
//    }
//}
