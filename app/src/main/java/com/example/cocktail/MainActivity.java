package com.example.cocktail;



import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  {
    static  ArrayList<CocktailList> arrayList=new ArrayList<>();
    static String idDrink,strDrink,strDrinkAlternate,strTags,strVideo,strCategory,strIBA,strAlcoholic,strGlass,
            strInstructions,strInstructionsES,strInstructionsDE,strInstructionsFR,strInstructionsIT,strInstructionsZH_HANS,
            strInstructionsZH_HANT,strDrinkThumb;
    static RecyclerView recyclerView;
    static Adapter adapter;
    public static AutoCompleteTextView searchData;
    static ProgressBar progressBar;
    static TextView noData;
    static ExtendedFloatingActionButton floatingActionButton;
    public  String searchKey="a";
    static Context context;
    int countVal=1;
    static  boolean scannningData=false;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerView);
        searchData=findViewById(R.id.searchData);
        progressBar=findViewById(R.id.progressBar);
        noData=findViewById(R.id.noData);
        context=getApplicationContext();
        floatingActionButton=findViewById(R.id.extendFloatingActionButton);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));// set status background white
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(MainActivity.this,QRCodeScanner.class);
//                startActivity(intent);
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(intent);

            }
        });
        new getCocktailList().execute();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        floatingActionButton.extend();
                        System.out.println("The RecyclerView is not scrolling");
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        floatingActionButton.shrink();
                        System.out.println("Scrolling now");
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        floatingActionButton.shrink();
                        System.out.println("Scroll Settling");
                        break;

                }

        }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

        });
        searchData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(scannningData==false) {
                 Log.d("count", String.valueOf(count));
                Log.d("before", String.valueOf(before));
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                noData.setVisibility(View.GONE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            countVal = count;
                            if (count < before) {
                                if (count == 0) {
                                    searchKey = "";

                                    new getCocktailList().execute();
                                } else {
                                    searchKey = charSequence.toString();

                                    new getCocktailList().execute();
                                }
                            } else {
                                searchKey = charSequence.toString();

                                new getCocktailList().execute();
                            }
                        }

                    }, 1000);
                }



            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public static class getScanResult extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                arrayList=new ArrayList<>();
                noData.setVisibility(View.GONE);

                Call<JsonObject> call = RetrofitClient.getInstance().getMyApi().searchCocktailList(searchData.getText().toString());
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            if (response.isSuccessful() && response.body() != null) {


                                JsonObject object = response.body();
                                Log.d("searchCocktailList", object.toString());
                                Object json = object.get("drinks");
                                if (json instanceof JsonArray) {
                                    JsonArray drinks = object.getAsJsonArray("drinks");
                                    floatingActionButton.setVisibility(View.VISIBLE);
                                    if (drinks.size() == 0) {

                                        noData.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);

                                    } else {
                                        for (int i = 0; i < drinks.size(); i++) {
                                            JsonObject jsonObject = (JsonObject) drinks.get(i);
                                            idDrink = jsonObject.get("idDrink").toString().replaceAll("\"", "");
                                            strDrink = jsonObject.get("strDrink").toString().replaceAll("\"", "");
                                            strDrinkAlternate = jsonObject.get("strDrinkAlternate").toString().replaceAll("\"", "");
                                            strTags = jsonObject.get("strTags").toString().replaceAll("\"", "");
                                            strVideo = jsonObject.get("strVideo").toString().replaceAll("\"", "");
                                            strCategory = jsonObject.get("strCategory").toString().replaceAll("\"", "");
                                            strIBA = jsonObject.get("strIBA").toString().replaceAll("\"", "");
                                            strAlcoholic = jsonObject.get("strAlcoholic").toString().replaceAll("\"", "");
                                            strGlass = jsonObject.get("strGlass").toString().replaceAll("\"", "");
                                            strInstructions = jsonObject.get("strInstructions").toString().replaceAll("\"", "");
                                            strInstructionsES = jsonObject.get("strInstructionsES").toString().replaceAll("\"", "");
                                            strInstructionsDE = jsonObject.get("strInstructionsDE").toString().replaceAll("\"", "");
                                            strInstructionsFR = jsonObject.get("strInstructionsFR").toString().replaceAll("\"", "");
                                            strInstructionsIT = jsonObject.get("strInstructionsIT").toString().replaceAll("\"", "");
                                            strInstructionsZH_HANS = jsonObject.get("strInstructionsZH-HANS").toString().replaceAll("\"", "");
                                            strInstructionsZH_HANT = jsonObject.get("strInstructionsZH-HANT").toString().replaceAll("\"", "");
                                            strDrinkThumb = jsonObject.get("strDrinkThumb").toString().replaceAll("\"", "");
                                            arrayList.add(new CocktailList(idDrink, strDrink, strDrinkAlternate, strTags, strVideo, strCategory, strIBA, strAlcoholic, strGlass,
                                                    strInstructions, strInstructionsES, strInstructionsDE, strInstructionsFR, strInstructionsIT, strInstructionsZH_HANS,
                                                    strInstructionsZH_HANT, strDrinkThumb));

                                        }
                                        adapter = new Adapter(context, arrayList);
                                        recyclerView.setAdapter(adapter);
                                        progressBar.setVisibility(View.GONE);
                                        noData.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        scannningData=false;
                                    }

                                }
                                else  {
                                    String drink = object.get("drinks").toString().replaceAll("\"", "");
                                    if(drink.equals("null")||drink.equals(null)) {
                                        noData.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.GONE);
                                        scannningData=false;
                                    }
                                }
                            } else {
                                progressBar.setVisibility(View.GONE);
                                noData.setVisibility(View.VISIBLE);
//                                Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();

                            }
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
//                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();

                    }
                });
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return true;
        }
    }

    private class getCocktailList extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Log.d("searchKey",searchKey);
                Log.d("countVal", String.valueOf(countVal));
                arrayList=new ArrayList<>();

                if(searchKey.trim().length()<=1 && countVal>0) {
                    Call<JsonObject> call = RetrofitClient.getInstance().getMyApi().cocktailListFirstLetter(searchKey.trim());
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            try {
                                if (response.isSuccessful() && response.body() != null) {
                                    progressBar.setVisibility(View.GONE);
                                    noData.setVisibility(View.GONE);
                                    JsonObject object = response.body();
                                    Log.d("object", object.toString());
                                    Object json = object.get("drinks");
                                    if (json instanceof JsonArray) {
                                        JsonArray drinks = object.getAsJsonArray("drinks");
                                        floatingActionButton.setVisibility(View.VISIBLE);
                                        if (drinks.size() == 0) {

                                            noData.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.GONE);

                                        } else {
                                            for (int i = 0; i < drinks.size(); i++) {
                                                JsonObject jsonObject = (JsonObject) drinks.get(i);
                                                idDrink = jsonObject.get("idDrink").toString().replaceAll("\"", "");
                                                strDrink = jsonObject.get("strDrink").toString().replaceAll("\"", "");
                                                strDrinkAlternate = jsonObject.get("strDrinkAlternate").toString().replaceAll("\"", "");
                                                strTags = jsonObject.get("strTags").toString().replaceAll("\"", "");
                                                strVideo = jsonObject.get("strVideo").toString().replaceAll("\"", "");
                                                strCategory = jsonObject.get("strCategory").toString().replaceAll("\"", "");
                                                strIBA = jsonObject.get("strIBA").toString().replaceAll("\"", "");
                                                strAlcoholic = jsonObject.get("strAlcoholic").toString().replaceAll("\"", "");
                                                strGlass = jsonObject.get("strGlass").toString().replaceAll("\"", "");
                                                strInstructions = jsonObject.get("strInstructions").toString().replaceAll("\"", "");
                                                strInstructionsES = jsonObject.get("strInstructionsES").toString().replaceAll("\"", "");
                                                strInstructionsDE = jsonObject.get("strInstructionsDE").toString().replaceAll("\"", "");
                                                strInstructionsFR = jsonObject.get("strInstructionsFR").toString().replaceAll("\"", "");
                                                strInstructionsIT = jsonObject.get("strInstructionsIT").toString().replaceAll("\"", "");
                                                strInstructionsZH_HANS = jsonObject.get("strInstructionsZH-HANS").toString().replaceAll("\"", "");
                                                strInstructionsZH_HANT = jsonObject.get("strInstructionsZH-HANT").toString().replaceAll("\"", "");
                                                strDrinkThumb = jsonObject.get("strDrinkThumb").toString().replaceAll("\"", "");
                                                arrayList.add(new CocktailList(idDrink, strDrink, strDrinkAlternate, strTags, strVideo, strCategory, strIBA, strAlcoholic, strGlass,
                                                        strInstructions, strInstructionsES, strInstructionsDE, strInstructionsFR, strInstructionsIT, strInstructionsZH_HANS,
                                                        strInstructionsZH_HANT, strDrinkThumb));

                                            }
                                            adapter = new Adapter(getApplicationContext(), arrayList);
                                            recyclerView.setAdapter(adapter);
                                            progressBar.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.VISIBLE);
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                        }

                                    } else {
                                        Log.d("drink", "null");
                                        String drink = object.get("drinks").toString().replaceAll("\"", "");
                                        if (drink.equals("null") || drink.equals(null)) {
                                            noData.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.GONE);
                                        }
                                    }
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    noData.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);

//                                Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();

                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);
//                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();

                        }
                    });
                }
                else{
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            progressBar.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    });


                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return true;
        }
    }
}