package com.example.pawrescue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LostPetsAdapter extends RecyclerView.Adapter<LostPetsAdapter.LostPetViewHolder> {

    private List<LostPet> lostPets;

    public class LostPetViewHolder extends RecyclerView.ViewHolder {
        TextView petNameTextView;
        // Diğer görünümler

        public LostPetViewHolder(View itemView) {
            super(itemView);
            petNameTextView = itemView.findViewById(R.id.textViewName);
            // Diğer görünümleri bağlayın
        }
    }

    public LostPetsAdapter() {
        lostPets = new ArrayList<>();
    }

    public void setLostPets(List<LostPet> lostPets) {
        this.lostPets = lostPets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LostPetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pet, parent, false);
        return new LostPetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LostPetViewHolder holder, int position) {
        LostPet lostPet = lostPets.get(position);
        holder.petNameTextView.setText(lostPet.getName());
        // Diğer görünümleri güncelleyin
    }

    @Override
    public int getItemCount() {
        return lostPets.size();
    }
}

