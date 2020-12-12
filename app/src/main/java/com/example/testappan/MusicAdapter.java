package com.example.testappan;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Musics> mFiles;


    public MusicAdapter(Context mContext, ArrayList<Musics> mFiles) {
        this.mContext = mContext;
        this.mFiles = mFiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.file_name.setText(mFiles.get(position).getTitle());

        byte [] image = getArt(mFiles.get(position).getPath());
        if( image != null){
            Glide.with(mContext).asBitmap()
                    .load(image)
                    .into(holder.img_art);
        }
        else{
            Glide.with(mContext)
                    .load(R.drawable.ic_sss)
                    .into(holder.img_art);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext ,PlayerActivity.class);
                intent.putExtra("position",position);
                mContext.startActivity(intent);
            }
        });

        holder.menu_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext,v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()){
                        case R.id.delete:
                            Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                            deleteFile(position,v);
                            break;
                    }
                    return true;
                });
            }
        });
    }

    private void deleteFile(int position, View view) {
        Uri conUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(mFiles.get(position).getId()));

        //Chi cho phep xoa nhac trong bo nho may
        File file = new File(mFiles.get(position).getPath());
        boolean deleted = file.delete();
        // kiem tra file co trong bo nho may hay ko
        if(deleted){
            mContext.getContentResolver().delete(conUri,null,null);
            mFiles.remove(position);
            notifyItemRemoved(position);
            notifyItemChanged(position,mFiles.size());
            Snackbar.make(view,"deleted succesfully!", Snackbar.LENGTH_LONG)
                    .show();;
        }
        // truong file o the SD
        else{
            Snackbar.make(view,"Can't delete", Snackbar.LENGTH_LONG)
                    .show();;
        }



    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_art, menu_more;
        TextView file_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_art = itemView.findViewById(R.id.img_music);
            file_name = itemView.findViewById(R.id.music_file_name);
            menu_more = itemView.findViewById(R.id.menu_more);
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
