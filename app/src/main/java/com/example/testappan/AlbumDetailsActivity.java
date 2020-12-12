package com.example.testappan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.example.testappan.MainActivity.musicsList;

public class AlbumDetailsActivity extends AppCompatActivity {
    RecyclerView rcvAlbDetail;
    ImageView imgAlbPhoto;
    ArrayList<Musics> albumSongs = new ArrayList<>();
    String albumName;
    AlbumDetailAdapter albumDetailAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        rcvAlbDetail = findViewById(R.id.rcvAlbDetail);
        imgAlbPhoto = findViewById(R.id.imgAlbPhoto);
        albumName = this.getIntent().getStringExtra("albumName");
        int j = 0;
        // tim va add ten album vao list albumSongs
        for(int i = 0; i< musicsList.size();i++){
            if(albumName.equals(musicsList.get(i).getAlbum())){
               albumSongs.add(j,musicsList.get(i));
               j++;
            }
        }

        byte [] image = getArt(albumSongs.get(0).getPath());
        if( image != null){
            Glide.with(this)
                    .load(image)
                    .into(imgAlbPhoto);
        }
        else{
            Glide.with(this)
                    .load(R.drawable.ic_ss)
                    .into(imgAlbPhoto);
        }

    }

    // theem cac bai nhac vao adapter
    @Override
    protected void onResume() {
        super.onResume();
        if(!(albumSongs.size() < 1)){
            albumDetailAdapter = new AlbumDetailAdapter(this,albumSongs);
            rcvAlbDetail.setAdapter(albumDetailAdapter);
            rcvAlbDetail.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
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