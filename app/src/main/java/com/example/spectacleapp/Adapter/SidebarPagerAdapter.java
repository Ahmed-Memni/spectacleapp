package com.example.spectacleapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.spectacleapp.ImageDetailActivity;
import com.example.spectacleapp.Models.Spectacles;
import com.example.spectacleapp.R;

import java.util.List;

public class SidebarPagerAdapter extends RecyclerView.Adapter<SidebarPagerAdapter.SidebarViewHolder> {

    private Context context;
    private List<Spectacles> spectaclesList;

    public SidebarPagerAdapter(Context context, List<Spectacles> spectaclesList) {
        this.context = context;
        this.spectaclesList = spectaclesList;
    }

    @NonNull
    @Override
    public SidebarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sidebar, parent, false);
        return new SidebarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SidebarViewHolder holder, int position) {
        Spectacles spectacle = spectaclesList.get(position);

        // Load the poster image using Glide
        Glide.with(context)
                .load(spectacle.getPoster())
                .apply(new RequestOptions().fitCenter()) // shows full image without cropping
                .into(holder.imageView);

        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImageDetailActivity.class);
            intent.putExtra("spectacle", spectacle); // Pass the entire spectacle object
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return spectaclesList.size();
    }

    public static class SidebarViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public SidebarViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSingle);
        }
    }
}
