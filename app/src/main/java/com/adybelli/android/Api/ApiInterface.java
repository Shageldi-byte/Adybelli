package com.adybelli.android.Api;

import com.adybelli.android.Common.Constant;
import com.adybelli.android.Object.AddAddressResponse;
import com.adybelli.android.Object.AddFavResponse;
import com.adybelli.android.Object.AddToCardResponse;
import com.adybelli.android.Object.AllBrands;
import com.adybelli.android.Object.AllBrandsResponse;
import com.adybelli.android.Object.CancelOrderProduct;
import com.adybelli.android.Object.Catalog;
import com.adybelli.android.Object.ConstantResponse;
import com.adybelli.android.Object.CreateOrderResponse;
import com.adybelli.android.Object.DeleteAddressBody;
import com.adybelli.android.Object.DeleteAddressResponse;
import com.adybelli.android.Object.GetAddressResponse;
import com.adybelli.android.Object.GetCategory;
import com.adybelli.android.Object.GetFavs;
import com.adybelli.android.Object.GetLocations;
import com.adybelli.android.Object.GetProducts;
import com.adybelli.android.Object.GetProducts2;
import com.adybelli.android.Object.GetUserById;
import com.adybelli.android.Object.GetUserCard;
import com.adybelli.android.Object.GetUserCardInfo;
import com.adybelli.android.Object.GetUserOrdersResponse;
import com.adybelli.android.Object.GetUserSingleOrder;
import com.adybelli.android.Object.GetUserSingleOrderResponse;
import com.adybelli.android.Object.Home;
import com.adybelli.android.Object.Product;
import com.adybelli.android.Object.ProductOption;
import com.adybelli.android.Object.ProductOptionBody;
import com.adybelli.android.Object.RemoveFavResponse;
import com.adybelli.android.Object.SignInPost;
import com.adybelli.android.Object.SignInResponse;
import com.adybelli.android.Object.SingleProduct;
import com.adybelli.android.Object.UpdateMailResponse;
import com.adybelli.android.Object.UpdateUserCard;
import com.adybelli.android.Object.UpdateUserInfoPost;
import com.adybelli.android.Object.UpdateUserResponse;
import com.adybelli.android.Object.UpdateUserTokenResponse;
import com.adybelli.android.Object.UserVerificationBody;
import com.adybelli.android.Post.AddAddressPost;
import com.adybelli.android.Post.AddFavPost;
import com.adybelli.android.Post.AddToCardPost;
import com.adybelli.android.Post.CancelOrder;
import com.adybelli.android.Post.CreateAccountPost;
import com.adybelli.android.Post.CreateAccountResponse;
import com.adybelli.android.Post.CreateOrderPost;
import com.adybelli.android.Post.DeleteFavPost;
import com.adybelli.android.Post.ProductOptionPost;
import com.adybelli.android.Post.ProductsPost;
import com.adybelli.android.Post.SearchPost;
import com.adybelli.android.Post.UpdateMailPost;
import com.adybelli.android.Post.UpdateUserCardPost;
import com.adybelli.android.Post.UserUpdateTokenPost;
import com.adybelli.android.Post.VerifyUserResponseBody;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("/api/get-home?")
    Call<Home> getHome(@Header("Authorization") String token,@Query("gender_id") Integer id,@Header("Device") String device,@Header("Lang") String lang);

    @GET("/api/get-catalog")
    Call<Catalog> getCatalog(@Header("Lang") String lang);

    @GET("/api/get-categories-for-catalog?")
    Call<GetCategory> getCategories(@Query("cat_id") Integer id,@Header("Lang") String lang);

    @POST("/api/get-products")
    Call<GetProducts> getProducts(@Header("Authorization") String token,@Body ProductsPost body,@Header("Lang") String lang);

    @POST("/api/get-products-options")
    Call<ProductOption> getProductsOptions(@Body ProductOptionPost body,@Header("Lang") String lang);

    @GET("/api/get-product-by-id?")
    Call<SingleProduct> getSingleProduct(@Header("Authorization") String token,@Query("prod_id") int id,@Header("Lang") String lang);

    @POST("/api/user-verification")
    Call<UserVerificationBody> userVerification(@Body SignInPost phone,@Header("Lang") String lang);

    @POST("/api/signin-user")
    Call<SignInResponse> signIn(@Body SignInPost body,@Header("Lang") String lang);

    @POST("/api/verify-user")
    Call<VerifyUserResponseBody> verifyUser(@Body SignInPost body,@Header("Lang") String lang);

    @POST("/api/signup-user")
    Call<CreateAccountResponse> signUp(@Body CreateAccountPost post,@Header("Lang") String lang);

    @GET("/api/get-locations")
    Call<GetLocations> getLocations(@Header("Lang") String lang);

    @POST("/api/add-user-address")
    Call<AddAddressResponse> addAddress(@Header("Authorization") String token, @Body AddAddressPost post,@Header("Lang") String lang);

    @POST("/api/modify-user-address")
    Call<AddAddressResponse> updateAddress(@Header("Authorization") String token, @Body AddAddressPost post,@Header("Lang") String lang);

    @GET("/api/get-user-address")
    Call<GetAddressResponse> getAddress(@Header("Authorization") String token,@Header("Lang") String lang);


    @GET("/api/get-user--cart-info?")
    Call<GetUserCardInfo> getUserCartInfo(@Header("Authorization") String token,@Query("cart_id_list") String cart_id_list,@Header("Lang") String lang);

    @HTTP(method = "DELETE", path = "/api/delete-user-address", hasBody = true)
    Call<DeleteAddressResponse> deleteAddress(@Header("Authorization") String token, @Body DeleteAddressBody ua_id,@Header("Lang") String lang);

    @GET("/api/get-user-by-id")
    Call<GetUserById> getUserById(@Header("Authorization") String token,@Header("Lang") String lang);

    @POST("/api/update-user-info")
    Call<UpdateUserResponse> updateUser(@Header("Authorization") String token, @Body UpdateUserInfoPost post,@Header("Lang") String lang);

    @GET("/api/get-constant-page?")
    Call<ConstantResponse> getConstantPage(@Query("page") String page,@Query("lang") String lang);

    @POST("/api/add-to-favorites")
    Call<AddFavResponse> addFavourites(@Header("Authorization") String token, @Body AddFavPost post,@Header("Lang") String lang);

    @GET("/api/get-user-favorites?")
    Call<GetFavs> getFavourites(@Header("Authorization") String token, @Query("last_id") Integer last_id, @Query("per_page") int per_page,@Header("Lang") String lang);

    @HTTP(method = "DELETE", path = "/api/remove-from-favorites", hasBody = true)
    Call<RemoveFavResponse> deleteFav(@Header("Authorization") String token, @Body DeleteFavPost post,@Header("Lang") String lang);


    @POST("/api/add-product-to-cart")
    Call<AddToCardResponse> addToCard(@Header("Authorization") String token, @Body AddToCardPost post,@Header("Lang") String lang);

    @GET("/api/get-user--cart")
    Call<GetUserCard> getUserCard(@Header("Authorization") String token,@Header("Lang") String lang);

    @POST("/api/update-user--cart")
    Call<UpdateUserCard> updateUserCard(@Header("Authorization") String token, @Body UpdateUserCardPost post,@Header("Lang") String lang);

    @POST("/api/create-order")
    Call<CreateOrderResponse> createOrder(@Header("Authorization") String token, @Body CreateOrderPost body,@Header("Lang") String lang);

    @GET("/api/get-user-orders?")
    Call<GetUserOrdersResponse> getUserOrders(@Header("Authorization") String token,@Query("page") Integer page,@Query("per_page") Integer per_page,@Header("Lang") String lang);

    @GET("/api/get-user-order?")
    Call<GetUserSingleOrderResponse> getSingleOrder(@Header("Authorization") String token, @Query("order_id") Integer order_id,@Header("Lang") String lang);

    @GET("/api/get-brands?")
    Call<AllBrandsResponse> getAllBrands(@Query("page") Integer page, @Query("per_page") int per_page,@Header("Lang") String lang);

    @GET("/api/quick-get-products?")
    Call<GetProducts2> getRecently(@Header("Authorization") String token, @Query("products") String products,@Header("Lang") String lang);

    @POST("/api/get-search-products")
    Call<GetProducts> getSearch(@Header("Authorization") String token, @Body SearchPost post,@Header("Lang") String lang);

    @POST("/api/link-to-mail")
    Call<UpdateMailResponse> updateMail(@Header("Authorization") String token, @Body UpdateMailPost post,@Header("Lang") String lang);

    @POST("/api/update-user-device-----token")
    Call<UpdateUserTokenResponse> updateUserToken(@Header("Authorization") String token, @Body UserUpdateTokenPost post,@Header("Lang") String lang);

    @HTTP(method = "DELETE", path = "/api/cancel__user-orders", hasBody = true)
    Call<UpdateMailResponse> cancelOrder(@Header("Authorization") String token, @Body CancelOrder post,@Header("Lang") String lang);

    @HTTP(method = "DELETE", path = "/api/cancel__user-ordered-product", hasBody = true)
    Call<UpdateMailResponse> cancelOrderProduct(@Header("Authorization") String token, @Body CancelOrderProduct post,@Header("Lang") String lang);

    @GET("/api/get-user-by-id")
    Call<GetUserById> getVersionUser(@Header("Authorization") String token,@Header("Device") String device,@Header("Lang") String lang);

}
