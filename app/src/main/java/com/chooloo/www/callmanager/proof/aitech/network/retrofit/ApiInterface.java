package com.chooloo.www.callmanager.proof.aitech.network.retrofit;


import com.chooloo.www.callmanager.proof.aitech.model.ForgotModel;
import com.chooloo.www.callmanager.proof.aitech.model.LoginModel;
import com.chooloo.www.callmanager.proof.aitech.model.SignUpModel;
import com.chooloo.www.callmanager.proof.aitech.model.SyncModel;
import com.chooloo.www.callmanager.proof.aitech.util.Constants;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

public interface ApiInterface {

    @Headers({
            "Accept: application/json"
    })
    @POST("login")
    Call<LoginModel> getUserLogin(@Body HashMap<String, String> jsonLogin);


    @POST("register")
    Call<SignUpModel> getSignUpLogin(@Body HashMap<String, String> jsonLogin);


    @POST("forgetpassword")
    Call<ForgotModel> getForgotUser(@Body HashMap<String, String> jsonLogin);

    @POST("savecalllogs")
    Call<SyncModel> sendSyncLog(@Body HashMap<String, String> jsonLogin,
                                @Header("Authorization") String token);


    @FormUrlEncoded
    @POST()
    Call<String> getCallProofLogin(@Field(Constants.EMAIL) String email,
                                   @Field(Constants.PASSWORD) String password,
                                   @Field(Constants.TIMEZONE) String timezone,
                                   @Field(Constants.DEVICE_ID_APP) String deviceid,
                                   @Field(Constants.PHONE_NUMBER) String phone_number,
                                   @Url String url);

/*    @POST()
    Call<SyncModel> getCallProofLogin(@Body CallProofRequestModel model,
                                      @Url String url);*/

    @Multipart
    @POST("savecalllogs")
    Call<SyncModel> sendSyncLogFile(@PartMap Map<String, RequestBody> id,
                                    @Part MultipartBody.Part file,
                                    @Header("Authorization") String token);
}






















/*    //check email
    @GET("api/email-exists")
    Call<CheckEmailModel> getCheckEmail(@Query("email") String emailID);

    //login
    @Headers({
            "Content-Type: application/vnd.api+json",
            "Client-Type: android-app",
            "Client-Version:31"
    })

    @POST("api/login")
    Call<LoginUserModel> getUserLogin(@Body JsonObject json);


    //fblogin
    @Headers({
            "Content-Type: application/vnd.api+json",
            "Client-Type: android-app",
            "Client-Version:31"
    })
    @POST("api/login")
    Call<LoginUserModel> getFBUserLogin(@Body JsonObject json);

    //select product
    @GET("api/products?filter[available]=true&include=getting_started_videos")
    Call<ProductDetails> getProducts();

    //upload image
    @Multipart
    @POST("api/upload")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image);

    //edit profile && toggle change
    @PUT("api/users/{user_id}")
    Call<SignUpModel> postUserProfile(@Path("user_id") long userID,
                                      @Body JsonObject json,
                                      @Header("Authorization") String apiToken,
                                      @Header("Accept-Language") String language);


    //filter refine list
//    @GET("api/tags?set=home_page&include=recipes")
    @GET("api/tags?sort=browsable&filter[primary]=true&filter[include-star-ratings]=true")
    Call<Filter> getFilterList(@Header("Authorization") String apiToken, @Header("Accept-Language") String language);

    //appliance-->recipelist
    @GET("api/recipes?")
    Call<Recipe> getRecipeList(@Query(value = "filter[product]", encoded = true) String tagValue,
                               @Query(value = "page[number]", encoded = true) String pageNo,
                               @Query(value = "page[size]", encoded = true) String pageSize,
                               @Header("Authorization") String apiToken,
                               @Header("Accept-Language") String language);

    //appliance-->recipeFilterlist
    @GET("api/recipes?")
    Call<Recipe> getRecipeFilterList(@Query(value = "filter[tag-value-slug]", encoded = true) String tagValue,
                                     @Query(value = "page[number]", encoded = true) String pageNo,
                                     @Query(value = "page[size]", encoded = true) String pageSize,
                                     @Header("Authorization") String apiToken,
                                     @Header("Accept-Language") String language);

    //popular category
    @GET("api/tags?filter[featured]=true&filter[random]=true&filter[limit]=6")
    Call<PopularCategoryModel> getPopularCategoryList(@Header("Authorization") String apiToken,
                                                      @Header("Accept-Language") String language);


    //reviews
    @GET("api/reviews?")
    Call<Reviews> getProductReviews(@Query(value = "filter[recipe-id]", encoded = true) String recipeId);


    //add to recipe box
    @POST("api/recipe_box_entries")
    Call<RecipeBoxResponseModel> addToBox(@Body JsonObject json,
                                          @Header("Authorization") String apiToken,
                                          @Header("Accept-Language") String language);

    //fetchRecipeBox
    @GET("api/recipes?filter[recipe-box]=true")
    Call<Recipe> fetchRecipeBox(@Header("Authorization") String apiToken, @Header("Accept-Language") String language);

    //filter refine list
    @GET("api/tags?set=home_page&include=recipes")
    Call<DiscoverModel> getRecipesList(@Header("Authorization") String apiToken, @Header("Accept-Language") String language);

    //getting meta data for recipe home page
    @GET("api/recipes/{recipe_id}")
    Call<RecipeMetaDataModel> getRecipeMeta(@Path("recipe_id") String recipeID, @Header("Authorization") String apiToken,
                                            @Header("Accept-Language") String language);

    //add review
    @POST("api/reviews")
    Call<ResponseBody> addReview(@Header("Authorization") String apiToken, @Body JsonObject json,
                                 @Header("Accept-Language") String language);

    //personal notes
    @POST("api/recipe_user_comment")
    Call<AddNotesModel> addPersonalNotes(@Header("Authorization") String apiToken, @Body JsonObject json,
                                         @Header("Accept-Language") String language);

    //remove from box
    @DELETE("api/recipe_box_entries/{recipe_id}")
    Call<ResponseBody> removeBoxItem(@Path("recipe_id") String recipeID, @Header("Authorization") String apiToken,
                                     @Header("Accept-Language") String language);


    //getsearch with recipe filter
    @GET("api/recipes?")
    Call<Recipe> getRecipeFilterWithSearchList(@Query(value = "filter[tag-value-slug]", encoded = true) String tagValue,
                                               @Query(value = "filter[search]", encoded = true) String search,
                                               @Query(value = "page[number]", encoded = true) String pageNo,
                                               @Query(value = "page[size]", encoded = true) String pageSize,
                                               @Header("Authorization") String apiToken,
                                               @Header("Accept-Language") String language);

    @GET("api/recipes?")
    Call<Recipe> getRecipesFromSearch(@Query(value = "filter[search]", encoded = true) String search,
                                      @Query(value = "page[number]", encoded = true) String pageNo,
                                      @Query(value = "page[size]", encoded = true) String pageSize,
                                      @Header("Authorization") String apiToken,
                                      @Header("Accept-Language") String language);

    //FOR INDIVIDUAL RECIPE ITEM
    @GET("api/recipes/{recipe_id}?include=products,variants.recipe_ingredients,recipe_product_serving_settings")
    Call<IndividualRecipeModel> getRecipeFromID(@Path("recipe_id") String recipeID, @Header("Authorization") String apiToken,
                                                @Header("Accept-Language") String language);


    @Headers({
            "Content-Type: application/json"

    })
    @POST
    Call<AmazonModel> amazonFresh(@Url String url, @Body JsonObject json);*/


