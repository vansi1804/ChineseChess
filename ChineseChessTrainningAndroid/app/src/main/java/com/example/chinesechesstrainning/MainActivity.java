package com.example.chinesechesstrainning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chinesechesstrainning.adapter.MatchAdapter;
import com.example.chinesechesstrainning.service.IAPIService;
import com.example.chinesechesstrainning.service.MusicService;
import com.example.chinesechesstrainning.support.DataTest;
import com.example.chinesechesstrainning.model.Match;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton imgBtnBack;
    private ImageButton imgBtnHome;
    private ImageButton imgBtnSpeaker;
    private ImageButton imgBtnMusic;
    private TextView tvMatchTitle;
    private RecyclerView recyclerView;
    private ArrayList<Match> matches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgBtnHome = findViewById(R.id.img_btn_home);
        imgBtnHome.setOnClickListener(this);
        imgBtnBack = findViewById(R.id.img_btn_back);
        imgBtnBack.setOnClickListener(this);
        imgBtnSpeaker = findViewById(R.id.img_btn_speaker);
        imgBtnSpeaker.setOnClickListener(this);
        imgBtnMusic = findViewById(R.id.img_btn_music);
        imgBtnMusic.setOnClickListener(this);
        tvMatchTitle = findViewById(R.id.tv_match_title);
        recyclerView = findViewById(R.id.match_recycler_view);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tvMatchTitle.setText(bundle.getString("title"));
            if (bundle.getString("speaker").equals("on")){
                imgBtnSpeaker.setTag("on");
                imgBtnSpeaker.setImageResource(R.drawable.speaker_on);
            }else{
                imgBtnSpeaker.setTag("off");
                imgBtnSpeaker.setImageResource(R.drawable.speaker_off);
            }
            if (bundle.getString("music").equals("on")){
                imgBtnMusic.setTag("on");
                imgBtnMusic.setImageResource(R.drawable.music_on);
                startService(new Intent(this,MusicService.class));
            }else{
                imgBtnMusic.setTag("off");
                imgBtnMusic.setImageResource(R.drawable.music_off);
            }
            matches = DataTest.getParentMatchById(bundle.getInt("match id")) == null?
                      DataTest.getMatches()
                    :DataTest.getChildMatchesById(DataTest.getParentMatchById(bundle.getInt("match id")).getId());
        }else{
            imgBtnHome.setVisibility(View.INVISIBLE);
            imgBtnBack.setVisibility(View.INVISIBLE);
            imgBtnSpeaker.setTag("on");
            imgBtnSpeaker.setImageResource(R.drawable.speaker_on);
            imgBtnMusic.setTag("on");
            imgBtnMusic.setImageResource(R.drawable.music_on);
            startService(new Intent(this,MusicService.class));
  //          setMatchesFromFindPiecesAPI();
            matches = DataTest.getMatches();
        }
        setMatchesIntoRecyclerView();
    }

    @Override
    public void onClick(View v) {
        if (v == imgBtnHome){
            matches = DataTest.getMatches();
            setMatchesIntoRecyclerView();
            imgBtnBack.setVisibility(View.INVISIBLE);
            imgBtnHome.setVisibility(View.INVISIBLE);
            tvMatchTitle.setText(null);
        }else if(v == imgBtnBack){
            int lastedEnterChar = tvMatchTitle.getText().toString().lastIndexOf('\n');
            if (lastedEnterChar > 0){
                tvMatchTitle.setText(tvMatchTitle.getText().toString()
                        .substring(0,lastedEnterChar));
            }else{
                tvMatchTitle.setText(null);
            }

            Match parentMatch = DataTest.getParentMatchById(matches.get(0).getId());
            if (parentMatch != null){
                Match grandMatch = DataTest.getParentMatchById(parentMatch.getId());
                if (grandMatch != null){
                    matches = DataTest.getChildMatchesById(grandMatch.getId());
                }else{
                    matches = DataTest.getMatches();
                    imgBtnHome.setVisibility(View.INVISIBLE);
                    imgBtnBack.setVisibility(View.INVISIBLE);
                }
                setMatchesIntoRecyclerView();
            }
        }else if (v == imgBtnSpeaker){
            if (imgBtnSpeaker.getTag().equals("on")){
                imgBtnSpeaker.setImageResource(R.drawable.speaker_off);
                imgBtnSpeaker.setTag("off");
            }else{
                imgBtnSpeaker.setImageResource(R.drawable.speaker_on);
                imgBtnSpeaker.setTag("on");
            }
        }else if (v == imgBtnMusic){
            if (imgBtnMusic.getTag().equals("on")){
                imgBtnMusic.setImageResource(R.drawable.music_off);
                imgBtnMusic.setTag("off");
                stopService(new Intent(this, MusicService.class));
            }else{
                imgBtnMusic.setImageResource(R.drawable.music_on);
                imgBtnMusic.setTag("on");
                startService(new Intent(this, MusicService.class));
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void setMatchesIntoRecyclerView(){
        recyclerView.setAdapter(new MatchAdapter(this.matches, this, matchClicked -> {
            imgBtnBack.setVisibility(View.VISIBLE);
            imgBtnHome.setVisibility(View.VISIBLE);
            ArrayList<Match> childMatch = DataTest.getChildMatchesById(matchClicked.getId());
            if (!childMatch.isEmpty()){
                if (tvMatchTitle.getText().length() == 0){
                tvMatchTitle.setText(matchClicked.getName());
                }else{
                    tvMatchTitle.setText(tvMatchTitle.getText() + "\n" + matchClicked.getName());
                }
                matches = childMatch;
                setMatchesIntoRecyclerView();
            }else {
                if (imgBtnMusic.getTag().equals("on")){
                    stopService(new Intent(this,MusicService.class));
                }
                Intent intent = new Intent(this, PlayBoardActivity.class);
                intent.putExtra("title", tvMatchTitle.getText() + "\n" + matchClicked.getName());
                intent.putExtra("speaker",imgBtnSpeaker.getTag().toString());
                intent.putExtra("music",imgBtnMusic.getTag().toString());
                intent.putExtra("match id", matchClicked.getId());
                startActivity(intent);
            }
        }));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void setMatchesFromFindPiecesAPI(){
        IAPIService.apiService.getMatches().enqueue(new Callback<ArrayList<Match>>() {
            @Override
            public void onResponse(Call<ArrayList<Match>> call, Response<ArrayList<Match>> response) {
                matches = response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<Match>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Call API Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

}