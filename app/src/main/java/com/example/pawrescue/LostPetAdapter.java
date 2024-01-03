package com.example.pawrescue;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LostPetAdapter extends RecyclerView.Adapter<LostPetAdapter.PetViewHolder> {

    private List<LostPet> lostPetList;
    private Context context;
    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    public LostPetAdapter(List<LostPet> lostPetList, Context context) {
        this.lostPetList = lostPetList;
        this.context = context;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lost_pet, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        LostPet lostPet = lostPetList.get(position);

        holder.textViewPetName.setText("Pet Name: " + lostPet.getName());

        holder.textViewPetType.setText("Pet Type: " + lostPet.getType());

        holder.textViewPetAge.setText("Pet Age: " + lostPet.getAge());

        holder.textViewPetGender.setText("Pet Gender: " + lostPet.getGender());

        holder.textViewLostAddress.setText("Lost Address: " + lostPet.getLostAddress());

        Bitmap photoBitmap = lostPet.getPhotoBitmap();
        if (photoBitmap != null) {
            Bitmap smallerBitmap = makeSmaller(photoBitmap, 200);
            holder.imageViewPet.setImageBitmap(smallerBitmap);
        } else {
            holder.imageViewPet.setImageResource(R.drawable.default_pet);
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(holder.getAdapterPosition());
                }
                return true;
            }
        });
    }

    public Bitmap makeSmaller(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float ratio = (float) width / (float) height;

        if (ratio > 1) {
            width = maxSize;
            height = (int) (width / ratio);
        } else {
            height = maxSize;
            width = (int) (height * ratio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public int getItemCount() {
        return lostPetList.size();
    }


    public static class PetViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPet;
        TextView textViewPetName;
        TextView textViewPetType;
        TextView textViewPetAge;
        TextView textViewPetGender;
        TextView textViewLostAddress;
        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPet = itemView.findViewById(R.id.imageViewPet);
            textViewPetName = itemView.findViewById(R.id.textViewPetName);
            textViewPetType = itemView.findViewById(R.id.textViewPetType);
            textViewPetAge = itemView.findViewById(R.id.textViewPetAge);
            textViewPetGender = itemView.findViewById(R.id.textViewPetGender);
            textViewLostAddress = itemView.findViewById(R.id.textViewLostAddress);
        }

    }
}