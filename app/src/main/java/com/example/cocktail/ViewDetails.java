package com.example.cocktail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewDetails extends AppCompatActivity {
     String drinkName,drinkImageUrl,description,glassName,qrResult;
     TextView drinkNameText,descriptionText,glassNameText;
     ImageView drinkNameImageUrl,backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);
        drinkNameText=findViewById(R.id.drinkName);
        descriptionText=findViewById(R.id.description);
        glassNameText=findViewById(R.id.glassName);
        backPressed=findViewById(R.id.backPressed);
        drinkNameImageUrl=findViewById(R.id.drinkThumb);
        backPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
         if(getIntent().hasExtra("drinkName")) {
             drinkName = getIntent().getStringExtra("drinkName");
             drinkNameText.setText(drinkName);
         }
        if(getIntent().hasExtra("drinkImageUrl")) {

            drinkImageUrl = getIntent().getStringExtra("drinkImageUrl");
            Glide.with(this)
                    .load(drinkImageUrl)
                    .into(drinkNameImageUrl);
        }
        if(getIntent().hasExtra("description")) {

            description = getIntent().getStringExtra("description");
            Log.d("description",description);
            descriptionText.setText(Html.fromHtml(description));
        }

        if(getIntent().hasExtra("glassName")) {

            glassName = getIntent().getStringExtra("glassName");
            glassNameText.setText(glassName);
        }
         if(getIntent().hasExtra("qrResult")){
             qrResult = getIntent().getStringExtra("qrResult");
//             new cocktailDetails().execute();
         }


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));

    }

}