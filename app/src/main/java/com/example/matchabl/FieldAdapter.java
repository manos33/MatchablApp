package com.example.matchabl;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.FieldViewHolder> {

    private List<Field> fieldList;
    private FavoritesManager favoritesManager;

    public FieldAdapter(List<Field> fieldList, FavoritesManager favoritesManager) {
        this.fieldList = fieldList;
        this.favoritesManager = favoritesManager;
    }

    @NonNull
    @Override
    public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_field, parent, false);
        return new FieldViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FieldViewHolder holder, int position) {
        Field field = fieldList.get(position);
        Log.d(TAG, "Binding field: " + field.getName());
        holder.bind(field);

        holder.itemView.setOnClickListener(v -> {
            FragmentManager fragmentManager = ((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, FieldProfileFragment.newInstance(String.valueOf(field.getId())));
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        holder.favoriteIcon.setOnClickListener(v -> {
            if (favoritesManager.isFavorite(field.getId())) {
                favoritesManager.removeFavorite(field.getId());
                holder.favoriteIcon.setImageResource(R.drawable.ic_star_outline);
            } else {
                favoritesManager.addFavorite(field.getId());
                holder.favoriteIcon.setImageResource(R.drawable.ic_star_filled);
            }
        });

        if (favoritesManager.isFavorite(field.getId())) {
            holder.favoriteIcon.setImageResource(R.drawable.ic_star_filled);
        } else {
            holder.favoriteIcon.setImageResource(R.drawable.ic_star_outline);
        }
    }


    @Override
    public int getItemCount() {
        return fieldList.size();
    }

    public void updateFields(List<Field> newFields) {
        this.fieldList = newFields;
        notifyDataSetChanged();
    }

    public static class FieldViewHolder extends RecyclerView.ViewHolder {

        private TextView fieldName;
        private TextView fieldAddress;
        private TextView fieldRating;
        private ImageView favoriteIcon;

        public FieldViewHolder(@NonNull View itemView) {
            super(itemView);
            fieldName = itemView.findViewById(R.id.field_name);
            fieldAddress = itemView.findViewById(R.id.field_address);
            fieldRating = itemView.findViewById(R.id.field_rating);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
        }

        public void bind(Field field) {
            fieldName.setText(field.getName());
            fieldAddress.setText(field.getAddress());
            fieldRating.setText(String.valueOf(field.getRating()));
        }
    }
}
