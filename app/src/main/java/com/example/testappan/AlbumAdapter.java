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

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private  Context mContext;
    ArrayList<Musics> albumFiles;
    View view;

    public AlbumAdapter(Context mContext, ArrayList<Musics> albumFiles) {
        this.mContext = mContext;
        this.albumFiles = albumFiles;
    }



    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.album_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, int position) {
        holder.album_name.setText(albumFiles.get(position).getAlbum());

        byte [] image = getArt(albumFiles.get(position).getPath());
        if( image != null){
            Glide.with(mContext).asBitmap()
                    .load(image)
                    .into(holder.img_album);
        }
        else{
            Glide.with(mContext)
                    .load(R.drawable.ic_ss)
                    .into(holder.img_album);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext,AlbumDetailsActivity.class);
                i.putExtra("albumName",albumFiles.get(position).getAlbum());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {

        return albumFiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_album;
        TextView album_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_album = itemView.findViewById(R.id.img_album);
            album_name = itemView.findViewById(R.id.name_album);
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
