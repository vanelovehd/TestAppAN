package com.example.testappan;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AlbumDetailAdapter extends RecyclerView.Adapter<AlbumDetailAdapter.ViewHolder> {

    private Context mContext;
    static ArrayList<Musics> albumFiles;
    View view;

    public AlbumDetailAdapter(Context mContext, ArrayList<Musics> albumFiles) {
        this.mContext = mContext;
        this.albumFiles = albumFiles;
    }



    @NonNull
    @Override
    public AlbumDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.music_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumDetailAdapter.ViewHolder holder, int position) {
        holder.music_file_name.setText(albumFiles.get(position).getTitle());

        byte [] image = getArt(albumFiles.get(position).getPath());
        if( image != null){
            Glide.with(mContext).asBitmap()
                    .load(image)
                    .into(holder.img_music);
        }
        else{
            Glide.with(mContext)
                    .load(R.drawable.ic_ss)
                    .into(holder.img_music);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext,PlayerActivity.class);
                in.putExtra("guiDL","albumDetails");
                in.putExtra("position",position);
                mContext.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {

        return albumFiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_music;
        TextView music_file_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_music = itemView.findViewById(R.id.img_music);
            music_file_name = itemView.findViewById(R.id.music_file_name);
        }
    }

    private byte[] getArt(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return  art;
    }
}
