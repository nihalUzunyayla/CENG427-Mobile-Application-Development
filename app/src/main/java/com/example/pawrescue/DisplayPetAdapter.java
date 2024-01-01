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

public class DisplayPetAdapter extends RecyclerView.Adapter<DisplayPetAdapter.PetViewHolder> {

    private List<Pet> petList;
    private Context context;
    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    public DisplayPetAdapter(List<Pet> petList, Context context) {
        this.petList = petList;
        this.context = context;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        holder.textViewName.setText(String.valueOf(petList.get(position).getName()));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(holder.getAdapterPosition());
                }
                return true;
            }
        });

        Bitmap photoBitmap = petList.get(position).getPhotoBitmap();
        if (photoBitmap != null) {
            Bitmap smallerBitmap = makeSmaller(photoBitmap, 200);
            holder.imageViewPet.setImageBitmap(smallerBitmap);
        } else {
            holder.imageViewPet.setImageResource(R.drawable.camera);
        }
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
        return petList.size();
    }


    public static class PetViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPet;
        TextView textViewName;
        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPet = itemView.findViewById(R.id.imageViewPet);
            textViewName = itemView.findViewById(R.id.textViewName);
        }
    }
}