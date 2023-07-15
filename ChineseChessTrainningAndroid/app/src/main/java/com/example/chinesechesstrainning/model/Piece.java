package com.example.chinesechesstrainning.model;

import com.example.chinesechesstrainning.support.Support;

import java.io.Serializable;

public class Piece implements Serializable {
    private long id;
    private String name;
    private boolean isRed;
    private int currentCol;
    private int currentRow;
    private int image;

    public Piece(Piece piece) {
      setId(piece.getId());
      setName(piece.getName());
      setRed(piece.isRed());
      setCurrentCol(piece.getCurrentCol());
      setCurrentRow(piece.getCurrentRow());
      setImage(piece.getImage());
    }

    public Piece(long id, String name, boolean isRed, int currentCol, int currentRow, int image) {
        this.id = id;
        this.name = name;
        this.isRed = isRed;
        this.currentCol = currentCol;
        this.currentRow = currentRow;
        this.image = image;
    }

    public Piece() {

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

    public int getCurrentCol() {
        return currentCol;
    }

    public void setCurrentCol(int currentCol) {
        this.currentCol = currentCol;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
