package com.example.chinesechesstrainning.model;

import com.example.chinesechesstrainning.support.Support;

import java.io.Serializable;

public class Piece implements Serializable {
    private long id;
    private String name;
    private boolean isRed;
    private int current_col;
    private int current_row;
    private int image;
    private int clickedAndMovedImage;
    private boolean isDead;

    public Piece(long id, String name, boolean isRed) {
        this.id = id;
        this.name = name;
        this.isRed = isRed;
        Support.setPieceDefault(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRed() {
        return isRed;
    }

    public void setRed(boolean red) {
        isRed = red;
    }

    public int getCurrent_row() {
        return current_row;
    }

    public void setCurrent_row(int current_row) {
        this.current_row = current_row;
    }

    public int getCurrent_col() {
        return current_col;
    }

    public void setCurrent_col(int current_col) {
        this.current_col = current_col;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getClickedAndMovedImage() {
        return clickedAndMovedImage;
    }

    public void setClickedAndMovedImage(int clickedAndMovedImage) {
        this.clickedAndMovedImage = clickedAndMovedImage;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }
}
