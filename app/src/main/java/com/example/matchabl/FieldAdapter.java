package com.example.matchabl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.FieldViewHolder> {

    private List<Field> fieldList;

    public FieldAdapter(List<Field> fieldList) {
        this.fieldList = fieldList;
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
        holder.bind(field);

        // Ακροατής κλικ στο στοιχείο της λίστας
        holder.itemView.setOnClickListener(v -> {
            // Δημιουργία του FieldProfileFragment με το facilityId
            FieldProfileFragment fragment = new FieldProfileFragment();
            Bundle args = new Bundle();
            args.putString("fieldId", String.valueOf(field.getId()));
            fragment.setArguments(args);

            // Πραγματοποίηση μετάβασης στο FieldProfileFragment
            FragmentManager fragmentManager = ((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    @Override
    public int getItemCount() {
        return fieldList.size();
    }

    public static class FieldViewHolder extends RecyclerView.ViewHolder {

        private TextView fieldName;
        private TextView fieldAddress;
        private TextView fieldRating;

        public FieldViewHolder(@NonNull View itemView) {
            super(itemView);
            fieldName = itemView.findViewById(R.id.field_name);
            fieldAddress = itemView.findViewById(R.id.field_address);
            fieldRating = itemView.findViewById(R.id.field_rating);
        }

        public void bind(Field field) {
            fieldName.setText(field.getName());
            fieldAddress.setText(field.getAddress());
            fieldRating.setText(String.valueOf(field.getRating()));
        }
    }
}
