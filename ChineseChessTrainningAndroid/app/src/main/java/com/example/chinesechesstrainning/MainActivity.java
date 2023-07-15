package com.example.chinesechesstrainning;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chinesechesstrainning.adapter.TrainingAdapter;
import com.example.chinesechesstrainning.model.Training;
import com.example.chinesechesstrainning.service.MusicService;
import com.example.chinesechesstrainning.service.SpeakerService;
import com.example.chinesechesstrainning.support.DataTest;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton imgBtnBack;
    private ImageButton imgBtnHome;
    private ImageButton imgBtnSpeaker;
    private ImageButton imgBtnMusic;
    private TextView tvTrainingTitle;
    private RecyclerView recyclerView;
    private List<Training> trainings;

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
        recyclerView = findViewById(R.id.match_recycler_view);

        if (getIntent().getExtras() != null) {
            setImgBtnMusicService(getIntent().getExtras().getString("music"));
            setImgBtnSpeakerService(getIntent().getExtras().getString("speaker"));

            Training training = DataTest.findTrainingById(getIntent().getExtras().getLong("trainingId"));
            if (training == null) {
                imgBtnHome.setVisibility(View.INVISIBLE);
                imgBtnBack.setVisibility(View.INVISIBLE);
                tvTrainingTitle.setText(null);
                this.trainings = DataTest.findAllChildrenTrainingByParentTrainingId(null);
            } else {
                if (training.getParentTrainingId() == null) {
                    imgBtnHome.setVisibility(View.INVISIBLE);
                    imgBtnBack.setVisibility(View.INVISIBLE);
                    tvTrainingTitle.setText(null);
                } else {
                    imgBtnHome.setVisibility(View.VISIBLE);
                    imgBtnBack.setVisibility(View.VISIBLE);
                    String title = getIntent().getExtras().getString("title");
                    tvTrainingTitle.setText(title.substring(0, title.lastIndexOf('\n')));
                }
                this.trainings = DataTest.findAllChildrenTrainingByParentTrainingId(training.getParentTrainingId());
            }

            getIntent().removeExtra("title");
            getIntent().removeExtra("speaker");
            getIntent().removeExtra("music");
            getIntent().removeExtra("trainingId");

        } else {
            imgBtnHome.setVisibility(View.INVISIBLE);
            imgBtnBack.setVisibility(View.INVISIBLE);

            setImgBtnMusicService("off");
            setImgBtnSpeakerService("off");

            this.trainings = DataTest.findAllChildrenTrainingByParentTrainingId(null);
        }
        setMatchesIntoRecyclerView(this.trainings);
    }

    @Override
    public void onClick(View v) {
        if (v == imgBtnHome) {
            imgBtnBack.setVisibility(View.INVISIBLE);
            imgBtnHome.setVisibility(View.INVISIBLE);
            tvTrainingTitle.setText(null);
            this.trainings = DataTest.findAllChildrenTrainingByParentTrainingId(null);
            setMatchesIntoRecyclerView(this.trainings);

        } else if (v == imgBtnBack) {
            Training parentTraining = DataTest.findTrainingById(this.trainings.get(0).getParentTrainingId());
            if (parentTraining != null) {
                this.trainings = DataTest.findAllChildrenTrainingByParentTrainingId(parentTraining.getParentTrainingId());
            } else {
                this.trainings = DataTest.findAllChildrenTrainingByParentTrainingId(null);
            }

            if (this.trainings.get(0).getParentTrainingId() == null) {
                imgBtnHome.setVisibility(View.INVISIBLE);
                imgBtnBack.setVisibility(View.INVISIBLE);
            }
            int lastedEnterChar = tvTrainingTitle.getText().toString().lastIndexOf('\n');
            if (lastedEnterChar > 0) {
                tvTrainingTitle.setText(tvTrainingTitle.getText().toString().substring(0, lastedEnterChar));
            } else {
                tvTrainingTitle.setText(null);
            }
            setMatchesIntoRecyclerView(this.trainings);

        } else if (v == imgBtnSpeaker) {
            if (imgBtnSpeaker.getTag().toString().equals("on")) {
                imgBtnSpeaker.setImageResource(R.drawable.speaker_off);
                imgBtnSpeaker.setTag("off");
            } else {
                imgBtnSpeaker.setImageResource(R.drawable.speaker_on);
                imgBtnSpeaker.setTag("on");
            }

        } else if (v == imgBtnMusic) {
            if (imgBtnMusic.getTag().toString().equals("on")) {
                setImgBtnSpeakerService("off");
            } else {
                setImgBtnSpeakerService("on");
                startService(new Intent(this, SpeakerService.class));
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void setMatchesIntoRecyclerView(List<Training> trainings) {
        recyclerView.setAdapter(
                new TrainingAdapter((ArrayList<Training>) trainings, this, trainingClicked -> {
                    imgBtnBack.setVisibility(View.VISIBLE);
                    imgBtnHome.setVisibility(View.VISIBLE);

                    List<Training> childTrainings = DataTest.findAllChildrenTrainingByParentTrainingId(trainingClicked.getId());
                    if (!childTrainings.isEmpty()) {
                        if (tvTrainingTitle.getText().length() == 0) {
                            tvTrainingTitle.setText(trainingClicked.getName());
                        } else {
                            tvTrainingTitle.setText(tvTrainingTitle.getText() + "\n" + trainingClicked.getName());
                        }
                        setMatchesIntoRecyclerView(childTrainings);
                        this.trainings = childTrainings;
                    } else {
                        if (imgBtnMusic.getTag().toString().equals("on")) {
                            stopService(new Intent(this, MusicService.class));
                        }
                        Intent intent = new Intent(this, PlayBoardActivity.class);
                        intent.putExtra("title", tvTrainingTitle.getText() + "\n" + trainingClicked.getName());
                        intent.putExtra("speaker", imgBtnSpeaker.getTag().toString());
                        intent.putExtra("music", imgBtnMusic.getTag().toString());
                        intent.putExtra("trainingId", trainingClicked.getId());
                        startActivity(intent);
                    }
                }));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setImgBtnMusicService(String tag) {
        imgBtnMusic.setTag(tag);
        if (imgBtnMusic.getTag().toString().equals("on")) {
            imgBtnMusic.setImageResource(R.drawable.music_on);
            startService(new Intent(this, MusicService.class));
        } else {
            imgBtnMusic.setImageResource(R.drawable.music_off);
            stopService(new Intent(this, MusicService.class));
        }
    }

    private void setImgBtnSpeakerService(String tag) {
        imgBtnSpeaker.setTag(tag);
        if (imgBtnSpeaker.getTag().toString().equals("on")) {
            imgBtnSpeaker.setImageResource(R.drawable.speaker_on);
        } else {
            imgBtnSpeaker.setImageResource(R.drawable.speaker_off);
            stopService(new Intent(this, SpeakerService.class));
        }
    }

}