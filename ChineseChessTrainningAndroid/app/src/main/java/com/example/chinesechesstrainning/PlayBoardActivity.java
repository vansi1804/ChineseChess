package com.example.chinesechesstrainning;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chinesechesstrainning.model.Move;
import com.example.chinesechesstrainning.model.Piece;
import com.example.chinesechesstrainning.model.PlayBoard;
import com.example.chinesechesstrainning.model.TrainingDetail;
import com.example.chinesechesstrainning.service.MusicService;
import com.example.chinesechesstrainning.service.SpeakerService;
import com.example.chinesechesstrainning.support.DataTest;

public class PlayBoardActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int ROW = 10;
    private static final int COL = 9;
    private ImageButton imgBtnBack;
    private ImageButton imgBtnHome;
    private ImageButton imgBtnSpeaker;
    private ImageButton imgBtnMusic;
    private TextView tvMatchTitle;
    private static ImageButton[][] imagePlayBoard;
    private TrainingDetail trainingDetail;
    private int currentTurn;
    private TextView tvTurn;
    private ImageButton imgBtnPreviousTurn;
    private ImageView imgViewTurn;
    private ImageButton imgBtnNextTurn;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_board);

        imgBtnHome = findViewById(R.id.img_btn_home);
        imgBtnHome.setOnClickListener(this);

        imgBtnBack = findViewById(R.id.img_btn_back);
        imgBtnBack.setOnClickListener(this);

        imgBtnSpeaker = findViewById(R.id.img_btn_speaker);
        imgBtnSpeaker.setOnClickListener(this);
        if (getIntent().getExtras().getString("speaker").equals("on")) {
            imgBtnSpeaker.setTag("on");
            imgBtnSpeaker.setImageResource(R.drawable.speaker_on);
        } else {
            imgBtnSpeaker.setTag("off");
            imgBtnSpeaker.setImageResource(R.drawable.speaker_off);
        }

        imgBtnMusic = findViewById(R.id.img_btn_music);
        imgBtnMusic.setOnClickListener(this);
        if (getIntent().getExtras().getString("music").equals("on")) {
            imgBtnMusic.setTag("on");
            imgBtnMusic.setImageResource(R.drawable.music_on);
            startService(new Intent(this, MusicService.class));
        } else {
            imgBtnMusic.setTag("off");
            imgBtnMusic.setImageResource(R.drawable.music_off);
        }

        imagePlayBoard = new ImageButton[COL][ROW];
        mapPlayBoardToImageButton();

        tvMatchTitle = findViewById(R.id.tv_training_title);
        tvMatchTitle.setText(getIntent().getExtras().getString("title"));

        trainingDetail = DataTest.findTrainingDetailById(getIntent().getExtras().getLong("trainingId"));

        currentTurn = 0;
        tvTurn = findViewById(R.id.tv_turn);
        tvTurn.setText(currentTurn + "/" + trainingDetail.getTotalTurn());

        imgBtnPreviousTurn = findViewById(R.id.btn_previous_turn);
        imgBtnPreviousTurn.setOnClickListener(this);
        imgBtnPreviousTurn.setEnabled(false);
        imgBtnPreviousTurn.setImageResource(R.drawable.previous_turn_disable);

        imgViewTurn = findViewById(R.id.img_view_turn);
        imgViewTurn.setImageResource(R.drawable.red_general);
        imgViewTurn.setTag("red");

        imgBtnNextTurn = findViewById(R.id.btn_next_turn);
        imgBtnNextTurn.setOnClickListener(this);
        if (trainingDetail.getTotalTurn() > 0) {
            imgBtnNextTurn.setEnabled(true);
            imgBtnNextTurn.setImageResource(R.drawable.next_turn);
        } else {
            imgBtnNextTurn.setEnabled(false);
            imgBtnNextTurn.setImageResource(R.drawable.next_turn_disable);
        }

        getIntent().removeExtra("title");
        getIntent().removeExtra("speaker");
        getIntent().removeExtra("music");
        getIntent().removeExtra("trainingId");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if (v == imgBtnHome || v == imgBtnBack) {
            if (imgBtnMusic.getTag().toString().equals("on")) {
                stopService(new Intent(this, MusicService.class));
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("title", tvMatchTitle.getText().toString());
            intent.putExtra("speaker", imgBtnSpeaker.getTag().toString());
            intent.putExtra("music", imgBtnMusic.getTag().toString());
            if (v == imgBtnHome) {
                intent.putExtra("trainingId", 0);
            } else {
                intent.putExtra("trainingId", trainingDetail.getTraining().getId());
            }
            startActivity(intent);

        } else if (v == imgBtnSpeaker) {
            if (imgBtnSpeaker.getTag().toString().equals("on")) {
                setImgBtnSpeakerService("off");
            } else {
                setImgBtnSpeakerService("on");
            }

        } else if (v == imgBtnMusic) {
            if (imgBtnMusic.getTag().toString().equals("on")) {
                setImgBtnMusicService("off");
            } else {
                setImgBtnMusicService("on");
            }
        } else {
            buildTurnEvent((v == imgBtnPreviousTurn) ? --currentTurn : ++currentTurn);
        }
    }

    public void mapPlayBoardToImageButton() {
        imagePlayBoard = new ImageButton[COL][ROW];
        for (int i = 0; i < COL; i++) {
            for (int j = 0; j < ROW; j++) {
                @SuppressLint("DiscouragedApi")
                int resourceId = getResources().getIdentifier(
                        "img_btn_position_" + i + "_" + j,
                        "id",
                        getPackageName()
                );
                imagePlayBoard[i][j] = findViewById(resourceId);
            }
        }
    }

    public void move(int turn) {
        Move move = trainingDetail.getMoves().get(turn);
        if (move != null) {
            Piece movingPiece = move.getMovingPiece();
            PlayBoard updatedPlayBoard = move.getUpdatedPlayBoard();
            setImagePlayBoard(movingPiece, updatedPlayBoard);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setImagePlayBoard(Piece movingPiece, PlayBoard updatedPlayBoard) {
        for (int col = 0; col < COL; col++) {
            for (int row = 0; row < ROW; row++) {
                imagePlayBoard[col][row].setBackground(null);
                imagePlayBoard[col][row].setImageDrawable(null);

                if (movingPiece != null
                        && col == movingPiece.getCurrentCol() && row == movingPiece.getCurrentRow()) {
                    imagePlayBoard[col][row].setImageResource(R.drawable.moved);
                }

                if (updatedPlayBoard.getState()[col][row] != null) {
                    if (movingPiece != null && updatedPlayBoard.getState()[col][row].getId() == movingPiece.getId()) {
                        imagePlayBoard[col][row].setBackground(getDrawable(R.drawable.moved));
                        imagePlayBoard[col][row].setImageDrawable(getDrawable(updatedPlayBoard.getState()[col][row].getImage()));

                    } else {
                        imagePlayBoard[col][row].setImageResource(updatedPlayBoard.getState()[col][row].getImage());
                    }
                }
            }
        }

        if (imgViewTurn.getTag().toString().equals("red")) {
            imgViewTurn.setTag("black");
            imgViewTurn.setImageResource(R.drawable.black_general);
        } else {
            imgViewTurn.setTag("red");
            imgViewTurn.setImageResource(R.drawable.red_general);
        }
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void buildTurnEvent(int turn) {
        if (imgBtnSpeaker.getTag().toString().equals("on")) {
            startService(new Intent(this, SpeakerService.class));
        }

        tvTurn.setText(turn + "/" + this.trainingDetail.getTotalTurn());
        move(turn);

        if (turn == 0) {
            setImagePlayBoard(null, DataTest.generatePlayBoard());
            imgViewTurn.setTag("red turn");
            imgBtnPreviousTurn.setEnabled(false);
            imgBtnPreviousTurn.setImageResource(R.drawable.previous_turn_disable);
        } else if (turn == trainingDetail.getTotalTurn()) {
            imgBtnNextTurn.setEnabled(false);
            imgBtnNextTurn.setImageResource(R.drawable.next_turn_disable);
        } else {
            imgBtnNextTurn.setEnabled(true);
            imgBtnNextTurn.setImageResource(R.drawable.next_turn);
            imgBtnPreviousTurn.setEnabled(true);
            imgBtnPreviousTurn.setImageResource(R.drawable.previous_turn);
        }

        if (imgBtnSpeaker.getTag().toString().equals("on")) {
            startService(new Intent(this, SpeakerService.class));
        }

        if (imgViewTurn.getTag().toString().equals("red turn")) {
            imgViewTurn.setTag("black turn");
            imgViewTurn.setBackground(getDrawable(R.drawable.black_general));
        } else {
            imgViewTurn.setTag("red turn");
            imgViewTurn.setBackground(getDrawable(R.drawable.red_general));
        }
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
        }
        stopService(new Intent(this, SpeakerService.class));
    }

}
