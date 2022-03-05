package com.example.cocktail;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/";
    @GET("search.php")
    Call<JsonObject>cocktailListFirstLetter(@Query("f") String search_word);
    @GET("search.php")
    Call<JsonObject> searchCocktailList(@Query("s") String search_word);
}
