package com.example.cocktail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter  {
    Context context;
    ArrayList<CocktailList> arrayList;
    public Adapter(Context context, ArrayList<CocktailList> arrayList){
      this.context=context;
      this.arrayList=arrayList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout,parent,false);

        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
    AdapterViewHolder adapterViewHolder=(AdapterViewHolder) holder;
        Glide.with(context)
                .load(arrayList.get(position).getStrDrinkThumb())
                .into(adapterViewHolder.drink_thumb);
        adapterViewHolder.drink_name.setText(arrayList.get(position).getStrDrink());
        String description=("<b>"+arrayList.get(position).getStrInstructions()+"</b>"
                +"<br>"+arrayList.get(position).getStrInstructionsES()+"</br>"
                +"<br>"+arrayList.get(position).getStrInstructionsDE()+"</br>"
                +"<br>"+arrayList.get(position).getStrInstructionsFR()+"</br>"
                +"<br>"+arrayList.get(position).getStrInstructionsIT()+"</br>"
                +"<br>"+arrayList.get(position).getStrInstructionsZH_HANS()+"</br>"
                + "<br>"+arrayList.get(position).getStrInstructionsZH_HANT()+"</br>").replaceAll("null","").replaceAll("<br></br>","");
        adapterViewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ViewDetails.class);
                intent.putExtra("drinkName",arrayList.get(position).getStrDrink());
                intent.putExtra("drinkImageUrl",arrayList.get(position).getStrDrinkThumb());
                intent.putExtra("description",arrayList.get(position).getStrInstructions());
                intent.putExtra("glassName",arrayList.get(position).getStrGlass());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(arrayList!=null) {
            return arrayList.size();
        }
        else{
            return 0;
        }
    }

    class AdapterViewHolder extends RecyclerView.ViewHolder{
         ImageView drink_thumb;
         TextView drink_name;
         LinearLayout mainLayout;
        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            drink_thumb=itemView.findViewById(R.id.drink_thumb);
            drink_name=itemView.findViewById(R.id.drink_name);
            mainLayout=itemView.findViewById(R.id.mainLayout);
        }
    }
}
