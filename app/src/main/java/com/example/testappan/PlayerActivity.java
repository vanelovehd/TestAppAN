package com.example.testappan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;

import com.example.testappan.databinding.ActivityPlayerBinding;

import java.util.ArrayList;
import java.util.Random;

import static com.example.testappan.AlbumDetailAdapter.albumFiles;
import static com.example.testappan.MainActivity.musicsList;
import static com.example.testappan.MainActivity.repeatBoo;
import static com.example.testappan.MainActivity.shuffleBoo;

public class PlayerActivity extends AppCompatActivity {
    ActivityPlayerBinding binding;

    static ArrayList<Musics> listSongs = new ArrayList<>();
    int position = -1;
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();

    private Thread playThread,prevThread,nextThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_player);
        getIntenMethod();
        binding.songName.setText(listSongs.get(position).getTitle());
        binding.songArtist.setText(listSongs.get(position).getArtist());

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // điều hướng thanh process seekbar di chuyển
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);
                }
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        // luồng thiết lập thời gian thực khi thanh chạy
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    binding.seekBar.setProgress(mCurrentPosition);
                    binding.durationPlay.setText(formattedTime(mCurrentPosition));
                    int durationTotal = Integer.parseInt(listSongs.get(position).getDuration())/ 1000;
                    binding.durationTotal.setText(formattedTime(durationTotal));

                }
                handler.postDelayed(this,1000);
            }
        });

        binding.btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuffleBoo){
                    shuffleBoo = false;
                    binding.btnShuffle.setImageResource(R.drawable.ic_shuffle_off);
                }
                else {
                    shuffleBoo = true;
                    binding.btnShuffle.setImageResource(R.drawable.ic_shuffle_on);
                }
            }
        });

        binding.btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeatBoo){
                    repeatBoo = false;
                    binding.btnRepeat.setImageResource(R.drawable.ic_repeat_off);
                }
                else {
                    repeatBoo = true;
                    binding.btnRepeat.setImageResource(R.drawable.ic_repeat_on);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        playThreadBtn();
        prevThreadBtn();
        nextThreadBtn();
        super.onResume();
    }

    private void nextThreadBtn() {
        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();

                binding.btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextBtnClicked();
                    }
                });
            }
        };
        nextThread.start();

    }

    private void nextBtnClicked() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoo && !repeatBoo){
                position = getRandom(listSongs.size()-1);
            }
            else if(!shuffleBoo && !repeatBoo){
                position =((position+1) % listSongs.size());
            }

            //
            else if(repeatBoo && !shuffleBoo){
                position =((position) % listSongs.size());
            }

            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            binding.songName.setText(listSongs.get(position).getTitle());
            binding.songArtist.setText(listSongs.get(position).getArtist());
            binding.seekBar.setMax(mediaPlayer.getDuration()/1000);

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });

            binding.playPauseBtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }
        else{
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoo && !repeatBoo){
                position = getRandom(listSongs.size()-1);
            }
            else if(!shuffleBoo && !repeatBoo){
                position =((position+1) % listSongs.size());
            }
            else if(repeatBoo && !shuffleBoo){
                position =((position) % listSongs.size());
            }
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            binding.songName.setText(listSongs.get(position).getTitle());
            binding.songArtist.setText(listSongs.get(position).getArtist());
            binding.seekBar.setMax(mediaPlayer.getDuration()/1000);

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });

            binding.playPauseBtn.setImageResource(R.drawable.ic_play);
        }
    }

    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i+1);
    }

    private void prevThreadBtn() {
        prevThread = new Thread(){
            @Override
            public void run() {
                super.run();

                binding.btnPrev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prevBtnClicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void prevBtnClicked() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoo && !repeatBoo){
                position = getRandom(listSongs.size()-1);
            }
            else if(!shuffleBoo && !repeatBoo){
                position =((position-1) < 0 ? (listSongs.size()-1) : (position-1));
            }
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            binding.songName.setText(listSongs.get(position).getTitle());
            binding.songArtist.setText(listSongs.get(position).getArtist());
            binding.seekBar.setMax(mediaPlayer.getDuration()/1000);

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });

            binding.playPauseBtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }
        else{
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoo && !repeatBoo){
                position = getRandom(listSongs.size()-1);
            }
            else if(!shuffleBoo && !repeatBoo){
                position =((position-1) < 0 ? (listSongs.size()-1) : (position-1));
            }

            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            binding.songName.setText(listSongs.get(position).getTitle());
            binding.songArtist.setText(listSongs.get(position).getArtist());
            binding.seekBar.setMax(mediaPlayer.getDuration()/1000);

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });

            binding.playPauseBtn.setImageResource(R.drawable.ic_play);
        }
    }

    private void playThreadBtn() {
        playThread = new Thread(){
            @Override
            public void run() {
                super.run();

                binding.playPauseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playPauseBtnClicked() {
        if(mediaPlayer.isPlaying()){
            binding.playPauseBtn.setImageResource(R.drawable.ic_play);
            mediaPlayer.pause();
            binding.seekBar.setMax(mediaPlayer.getDuration()/1000);

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }
        else{
            binding.playPauseBtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
            binding.seekBar.setMax(mediaPlayer.getDuration()/1000);

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }
    }

    private String formattedTime(int mCurrentPosition) {
        String timeCu = "";
        String timeMoi = "";
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes =  String.valueOf(mCurrentPosition / 60);

        timeCu = minutes+":"+seconds;
        timeMoi = minutes+":"+"0"+seconds;

        if(seconds.length() == 1){
            return  timeMoi;
        }else{
            return  timeCu;
        }
    }

    private void getIntenMethod() {
        position = getIntent().getIntExtra("position",-1);
        String guiDL = getIntent().getStringExtra("guiDL");
        // neu nhan thong tin tu album phat nhac tu list album
        if(guiDL != null && guiDL.equals("albumDetails")){
            listSongs = albumFiles;
        }
        // nguoc lai phat trong list Music
        else {
            listSongs = musicsList;
        }

        if(listSongs != null){
            binding.playPauseBtn.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(listSongs.get(position).getPath());

        }
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        else{
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
            binding.seekBar.setMax(mediaPlayer.getDuration()/1000);
        }

    }


}