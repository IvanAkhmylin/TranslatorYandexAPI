package com.example.translator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.ViewHolder> {
    private ArrayList<ItemsManager> mListItem;
    private Context context;

    public AdapterRecyclerView(ArrayList<ItemsManager> favoriteItems) {
        mListItem = favoriteItems;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView mOriginalText;
        public TextView mTranslatedText;
        private CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            mOriginalText = itemView.findViewById(R.id.originalText);
            mTranslatedText = itemView.findViewById(R.id.translatedText);

        }


    }

    public AdapterRecyclerView(ArrayList<ItemsManager> itemList, Context context){
            mListItem = itemList;
            this.context  = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_view,viewGroup,false);
        ViewHolder vh = new  ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final ItemsManager item = mListItem.get(i);

        viewHolder.mOriginalText.setText(item.getOriginalText());
        viewHolder.mTranslatedText.setText(item.getTranslatedText());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = ((MainActivity)context).findViewById(R.id.editTextForResult);
                TextView textView = ((MainActivity)context).findViewById(R.id.editTextResult);
                Spinner spinner  = ((MainActivity)context).findViewById(R.id.spinnerResult);
                RecyclerView recyclerView = ((MainActivity) context).findViewById(R.id.recyclerView);

                editText.setText(item.getOriginalText());
                textView.setText(item.getTranslatedText());
                Log.d("TAG"," on click " + item.getLang());
                spinner.setSelection(Languages.getItemPosition(item.getLang()));
                Languages.setBuferForAutoDetectedLang(item.getLang());
                mListItem.remove(viewHolder.getAdapterPosition());
                mListItem.add(getItemCount() , item);
                notifyDataSetChanged();

                recyclerView.setVisibility(View.GONE);
            }
        });



    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }


}
