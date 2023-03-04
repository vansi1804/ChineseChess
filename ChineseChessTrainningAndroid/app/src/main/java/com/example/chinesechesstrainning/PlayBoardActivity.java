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

import com.example.chinesechesstrainning.model.MatchDetail;
import com.example.chinesechesstrainning.model.Piece;
import com.example.chinesechesstrainning.service.MusicService;
import com.example.chinesechesstrainning.service.SpeakerService;
import com.example.chinesechesstrainning.support.DataTest;

import java.util.ArrayList;

public class PlayBoardActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int ROW = 10;
    private static final int COL = 9;
    private ImageButton imgBtnBack;
    private ImageButton imgBtnHome;
    private ImageButton imgBtnSpeaker;
    private ImageButton imgBtnMusic;
    private TextView tvMatchTitle;
    private static ImageButton[][] board;
    private ArrayList<Piece> pieces;
    private ArrayList<MatchDetail> matchDetails;
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
        if (getIntent().getExtras().getString("speaker").equals("on")){
            imgBtnSpeaker.setTag("on");
            imgBtnSpeaker.setImageResource(R.drawable.speaker_on);
        }else{
            imgBtnSpeaker.setTag("off");
            imgBtnSpeaker.setImageResource(R.drawable.speaker_off);
        }
        imgBtnMusic = findViewById(R.id.img_btn_music);
        imgBtnMusic.setOnClickListener(this);
        if (getIntent().getExtras().getString("music").equals("on")){
            imgBtnMusic.setTag("on");
            imgBtnMusic.setImageResource(R.drawable.music_on);
            startService(new Intent(this,MusicService.class));
        }else{
            imgBtnMusic.setTag("off");
            imgBtnMusic.setImageResource(R.drawable.music_off);
        }
        tvMatchTitle = findViewById(R.id.tv_match_title);
        tvMatchTitle.setText(getIntent().getExtras().getString("title"));
        mappingImageButtonAndBoard();
        pieces = DataTest.getPieces();
        matchDetails = DataTest.getMatchDetailsByMatchId(getIntent().getExtras().getLong("match id"));
        currentTurn = 0;
        tvTurn = findViewById(R.id.tv_turn);
        tvTurn.setText("0/"+ matchDetails.size());
        imgBtnPreviousTurn = findViewById(R.id.btn_previous_turn);
        imgBtnPreviousTurn.setOnClickListener(this);
        imgBtnPreviousTurn.setEnabled(false);
        imgBtnPreviousTurn.setImageResource(R.drawable.previous_turn_disable);
        imgViewTurn = findViewById(R.id.img_view_turn);
        imgViewTurn.setImageResource(R.drawable.red_general);
        imgViewTurn.setTag("red turn");
        imgBtnNextTurn = findViewById(R.id.btn_next_turn);
        imgBtnNextTurn.setOnClickListener(this);
        refreshBoard();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if (v == imgBtnHome){
            if (imgBtnMusic.getTag().equals("on")){
                stopService(new Intent(this,MusicService.class));
            }
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("title", "");
            intent.putExtra("speaker",imgBtnSpeaker.getTag().toString());
            intent.putExtra("music",imgBtnMusic.getTag().toString());
            intent.putExtra("match id", 0);
            startActivity(intent);
        }else if(v == imgBtnBack){
            if (imgBtnMusic.getTag().equals("on")){
                stopService(new Intent(this, MusicService.class));
            }
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("title", tvMatchTitle.getText().toString()
                                            .substring(0,tvMatchTitle.getText().toString().lastIndexOf('\n')));
            intent.putExtra("speaker",imgBtnSpeaker.getTag().toString());
            intent.putExtra("music",imgBtnMusic.getTag().toString());
            intent.putExtra("match id", matchDetails.get(0).getMatchId());
            startActivity(intent);
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
        }else if (v == imgBtnNextTurn){
            currentTurn ++;
            tvTurn.setText(currentTurn +"/"+ matchDetails.size());
            if (imgBtnSpeaker.getTag().equals("on")){
                startService(new Intent(this, SpeakerService.class));
            }
            move(matchDetails.get(currentTurn-1));
            if (currentTurn == matchDetails.size()){
                imgBtnNextTurn.setEnabled(false);
                imgBtnNextTurn.setImageResource(R.drawable.next_turn_disable);
            }
            imgBtnPreviousTurn.setEnabled(true);
            imgBtnPreviousTurn.setImageResource(R.drawable.previous_turn);
            if (imgViewTurn.getTag().equals("red turn")){
                imgViewTurn.setImageResource(R.drawable.black_general);
                imgViewTurn.setTag("black turn");
            }else{
                imgViewTurn.setImageResource(R.drawable.black_general);
                imgViewTurn.setTag("red turn");
            }

        }else if (v == imgBtnPreviousTurn){
            currentTurn --;
            tvTurn.setText(currentTurn +"/"+ matchDetails.size());
            pieces = DataTest.getPieces();
            if (imgBtnSpeaker.getTag().equals("on")){
                startService(new Intent(this, SpeakerService.class));
            }
            if (currentTurn == 0){
                refreshBoard();
                imgBtnPreviousTurn.setEnabled(false);
                imgBtnPreviousTurn.setImageResource(R.drawable.previous_turn_disable);
            }else{
                for (int i = 0; i < currentTurn; i++) move(matchDetails.get(i));
            }
            imgBtnNextTurn.setEnabled(true);
            imgBtnNextTurn.setImageResource(R.drawable.next_turn);
            if (imgViewTurn.getTag().equals("red turn")){
                imgViewTurn.setImageResource(R.drawable.black_general);
                imgViewTurn.setTag("black turn");
            }else{
                imgViewTurn.setImageResource(R.drawable.red_general);
                imgViewTurn.setTag("red turn");
            }
        }
    }

    public void mappingImageButtonAndBoard(){
        board = new ImageButton[COL][ROW];
        //////////////////////////////////////////////////////
        board[0][0] = findViewById(R.id.img_btn_position_0_0);
        board[1][0] = findViewById(R.id.img_btn_position_1_0);
        board[2][0] = findViewById(R.id.img_btn_position_2_0);
        board[3][0] = findViewById(R.id.img_btn_position_3_0);
        board[4][0] = findViewById(R.id.img_btn_position_4_0);
        board[5][0] = findViewById(R.id.img_btn_position_5_0);
        board[6][0] = findViewById(R.id.img_btn_position_6_0);
        board[7][0] = findViewById(R.id.img_btn_position_7_0);
        board[8][0] = findViewById(R.id.img_btn_position_8_0);
        //////////////////////////////////////////////////////
        board[0][1] = findViewById(R.id.img_btn_position_0_1);
        board[1][1] = findViewById(R.id.img_btn_position_1_1);
        board[2][1] = findViewById(R.id.img_btn_position_2_1);
        board[3][1] = findViewById(R.id.img_btn_position_3_1);
        board[4][1] = findViewById(R.id.img_btn_position_4_1);
        board[5][1] = findViewById(R.id.img_btn_position_5_1);
        board[6][1] = findViewById(R.id.img_btn_position_6_1);
        board[7][1] = findViewById(R.id.img_btn_position_7_1);
        board[8][1] = findViewById(R.id.img_btn_position_8_1);
        //////////////////////////////////////////////////////
        board[0][2] = findViewById(R.id.img_btn_position_0_2);
        board[1][2] = findViewById(R.id.img_btn_position_1_2);
        board[2][2] = findViewById(R.id.img_btn_position_2_2);
        board[3][2] = findViewById(R.id.img_btn_position_3_2);
        board[4][2] = findViewById(R.id.img_btn_position_4_2);
        board[5][2] = findViewById(R.id.img_btn_position_5_2);
        board[6][2] = findViewById(R.id.img_btn_position_6_2);
        board[7][2] = findViewById(R.id.img_btn_position_7_2);
        board[8][2] = findViewById(R.id.img_btn_position_8_2);
        //////////////////////////////////////////////////////
        board[0][3] = findViewById(R.id.img_btn_position_0_3);
        board[1][3] = findViewById(R.id.img_btn_position_1_3);
        board[2][3] = findViewById(R.id.img_btn_position_2_3);
        board[3][3] = findViewById(R.id.img_btn_position_3_3);
        board[4][3] = findViewById(R.id.img_btn_position_4_3);
        board[5][3] = findViewById(R.id.img_btn_position_5_3);
        board[6][3] = findViewById(R.id.img_btn_position_6_3);
        board[7][3] = findViewById(R.id.img_btn_position_7_3);
        board[8][3] = findViewById(R.id.img_btn_position_8_3);
        //////////////////////////////////////////////////////
        board[0][4] = findViewById(R.id.img_btn_position_0_4);
        board[1][4] = findViewById(R.id.img_btn_position_1_4);
        board[2][4] = findViewById(R.id.img_btn_position_2_4);
        board[3][4] = findViewById(R.id.img_btn_position_3_4);
        board[4][4] = findViewById(R.id.img_btn_position_4_4);
        board[5][4] = findViewById(R.id.img_btn_position_5_4);
        board[6][4] = findViewById(R.id.img_btn_position_6_4);
        board[7][4] = findViewById(R.id.img_btn_position_7_4);
        board[8][4] = findViewById(R.id.img_btn_position_8_4);
        //////////////////////////////////////////////////////
        board[0][5] = findViewById(R.id.img_btn_position_0_5);
        board[1][5] = findViewById(R.id.img_btn_position_1_5);
        board[2][5] = findViewById(R.id.img_btn_position_2_5);
        board[3][5] = findViewById(R.id.img_btn_position_3_5);
        board[4][5] = findViewById(R.id.img_btn_position_4_5);
        board[5][5] = findViewById(R.id.img_btn_position_5_5);
        board[6][5] = findViewById(R.id.img_btn_position_6_5);
        board[7][5] = findViewById(R.id.img_btn_position_7_5);
        board[8][5] = findViewById(R.id.img_btn_position_8_5);
        //////////////////////////////////////////////////////
        board[0][6] = findViewById(R.id.img_btn_position_0_6);
        board[1][6] = findViewById(R.id.img_btn_position_1_6);
        board[2][6] = findViewById(R.id.img_btn_position_2_6);
        board[3][6] = findViewById(R.id.img_btn_position_3_6);
        board[4][6] = findViewById(R.id.img_btn_position_4_6);
        board[5][6] = findViewById(R.id.img_btn_position_5_6);
        board[6][6] = findViewById(R.id.img_btn_position_6_6);
        board[7][6] = findViewById(R.id.img_btn_position_7_6);
        board[8][6] = findViewById(R.id.img_btn_position_8_6);
        //////////////////////////////////////////////////////
        board[0][7] = findViewById(R.id.img_btn_position_0_7);
        board[1][7] = findViewById(R.id.img_btn_position_1_7);
        board[2][7] = findViewById(R.id.img_btn_position_2_7);
        board[3][7] = findViewById(R.id.img_btn_position_3_7);
        board[4][7] = findViewById(R.id.img_btn_position_4_7);
        board[5][7] = findViewById(R.id.img_btn_position_5_7);
        board[6][7] = findViewById(R.id.img_btn_position_6_7);
        board[7][7] = findViewById(R.id.img_btn_position_7_7);
        board[8][7] = findViewById(R.id.img_btn_position_8_7);
        //////////////////////////////////////////////////////
        board[0][8] = findViewById(R.id.img_btn_position_0_8);
        board[1][8] = findViewById(R.id.img_btn_position_1_8);
        board[2][8] = findViewById(R.id.img_btn_position_2_8);
        board[3][8] = findViewById(R.id.img_btn_position_3_8);
        board[4][8] = findViewById(R.id.img_btn_position_4_8);
        board[5][8] = findViewById(R.id.img_btn_position_5_8);
        board[6][8] = findViewById(R.id.img_btn_position_6_8);
        board[7][8] = findViewById(R.id.img_btn_position_7_8);
        board[8][8] = findViewById(R.id.img_btn_position_8_8);
        //////////////////////////////////////////////////////
        board[0][9] = findViewById(R.id.img_btn_position_0_9);
        board[1][9] = findViewById(R.id.img_btn_position_1_9);
        board[2][9] = findViewById(R.id.img_btn_position_2_9);
        board[3][9] = findViewById(R.id.img_btn_position_3_9);
        board[4][9] = findViewById(R.id.img_btn_position_4_9);
        board[5][9] = findViewById(R.id.img_btn_position_5_9);
        board[6][9] = findViewById(R.id.img_btn_position_6_9);
        board[7][9] = findViewById(R.id.img_btn_position_7_9);
        board[8][9] = findViewById(R.id.img_btn_position_8_9);
        //////////////////////////////////////////////////////

    }

    public void move(MatchDetail matchDetail){
        refreshBoard();
        for (Piece piece: pieces) {
            if (!piece.isDead() && piece.getCurrent_col() == matchDetail.getNext_col() && piece.getCurrent_row() == matchDetail.getNext_row()){
                piece.setDead(true);
            }
            if (!piece.isDead() && piece.getId() == matchDetail.getPiece_id()){
                board[matchDetail.getCurrent_col()-1][matchDetail.getCurrent_row()-1]
                        .setImageResource(R.drawable.moved);
                board[matchDetail.getNext_col()-1][matchDetail.getNext_row()-1]
                        .setImageResource(piece.getClickedAndMovedImage());
                piece.setCurrent_col(matchDetail.getNext_col());
                piece.setCurrent_row(matchDetail.getNext_row());
            }
        }
    }

    public void refreshBoard(){
        for (int col = 0; col < COL; col++) {
            for (int row = 0; row < ROW; row++) {
                board[col][row].setBackground(null);
                board[col][row].setImageDrawable(null);
                for (Piece piece: pieces) {
                    if (!piece.isDead()){
                        board[piece.getCurrent_col()-1][piece.getCurrent_row()-1].setImageResource(piece.getImage());
                    }
                }
            }
        }
    }

}
