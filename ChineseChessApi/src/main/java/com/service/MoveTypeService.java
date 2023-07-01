package com.service;

public interface MoveTypeService {

    boolean isHorizontalMoving(int fromRow, int toRow);

    boolean isUpMoving(boolean isRed, int fromRow, int toRow);

    boolean isVerticallyMoving(int fromCol, int toCol);
    
}
