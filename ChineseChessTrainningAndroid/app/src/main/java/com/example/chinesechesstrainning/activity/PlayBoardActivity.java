package com.example.chinesechesstrainning.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chinesechesstrainning.R;
import com.example.chinesechesstrainning.model.PieceDTO;
import com.example.chinesechesstrainning.model.PlayBoardDTO;
import com.example.chinesechesstrainning.model.move.MoveHistoryDTO;
import com.example.chinesechesstrainning.model.training.TrainingDetailDTO;
import com.example.chinesechesstrainning.service.MusicService;
import com.example.chinesechesstrainning.service.SpeakerService;
import com.example.chinesechesstrainning.support.Support;

public class PlayBoardActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int ROW = 10;
    private static final int COL = 9;
    private ImageButton imgBtnBack;
    private ImageButton imgBtnHome;
    private ImageButton imgBtnSpeaker;
    private ImageButton imgBtnMusic;
    private TextView tvTrainingTitle;
    private static ImageButton[][] imagePlayBoard;
    private TrainingDetailDTO trainingDetailDTO;
    private ImageButton imgBtnSwapBoard;
    private Long currentTurn;
    private TextView tvTurn;
    private TextView tvMoveDescription;
    private ImageButton imgBtnPreviousTurn;
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
        setImgBtnSpeakerService(getIntent().getExtras().getString("speaker"));

        imgBtnMusic = findViewById(R.id.img_btn_music);
        imgBtnMusic.setOnClickListener(this);
        setImgBtnMusicService(getIntent().getExtras().getString("music"));

        tvTrainingTitle = findViewById(R.id.tv_training_title);
        tvTrainingTitle.setText(getIntent().getExtras().getString("title"));
        tvTrainingTitle.setSelected(true);

        imagePlayBoard = new ImageButton[COL][ROW];
        mapImageButtonPlayBoard(false);

        imgBtnSwapBoard = findViewById(R.id.btn_swap_board);
        imgBtnSwapBoard.setTag("false");
        imgBtnSwapBoard.setOnClickListener(this);

        trainingDetailDTO = Support.findTrainingDetailById(getIntent().getExtras().getLong("trainingId"));

        currentTurn = 0L;
        tvTurn = findViewById(R.id.tv_turn);

        tvMoveDescription = findViewById(R.id.tv_move_description);

        imgBtnPreviousTurn = findViewById(R.id.btn_previous_turn);
        imgBtnPreviousTurn.setOnClickListener(this);

        imgBtnNextTurn = findViewById(R.id.btn_next_turn);
        imgBtnNextTurn.setOnClickListener(this);
        buildTurnEvent(currentTurn);

        getIntent().removeExtra("title");
        getIntent().removeExtra("speaker");
        getIntent().removeExtra("music");
        getIntent().removeExtra("trainingId");
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onClick(View v) {
        if (v == imgBtnHome || v == imgBtnBack) {
            if (imgBtnMusic.getTag().toString().equals("on")) {
                stopService(new Intent(this, MusicService.class));
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("title", tvTrainingTitle.getText().toString());
            intent.putExtra("speaker", imgBtnSpeaker.getTag().toString());
            intent.putExtra("music", imgBtnMusic.getTag().toString());
            if (v == imgBtnHome) {
                intent.putExtra("trainingId", 0);
            } else {
                intent.putExtra("trainingId", trainingDetailDTO.getTrainingDTO().getId());
            }
            startActivity(intent);

        } else if (v == imgBtnSpeaker) {
            setImgBtnSpeakerService(imgBtnSpeaker.getTag().equals("on") ? "off" : "on");

        } else if (v == imgBtnMusic) {
            setImgBtnMusicService(imgBtnMusic.getTag().equals("on") ? "off" : "on");

        } else if (v == imgBtnSwapBoard) {
            if (imgBtnSwapBoard.getTag().equals("true")) {
                imgBtnSwapBoard.setTag("false");
                mapImageButtonPlayBoard(false);
            } else {
                imgBtnSwapBoard.setTag("true");
                mapImageButtonPlayBoard(true);
            }
            buildTurnEvent(currentTurn);
        } else if (v == imgBtnPreviousTurn || v == imgBtnNextTurn) {
            if (imgBtnSpeaker.getTag().toString().equals("on")) {
                startService(new Intent(this, SpeakerService.class));
            }

            buildTurnEvent((v == imgBtnPreviousTurn) ? --currentTurn : ++currentTurn);
        } else {
            if (v instanceof ImageButton) {
                String[] index = v.getTag().toString().split("-");
                int col = Integer.parseInt(index[0]);
                int row = Integer.parseInt(index[1]);
                if (imagePlayBoard[col][row].getBackground() != null) {
                    imagePlayBoard[col][row].setImageDrawable(getDrawable(R.drawable.move));
                }
            }
        }
    }

    public void mapImageButtonPlayBoard(boolean isSwap) {
        for (int col = 0; col <= COL - 1; col++) {
            for (int row = 0; row <= ROW - 1; row++) {
                int i;
                int j;
                if (isSwap) {
                    i = COL - 1 - col;
                    j = ROW - 1 - row;
                } else {
                    i = col;
                    j = row;
                }
                @SuppressLint("DiscouragedApi")
                int resourceId = getResources().getIdentifier(
                        "img_btn_position_" + i + "_" + j,
                        "id",
                        getPackageName()
                );
                imagePlayBoard[col][row] = findViewById(resourceId);
                imagePlayBoard[col][row].setTag(col + "-" + row);

                imagePlayBoard[col][row].setOnClickListener(this);
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setImagePlayBoard(PieceDTO movingPieceDTO, PlayBoardDTO playBoardDTO) {
        for (int col = 0; col < COL; col++) {
            for (int row = 0; row < ROW; row++) {
                imagePlayBoard[col][row].setImageDrawable(null);
                imagePlayBoard[col][row].setBackground(null);

                if (movingPieceDTO != null
                        && col == movingPieceDTO.getCurrentCol() && row == movingPieceDTO.getCurrentRow()) {
                    imagePlayBoard[col][row].setImageDrawable(getDrawable(R.drawable.move));
                }

                if (playBoardDTO.getState()[col][row] != null) {
                    if (movingPieceDTO != null && playBoardDTO.getState()[col][row].getId() == movingPieceDTO.getId()) {
                        imagePlayBoard[col][row].setImageDrawable(getDrawable(R.drawable.move));
                    }
                    imagePlayBoard[col][row].setBackground(getDrawable(playBoardDTO.getState()[col][row].getImage()));
                }
            }
        }
    }

    public void move(Long turn) {
        MoveHistoryDTO moveHistoryDTO = trainingDetailDTO.getMoveHistoryDTOs().get(turn);
        if (moveHistoryDTO != null) {
            tvMoveDescription.setText(moveHistoryDTO.getDescription());
            PieceDTO movingPieceDTO = moveHistoryDTO.getMovedPieceDTO();
            PlayBoardDTO updatedPlayBoardDTO = moveHistoryDTO.getCurrentBoardDTO();
            setImagePlayBoard(movingPieceDTO, updatedPlayBoardDTO);
        } else {
            tvMoveDescription.setText(null);
        }
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void buildTurnEvent(Long turn) {
        tvTurn.setText(turn + "/" + this.trainingDetailDTO.getTotalTurn());
        move(turn);

        imgBtnNextTurn.setEnabled(true);
        imgBtnNextTurn.setBackground(getDrawable(R.drawable.next_turn));
        imgBtnPreviousTurn.setEnabled(true);
        imgBtnPreviousTurn.setBackground(getDrawable(R.drawable.previous_turn));

        if (turn == 0) {
            setImagePlayBoard(null, Support.generatePlayBoard());
            imgBtnPreviousTurn.setEnabled(false);
            imgBtnPreviousTurn.setBackground(getDrawable(R.drawable.previous_turn_disable));
        }
        if (turn == trainingDetailDTO.getTotalTurn()) {
            imgBtnNextTurn.setEnabled(false);
            imgBtnNextTurn.setBackground(getDrawable(R.drawable.next_turn_disable));
        }
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
