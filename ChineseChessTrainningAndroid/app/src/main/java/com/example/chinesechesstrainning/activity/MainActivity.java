package com.example.chinesechesstrainning.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chinesechesstrainning.R;
import com.example.chinesechesstrainning.adapter.TrainingItemAdapter;
import com.example.chinesechesstrainning.model.training.TrainingDTO;
import com.example.chinesechesstrainning.service.MusicService;
import com.example.chinesechesstrainning.support.Support;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton imgBtnBack;
    private ImageButton imgBtnHome;
    private ImageButton imgBtnSpeaker;
    private ImageButton imgBtnMusic;
    private TextView tvTrainingTitle;
    private RecyclerView recyclerView;
    private List<TrainingDTO> trainingDTOs;

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
        tvTrainingTitle = findViewById(R.id.tv_training_title);
        tvTrainingTitle.setSelected(true);
        recyclerView = findViewById(R.id.rc_training_item);

        if (getIntent().getExtras() != null) {
            imgBtnSpeaker.setTag(getIntent().getExtras().getString("speaker"));
            imgBtnMusic.setTag(getIntent().getExtras().getString("music"));

            TrainingDTO trainingDTO = Support.findTrainingById(getIntent().getExtras().getLong("trainingId"));
            if (trainingDTO == null || trainingDTO.getParentTrainingId() == null) {
                imgBtnHome.setVisibility(View.INVISIBLE);
                imgBtnBack.setVisibility(View.INVISIBLE);
                tvTrainingTitle.setText(null);
                trainingDTOs = Support.findAllChildrenTrainingByParentTrainingId(null);
            } else {
                imgBtnHome.setVisibility(View.VISIBLE);
                imgBtnBack.setVisibility(View.VISIBLE);
                String title = getIntent().getExtras().getString("title").replace('-', ' ');
                tvTrainingTitle.setText(title.substring(0, title.lastIndexOf('-')));
                trainingDTOs = Support.findAllChildrenTrainingByParentTrainingId(trainingDTO.getParentTrainingId());
            }

            getIntent().removeExtra("title");
            getIntent().removeExtra("speaker");
            getIntent().removeExtra("music");
            getIntent().removeExtra("trainingId");

        } else {
            imgBtnHome.setVisibility(View.INVISIBLE);
            imgBtnBack.setVisibility(View.INVISIBLE);

            imgBtnMusic.setTag("off");
            imgBtnSpeaker.setTag("off");

            tvTrainingTitle.setText(null);

            trainingDTOs = Support.findAllChildrenTrainingByParentTrainingId(null);
        }

        setImgBtnMusicService(imgBtnMusic.getTag().toString());
        setImgBtnSpeakerService(imgBtnSpeaker.getTag().toString());

        setMatchesIntoRecyclerView(trainingDTOs);
    }

    @Override
    public void onClick(View v) {
        if (v == imgBtnHome) {
            imgBtnBack.setVisibility(View.INVISIBLE);
            imgBtnHome.setVisibility(View.INVISIBLE);
            tvTrainingTitle.setText(null);
            trainingDTOs = Support.findTrainingById(null).getChildTrainings();
            setMatchesIntoRecyclerView(trainingDTOs);

        } else if (v == imgBtnBack) {
            TrainingDTO parentTrainingDTO = Support.findTrainingById(trainingDTOs.get(0).getParentTrainingId());
            trainingDTOs = Support.findAllChildrenTrainingByParentTrainingId(parentTrainingDTO.getParentTrainingId());

            if (trainingDTOs.get(0).getParentTrainingId() == null) {
                imgBtnHome.setVisibility(View.INVISIBLE);
                imgBtnBack.setVisibility(View.INVISIBLE);
            }

            int lastedEnterChar = tvTrainingTitle.getText().toString().lastIndexOf('-');
            if (lastedEnterChar > 0) {
                tvTrainingTitle.setText(tvTrainingTitle.getText().toString().substring(0, lastedEnterChar));
            } else {
                tvTrainingTitle.setText("");
            }

            setMatchesIntoRecyclerView(trainingDTOs);
        } else if (v == imgBtnSpeaker) {
            setImgBtnSpeakerService(imgBtnSpeaker.getTag().equals("on") ? "off" : "on");
        } else if (v == imgBtnMusic) {
            setImgBtnMusicService(imgBtnMusic.getTag().equals("on") ? "off" : "on");
        }
    }

    @SuppressLint("SetTextI18n")
    private void setMatchesIntoRecyclerView(List<TrainingDTO> trainingDTOS) {
        recyclerView.setAdapter(new TrainingItemAdapter((ArrayList<TrainingDTO>) trainingDTOS, this, trainingClicked -> {
            TrainingDTO trainingDTO = Support.findTrainingById(trainingClicked.getId());

            String title = tvTrainingTitle.getText().length() == 0
                    ? trainingDTO.getTitle() : tvTrainingTitle.getText() + "-" + trainingDTO.getTitle();

            if (!trainingDTO.getChildTrainings().isEmpty()) {
                imgBtnBack.setVisibility(View.VISIBLE);
                imgBtnHome.setVisibility(View.VISIBLE);
                tvTrainingTitle.setText(title);
                setMatchesIntoRecyclerView(trainingDTO.getChildTrainings());
                trainingDTOs = trainingDTO.getChildTrainings();
            } else {
                if (imgBtnMusic.getTag().toString().equals("on")) {
                    stopService(new Intent(this, MusicService.class));
                }
                Intent intent = new Intent(this, PlayBoardActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("speaker", imgBtnSpeaker.getTag().toString());
                intent.putExtra("music", imgBtnMusic.getTag().toString());
                intent.putExtra("trainingId", trainingDTO.getId());
                startActivity(intent);
            }
        }));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setImgBtnMusicService(String tag) {
        imgBtnMusic.setTag(tag);
        if (imgBtnMusic.getTag().toString().equals("on")) {
            imgBtnMusic.setBackground(getDrawable(R.drawable.music_on));
            startService(new Intent(this, MusicService.class));
        } else {
            imgBtnMusic.setBackground(getDrawable(R.drawable.music_off));
            stopService(new Intent(this, MusicService.class));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setImgBtnSpeakerService(String tag) {
        imgBtnSpeaker.setTag(tag);
        if (imgBtnSpeaker.getTag().toString().equals("on")) {
            imgBtnSpeaker.setBackground(getDrawable(R.drawable.speaker_on));
        } else {
            imgBtnSpeaker.setBackground(getDrawable(R.drawable.speaker_off));
        }
    }

}